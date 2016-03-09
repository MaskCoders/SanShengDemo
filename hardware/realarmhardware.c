#include <stdio.h>  
#include <stdlib.h>
#include <termios.h>
#include <unistd.h> 
#include <sys/types.h>
#include <sys/stat.h> 
#include <fcntl.h>
#include <string.h>
#include "jni.h"
#include "JNIHelp.h"
#include <assert.h> 
#include "can.h"
#include <sys/socket.h>
#include <net/if.h>
#include <cutils/properties.h>
#include <sys/wait.h>
// 引入log头文件
#include <android/log.h>  
// log标签
#define  TAG    "REALARM_HARDWARE"
// 定义info信息
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO, TAG, __VA_ARGS__)
// 定义debug信息
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, TAG, __VA_ARGS__)
// 定义error信息
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#define DEVICE_NAME		"/dev/real_led"

/*LED JNI*/
static jint Java_realarm_hardware_HardwareControl_LedSetState
  	(JNIEnv *env, jobject thiz,jint ledState)
{
	int fd = open(DEVICE_NAME, 0);
	
	if (fd == -1)
	{
		LOGE("led open error");
		return 1;	
	}
	
	
	ledState &= 0x01;
	ioctl(fd, ledState, 1);

	close(fd);
	
	return 0;
}

#if 1 //+=
#define SYSFS_GPIO_DIR        	"/sys/class/gpio"
// UART mode setup, 1 is RS485, 0 is RS232
#define UART_MODE_GPIO_SET		155 // GPIOE_27
// PLC power enable
#define PLC_POWER_GPIO_SET		156 // GPIOE_28

// dir: 1 is out, 0 is in
static int gpio_setvalue(unsigned int gpio, int dir, int value)
{
	int fd = -1;
	char cmd[64] = {0};
	char dev[64] = {0};

    sprintf(cmd, "%d", gpio);
	fd = open("/sys/class/gpio/export", O_WRONLY | O_TRUNC);
	if (fd < 0)
	{
		perror("open device failed!");
        return 1;
	}
	write(fd, cmd, strlen(cmd));
	close(fd);

	if (1 == dir)
		sprintf(cmd, "out");
	else if (0 == dir)
		sprintf(cmd, "in");
	else
		sprintf(cmd, "in");

 	sprintf(dev, "/sys/class/gpio/gpio%d/direction", gpio);
	fd = open(dev, O_WRONLY | O_TRUNC);
	if (fd < 0)
	{
		perror("open device failed!!");
		return 1;
	}
	write(fd, cmd, strlen(cmd));
	close(fd);

	sprintf(cmd, "%d", value); // 1=out 0=in
 	sprintf(dev, "/sys/class/gpio/gpio%d/value", gpio);
	fd = open(dev, O_WRONLY | O_TRUNC);
	if (fd < 0)
	{
		perror("open device failed!!!");
		return 1;
	}
	write(fd, cmd, strlen(cmd));
    close(fd);

    fd = open("/sys/class/gpio/unexport", O_WRONLY | O_TRUNC);
    sprintf(cmd, "%d", gpio);
    write(fd, cmd, strlen(cmd));
    close(fd);
    
    return 0;
}

static jint Java_realarm_hardware_HardwareControl_UartModeSetup
  	(JNIEnv *env, jobject thiz,jint uartMode)
{
#if 0 //+=
	int ret = 1;
	if (uartMode)
		ret = gpio_setvalue(UART_MODE_GPIO_SET, 1, 1);
	else
		ret = gpio_setvalue(UART_MODE_GPIO_SET, 1, 0);

	return ret;
#else	
	int fd, len;
	char fsBuf[64] = {0};

	snprintf(fsBuf, sizeof(fsBuf), SYSFS_GPIO_DIR "/gpio%d/value", UART_MODE_GPIO_SET);

	fd = open(fsBuf, O_WRONLY);
	if (fd < 0) {
		return 1;
	}

	if (uartMode)
		len = write(fd, "1", 2);
	else
		len = write(fd, "0", 2);

	if (len < 2)
		return 1;

	close(fd);
	
	return 0;
#endif
}

// 1 enable power, 0 disable power
static jint Java_realarm_hardware_HardwareControl_PlcPowerSetup
  	(JNIEnv *env, jobject thiz,jint powerEnable)
{
#if 0 //+=
	int ret = 1;
	if (powerEnable)
		ret = gpio_setvalue(PLC_POWER_GPIO_SET, 1, 1);
	else
		ret = gpio_setvalue(PLC_POWER_GPIO_SET, 1, 0);

	return ret;
#else
	int fd, len;
	char fsBuf[64] = {0};

	snprintf(fsBuf, sizeof(fsBuf), SYSFS_GPIO_DIR "/gpio%d/value", PLC_POWER_GPIO_SET);

	fd = open(fsBuf, O_WRONLY);
	if (fd < 0) {
		return 1;
	}

	if (powerEnable)
		len = write(fd, "1", 2);
	else
		len = write(fd, "0", 2);

	if (len < 2)
		return 1;

	close(fd);
	
	return 0;
#endif
}

#define SYSFS_PWM_DIR        	"/sys/devices/platform/pwm"
static jint Java_realarm_hardware_HardwareControl_PwmEnableSetup
  	(JNIEnv *env, jobject thiz,jint pwmEnable)
{
	int fd, len;
	char fsBuf[64] = {0};
	char freq_duty50[] = "38000,50";
	char freq_duty00[] = "38000,00";

	snprintf(fsBuf, sizeof(fsBuf), SYSFS_PWM_DIR "/pwm.%d", 0);

	fd = open(fsBuf, O_WRONLY);
	if (fd < 0) {
		return 1;
	}

	if (pwmEnable)
		len = write(fd, freq_duty50, sizeof(freq_duty50));
	else
		len = write(fd, freq_duty00, sizeof(freq_duty00));

	if (len < sizeof(freq_duty50))
		return 1;

	close(fd);
	
	return 0;
}
#endif

/*UART JNI*/
int serialfd=-1;
static speed_t getBaudrate(jint baudrate)
{
	switch(baudrate) {
	case 0: return B0;
	case 50: return B50;
	case 75: return B75;
	case 110: return B110;
	case 134: return B134;
	case 150: return B150;
	case 200: return B200;
	case 300: return B300;
	case 600: return B600;
	case 1200: return B1200;
	case 1800: return B1800;
	case 2400: return B2400;
	case 4800: return B4800;
	case 9600: return B9600;
	case 19200: return B19200;
	case 38400: return B38400;
	case 57600: return B57600;
	case 115200: return B115200;
	case 230400: return B230400;
	case 460800: return B460800;
	case 500000: return B500000;
	case 576000: return B576000;
	case 921600: return B921600;
	case 1000000: return B1000000;
	case 1152000: return B1152000;
	case 1500000: return B1500000;
	case 2000000: return B2000000;
	case 2500000: return B2500000;
	case 3000000: return B3000000;
	case 3500000: return B3500000;
	case 4000000: return B4000000;
	default: return -1;
	}
}

/*
 * Class:     cedric_serial_SerialPort
 * Method:    open
 * Signature: (Ljava/lang/String;)V
 */
static jobject Java_realarm_hardware_HardwareControl_OpenSerialPort
  (JNIEnv *env, jobject thiz, jstring path, jint baudrate, jint databits, jint stopbits, jint parity, jint flags)
{
	int fd;
	speed_t speed;
	jobject mFileDescriptor;

	LOGD("baudrate %d", baudrate);
	/* Check arguments */
	{
		speed = getBaudrate(baudrate);
		if (speed == -1) {
			/* TODO: throw an exception */
			LOGE("Invalid baudrate");
			return NULL;
		}
	}

	/* Opening device */
	{
		jboolean iscopy;
		const char *path_utf = (*env)->GetStringUTFChars(env, path, &iscopy);
		LOGD("Opening serial port %s", path_utf);
		fd = open(path_utf, O_RDWR);
		serialfd=fd;
		LOGD("open() fd = %d", fd);
		(*env)->ReleaseStringUTFChars(env, path, path_utf);
		if (fd == -1)
		{
			/* Throw an exception */
			LOGE("Cannot open port");
			/* TODO: throw an exception */
			return NULL;
		}
	}

	/* Configure device */
	{
		struct termios cfg;
		LOGD("Configuring serial port");
		if (tcgetattr(fd, &cfg))
		{
			LOGE("tcgetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}

		cfmakeraw(&cfg);
		cfsetispeed(&cfg, speed);
		cfsetospeed(&cfg, speed);

		if (tcsetattr(fd, TCSANOW, &cfg))
		{
			LOGE("tcsetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}
	}

	{
		struct termios cfg;

		if (tcgetattr(fd, &cfg))
		{
			LOGE("tcgetattr() failed");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}

		cfg.c_cflag &= ~CSIZE;
		cfg.c_iflag &= ~(IXON | IXOFF | IXANY | BRKINT | ICRNL | INPCK | ISTRIP);
		cfg.c_lflag &= ~(ICANON | ECHO | ECHOE | ISIG); 
		cfg.c_oflag &= ~OPOST;
		
    	switch (databits)
    	{     
		case 5:
            cfg.c_cflag |= CS5;
            break;
        case 6:
			cfg.c_cflag |= CS6;
            break;
      	case 7:       
        	cfg.c_cflag |= CS7;   
        	break;  
    	case 8:       
        	cfg.c_cflag |= CS8;  
        	break;     
    	default:      
        	cfg.c_cflag |= CS8;  
            break;
    	}  
    
    	switch (stopbits)  
    	{     
        case 1:      
            cfg.c_cflag &= ~CSTOPB;    
            break;    
        case 2:      
          	cfg.c_cflag |= CSTOPB;    
           	break;  
        default:      
            cfg.c_cflag &= ~CSTOPB;   
            break; 
    	}   

    	switch (parity)   
    	{     
        case 'n':  
        case 'N':      
            cfg.c_cflag &= ~PARENB;		/* Clear parity enable */  
            cfg.c_iflag &= ~INPCK;     	/* Enable parity checking */   
            break;    
        case 'o':     
        case 'O':       
            cfg.c_cflag |= (PARODD | PARENB); /* Odd parity */
            cfg.c_iflag |= INPCK;       /* Disnable parity checking */   
            break;    
        case 'e':    
        case 'E':     
            cfg.c_cflag |= PARENB;     	/* Enable parity */      
            cfg.c_cflag &= ~PARODD;   	/* Even parity */
            cfg.c_iflag |= INPCK;       /* Disnable parity checking */  
            break;  
        default:     
            cfg.c_cflag &= ~PARENB;		/* Clear parity enable */  
            cfg.c_iflag &= ~INPCK;     	/* Enable parity checking */   
            break;
        }    

    	/* Set input parity option */   
    	if ((parity != 'n') || (parity != 'N'))         
        	cfg.c_iflag |= INPCK;   

		cfg.c_cc[VTIME] = 150;
		cfg.c_cc[VMIN] = 0;

		tcflush(fd, TCIFLUSH);
		if (tcsetattr(fd, TCSANOW, &cfg))
		{
			LOGE("tcsetattr() failed!");
			close(fd);
			/* TODO: throw an exception */
			return NULL;
		}
	}

	// Create a corresponding file descriptor 
	{
		jclass cFileDescriptor = (*env)->FindClass(env, "java/io/FileDescriptor");
		jmethodID iFileDescriptor = (*env)->GetMethodID(env, cFileDescriptor, "<init>", "()V");
		jfieldID descriptorID = (*env)->GetFieldID(env, cFileDescriptor, "descriptor", "I");
		mFileDescriptor = (*env)->NewObject(env, cFileDescriptor, iFileDescriptor);
		(*env)->SetIntField(env, mFileDescriptor, descriptorID, (jint)fd);
	}

	return mFileDescriptor;
}


/*
 * Class:     cedric_serial_SerialPort
 * Method:    close
 * Signature: ()V
 */
static void Java_realarm_hardware_HardwareControl_CloseSerialPort
  (JNIEnv *env, jobject thiz)
{


	LOGE("close(fd = %d)", serialfd);
	if(-1!=serialfd)
		close(serialfd);
}


/*realarm开发板对485的使能做了硬件处理，所以485操作与串口并无区别*/
/*RS485 JNI*/


/*CAN JNI*/

int canfd=-1;
struct sockaddr_can addr;

static void Java_realarm_hardware_HardwareControl_InitCan
  (JNIEnv *env, jobject thiz, jint baudrate)
{

	/* Check arguments */
	switch (baudrate)
	{
		case 5000   :
		case 10000  :
		case 20000  :
		case 50000  :
		case 100000 :
		case 125000 :
			LOGI("Can Bus Speed is %d",baudrate);
		break;
		default:
			LOGI("Can Bus Speed is %d.if it do not work,try 5000~125000",baudrate);
	}

	/* Configure device */
	if(baudrate!=0)
	{
		char str_baudrate[16];
		sprintf(str_baudrate,"%d", baudrate);
		property_set("net.can.baudrate", str_baudrate); 
		LOGI("str_baudrate is:%s", str_baudrate);
		property_set("net.can.change", "yes");
		
	}	
}
static jint Java_realarm_hardware_HardwareControl_OpenCan
  (JNIEnv *env, jobject thiz)
{

	struct ifreq ifr;
	int ret;     
	
	/* Opening device */
	canfd = socket(PF_CAN,SOCK_RAW,CAN_RAW);

	if(canfd==-1)
	{
		LOGE("Can Write Without Open"); 
		return   0;
	}

	strcpy((char *)(ifr.ifr_name),"can0");
	ioctl(canfd,SIOCGIFINDEX,&ifr);



	addr.can_family = AF_CAN;
	addr.can_ifindex = ifr.ifr_ifindex;
	bind(canfd,(struct sockaddr*)&addr,sizeof(addr));

	return canfd;
}

static jint Java_realarm_hardware_HardwareControl_CanWrite
  (JNIEnv *env, jobject thiz, jint canId, jstring data)
{

	int nbytes;
	int k;
	struct can_frame frame;
	memset(&frame,0,sizeof(struct can_frame));
	jboolean iscopy = 0;

	const char *send_data = (*env)->GetStringUTFChars(env, data, &iscopy);	
	
	frame.can_id = canId;
	frame.can_dlc = strlen(send_data);
	
	strncpy((char *)frame.data,send_data,frame.can_dlc);
	
	nbytes = sendto(canfd,&frame,sizeof(struct can_frame),0,(struct sockaddr*)&addr,sizeof(addr));

	LOGD("write nbytes=%d",nbytes);
	
	return nbytes;
}

static jobject Java_realarm_hardware_HardwareControl_CanRead
  (JNIEnv *env, jobject thiz, jobject obj, jint time)
{

	unsigned long nbytes,len;

	struct can_frame frame = {0};
	int k=0;
	jstring   jstr; 
	
	char temp[16];

	fd_set rfds;
	int retval;
	struct timeval tv;
        tv.tv_sec = time;  		
        tv.tv_usec = 0;

	bzero(temp,16);
	if(canfd==-1){
		LOGE("Can Read Without Open");
		frame.can_id=0;
		frame.can_dlc=0;
	}else{
		FD_ZERO(&rfds);
		FD_SET(canfd, &rfds);
		retval = select(canfd+1 , &rfds, NULL, NULL, &tv);
		if (retval == -1){
			LOGE("Can Read slect error");
		}else if (retval == 0){
			LOGD("Can Read timer out,do data");
		}else{
			nbytes = recvfrom(canfd, &frame, sizeof(struct can_frame), 0, (struct sockaddr *)&addr,&len);
		
			for(k = 0;k < frame.can_dlc;k++)
				temp[k] = frame.data[k];
			temp[k] = 0;
			
			frame.can_id = frame.can_id & CAN_EFF_MASK;//支持扩展帧,标准帧11bit,扩展29bit
			LOGD("Can Read slect success.");
		}

	}
	
		
    jclass objectClass = (*env)->FindClass(env,"realarm/hardware/CanFrame");
    jfieldID id = (*env)->GetFieldID(env,objectClass,"can_id","I");
    jfieldID leng = (*env)->GetFieldID(env,objectClass,"can_dlc","C");
    jfieldID str = (*env)->GetFieldID(env,objectClass,"data","Ljava/lang/String;");
    
	if(frame.can_dlc) {	
		LOGD("can_id is :%d", frame.can_id);
		LOGD("can read nbytes=%d", frame.can_dlc);
		LOGD("can data is:%s", temp);
	}
	
    (*env)->SetCharField(env, obj, leng, frame.can_dlc);
    (*env)->SetObjectField(env, obj, str, (*env)->NewStringUTF(env,temp));
    (*env)->SetIntField(env, obj, id, frame.can_id);
	 
	return   obj;
}

static void Java_realarm_hardware_HardwareControl_CloseCan
  (JNIEnv *env, jobject thiz)
{

	if(canfd!=-1)
		close(canfd);
	canfd=-1;
	LOGD("close can0");
}

/*CAN JNI*/

static JNINativeMethod gMethods[] = {  
	{"LedSetState", "(I)I", (void *)Java_realarm_hardware_HardwareControl_LedSetState}, 
	{"OpenSerialPort", "(Ljava/lang/String;IIIII)Ljava/io/FileDescriptor;", (void *)Java_realarm_hardware_HardwareControl_OpenSerialPort},
	{"CloseSerialPort", "()V", (void *)Java_realarm_hardware_HardwareControl_CloseSerialPort},
	{"InitCan", "(I)V", (void *)Java_realarm_hardware_HardwareControl_InitCan},  
	{"OpenCan", "()I", (void *)Java_realarm_hardware_HardwareControl_OpenCan},
	{"CanWrite", "(ILjava/lang/String;)I", (void *)Java_realarm_hardware_HardwareControl_CanWrite}, 
	{"CanRead", "(Lrealarm/hardware/CanFrame;I)Lrealarm/hardware/CanFrame;", 
						(void *)Java_realarm_hardware_HardwareControl_CanRead},
	{"CloseCan", "()V", (void *)Java_realarm_hardware_HardwareControl_CloseCan}, 
#if 1 //+=
	{"UartModeSetup", "(I)I", (void *)Java_realarm_hardware_HardwareControl_UartModeSetup}, 
	{"PlcPowerSetup", "(I)I", (void *)Java_realarm_hardware_HardwareControl_PlcPowerSetup}, 
	{"PwmEnableSetup", "(I)I", (void *)Java_realarm_hardware_HardwareControl_PwmEnableSetup}, 
#endif
}; 
 
static int register_android_realarm_test(JNIEnv *env)  
{  
   	jclass clazz;
    static const char* const kClassName =  "realarm/hardware/HardwareControl";

    /* look up the class */
    clazz = (*env)->FindClass(env, kClassName);
 
    if (clazz == NULL) {
        LOGE("Can't find class %s\n", kClassName);
        return -1;
    }

    /* register all the methods */
    if ((*env)->RegisterNatives(env,clazz, gMethods, sizeof(gMethods) / sizeof(gMethods[0])) != JNI_OK)
   
    {
        LOGE("Failed registering methods for %s\n", kClassName);
        return -1;
    }

    /* fill out the rest of the ID cache */
    return 0;
}

jint JNI_OnLoad(JavaVM* vm, void* reserved) {
    

	JNIEnv *env = NULL;
	if ((*vm)->GetEnv(vm,(void**) &env, JNI_VERSION_1_6) != JNI_OK) {  

		LOGI("Error GetEnv\n");  
		return -1;  
	} 
	assert(env != NULL);  
	if (register_android_realarm_test(env) < 0) {  
		printf("register_android_realarm_test error.\n"); 
		LOGE("register_android_realarm_test error."); 
		return -1;  
	}

    /* success -- return valid version number */
	LOGI("/*****************realarm**********************/");

    return JNI_VERSION_1_6;
}

