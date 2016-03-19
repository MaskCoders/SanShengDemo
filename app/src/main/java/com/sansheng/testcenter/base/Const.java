package com.sansheng.testcenter.base;

/**
 * Created by hua on 12/18/15.
 */
public interface Const {
    static final int PORT = 8001;
    static final String ERRCODE = "ERRCODE";
    static final String CONN_SUCCESS = "CONN_SUCCESS";
    static final String HOST = "127.0.0.1";

    static final int CONN_SER_CLS = -2;
    static final int OVER_TIME = -4;
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int RECV_MSG_PARSE_ERR = 4;
    static final int SEND_MSG = 2;
    static final int CONN_CLOSE = 3;
    static final int INPUT_ERR = -3;

    enum CX {
        XXX(11);

        private int value;

        private CX(int v) {
            value = v;
        }
    }

    interface WhmConst {
        enum C {
            MAIN_REQUEST_READ_DATA(0x11),
            SLAVE_RESPONSE_READ_DATA(0x91),
            SLAVE_RESPONSE_READ_DATA_ERR(0xD1,1),
            //1.2 读后续数据
            MAIN_REQUEST_FRAME(0x12,5),
            SLAVE_RESPONSE_FRAME(0x92,5),
            SLAVE_RESPONSE_FRAME_ERR(0xD2,1),
            //1.3 写数据
            MAIN_REQUEST_WRITE(0x14,12),
            SLAVE_RESPONSE_WRITE(0x94,0),
            SLAVE_RESPONSE_WRITE_ERR(0xD4,1),
            //1.4 读通信地址
            MAIN_REQUEST_RED_ADDRESS(0x13,0),
            SLAVE_RESPONSE_RED_ADDRESS(0x93,6),
            // SLAVE_RESPONSE_RED_ADDRESS_ERR (0xD3),
            //1.5 写通信地址
            MAIN_REQUEST_WRITE_ADDRESS(0x15,6),
            SLAVE_RESPONSE_WRITE_ADDRESS(0x95,0),
            //1.6 广播校时
            CALIBRATION_TIME(0x08,6),
            //1.7 冻结命令
            MAIN_REQUEST_FROZEN_DATA(0x16),
            SLAVE_RESPONSE_FROZEN_DATA(0x96,0),
            SLAVE_RESPONSE_FROZEN_DATA_ERR(0xD6,1),
            //1.8 更改通信速率
            MAIN_REQUEST_COMMUNICATION_RATE(0x17,1),
            SLAVE_RESPONSE_COMMUNICATION_RATE(0x97,1),
            SLAVE_RESPONSE_COMMUNICATION_RATE_ERR(0xD7,1),
            //1.9 修改密码
            MAIN_REQUEST_CHANGE_PW(0x18,12),
            SLAVE_RESPONSE_CHANGE_PW(0x98),
            SLAVE_RESPONSE_CHANGE_PW_ERR(0xD8,1),
            //1.10 最大需量清零
            MAIN_REQUEST_ALL_CLEAR(0x19,8),
            SLAVE_RESPONSE_ALL_CLEAR(0x99,0),
            SLAVE_RESPONSE_ALL_CLEAR_ERR(0xD9,1),
            //1.11 电表清零
            MAIN_REQUEST_CLEAR(0x1A,8),
            SLAVE_RESPONSE_CLEAR(0x9A,0),
            SLAVE_RESPONSE_CLEAR_ERR(0xDA,1),
            //1.12 事件清零
            MAIN_REQUEST_EVENT_CLEAR(0x1B,12),
            SLAVE_RESPONSE_EVENT_CLEAR(0x9B,0),
            SLAVE_RESPONSE_EVENT_CLEAR_ERR(0xDB,1),
            //1.13 新增：跳合闸、报警、保电
            MAIN_REQUEST_CTL(0x1C,8),
            SLAVE_RESPONSE_CTL(0x9C,0),
            SLAVE_RESPONSE_CTL_ERR(0xDC,1),
            //1.14 新增：多功能端子输出控制命令
            MAIN_REQUEST_CTL_CMD(0x1D,0),
            SLAVE_RESPONSE_CTL_CMD(0x9D,0),
            SLAVE_RESPONSE_CTL_CMD_ERR(0xDD,1),
            //1.15 新增：安全认证命令
            MAIN_REQUEST_AUTH(0x03,3),
            SLAVE_RESPONSE_AUTH(0x83,4),
            SLAVE_RESPONSE_AUTH_ERR(0xC3,2);

            private int value;
            private int len = 4;

            private C(int v,int l) {
                value = v;
                len = l;
            }
            private C(int v) {
                value = v;
            }
            public int getValue(){
                return value;
            }
            public int getLen(){
                return len;
            }
            public static C getC(int value){
                for(C c:C.class.getEnumConstants()){
                    if(c.value == value){
                        return c;
                    }
                }
                return null;
            }

            }

    }
}
