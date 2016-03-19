package com.sansheng.testcenter.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import android_serialport_api.SerialPortFinder;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.bean.AssistBean;
import com.sansheng.testcenter.bean.BeanMark;
import com.sansheng.testcenter.bean.ComBean;
import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.tools.serial.MyFunc;
import com.sansheng.testcenter.tools.serial.SerialHelper;

import java.io.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * serialport api和jni取自http://code.google.com/p/android-serialport-api/
 * @author benjaminwan
 * 串口助手，支持4串口同时读写
 * 程序载入时自动搜索串口设备
 * n,8,1，没得选
 */

public class ComAssistantActivity extends BaseActivity {
	EditText editTextRecDisp,editTextLines,editTextCOMA,editTextCOMB,editTextCOMC,editTextCOMD;
	EditText editTextTimeCOMA,editTextTimeCOMB,editTextTimeCOMC,editTextTimeCOMD;
	CheckBox checkBoxAutoClear,checkBoxAutoCOMA,checkBoxAutoCOMB,checkBoxAutoCOMC,checkBoxAutoCOMD;
	Button ButtonClear,ButtonSendCOMA,ButtonSendCOMB,ButtonSendCOMC,ButtonSendCOMD;
	ToggleButton toggleButtonCOMA,toggleButtonCOMB,toggleButtonCOMC,toggleButtonCOMD;
	Spinner SpinnerCOMA,SpinnerCOMB,SpinnerCOMC,SpinnerCOMD;
	Spinner SpinnerBaudRateCOMA,SpinnerBaudRateCOMB,SpinnerBaudRateCOMC,SpinnerBaudRateCOMD;
	RadioButton radioButtonTxt,radioButtonHex;
	SerialControl ComA,ComB,ComC,ComD;//4个串口
	DispQueueThread DispQueue;//刷新显示线程
	SerialPortFinder mSerialPortFinder;//串口设备搜索
	AssistBean AssistData;//用于界面数据序列化和反序列化
	int iRecLines=0;//接收区行数

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serial_test_layout);
        ComA = new SerialControl();
        ComB = new SerialControl();
        ComC = new SerialControl();
        ComD = new SerialControl();
        DispQueue = new DispQueueThread();
        DispQueue.start();
        AssistData = getAssistData();
        setControls();
    }

	@Override
	protected void initButtonList() {

	}

	@Override
	protected void initConnList() {

	}

	@Override
	protected void initCenter() {

	}

	@Override
    public void onDestroy(){
    	saveAssistData(AssistData);
    	CloseComPort(ComA);
    	CloseComPort(ComB);
    	CloseComPort(ComC);
    	CloseComPort(ComD);
    	super.onDestroy();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      CloseComPort(ComA);
	  CloseComPort(ComB);
	  CloseComPort(ComC);
	  CloseComPort(ComD);
      setContentView(R.layout.serial_test_layout);
      setControls();
    }
    
    //----------------------------------------------------
    private void setControls()
	{
    	String appName = getString(R.string.app_name);
        try {
			PackageInfo pinfo = getPackageManager().getPackageInfo("com.bjw.ComAssistant", PackageManager.GET_CONFIGURATIONS);
			String versionName = pinfo.versionName;
//			String versionCode = String.valueOf(pinfo.versionCode);
			setTitle(appName+" V"+versionName);
        } catch (NameNotFoundException e) {
        	e.printStackTrace();
        }
    	editTextRecDisp=(EditText)findViewById(R.id.editTextRecDisp);
    	editTextLines=(EditText)findViewById(R.id.editTextLines);
    	editTextCOMA=(EditText)findViewById(R.id.editTextCOMA);
    	editTextCOMB=(EditText)findViewById(R.id.editTextCOMB);
    	editTextCOMC=(EditText)findViewById(R.id.editTextCOMC);
    	editTextCOMD=(EditText)findViewById(R.id.editTextCOMD);
    	editTextTimeCOMA = (EditText)findViewById(R.id.editTextTimeCOMA);
		editTextTimeCOMB= (EditText)findViewById(R.id.editTextTimeCOMB);
		editTextTimeCOMC= (EditText)findViewById(R.id.editTextTimeCOMC);
		editTextTimeCOMD= (EditText)findViewById(R.id.editTextTimeCOMD);
    	
    	checkBoxAutoClear=(CheckBox)findViewById(R.id.checkBoxAutoClear);
		checkBoxAutoCOMA=(CheckBox)findViewById(R.id.checkBoxAutoCOMA);
		checkBoxAutoCOMB=(CheckBox)findViewById(R.id.checkBoxAutoCOMB);
		checkBoxAutoCOMC=(CheckBox)findViewById(R.id.checkBoxAutoCOMC);
		checkBoxAutoCOMD=(CheckBox)findViewById(R.id.checkBoxAutoCOMD);
    	ButtonClear=(Button)findViewById(R.id.ButtonClear);
    	ButtonSendCOMA=(Button)findViewById(R.id.ButtonSendCOMA);
    	ButtonSendCOMB=(Button)findViewById(R.id.ButtonSendCOMB);
    	ButtonSendCOMC=(Button)findViewById(R.id.ButtonSendCOMC);
    	ButtonSendCOMD=(Button)findViewById(R.id.ButtonSendCOMD);
    	toggleButtonCOMA=(ToggleButton)findViewById(R.id.toggleButtonCOMA);
    	toggleButtonCOMB=(ToggleButton)findViewById(R.id.ToggleButtonCOMB);
    	toggleButtonCOMC=(ToggleButton)findViewById(R.id.ToggleButtonCOMC);
    	toggleButtonCOMD=(ToggleButton)findViewById(R.id.ToggleButtonCOMD);
    	SpinnerCOMA=(Spinner)findViewById(R.id.SpinnerCOMA);
    	SpinnerCOMB=(Spinner)findViewById(R.id.SpinnerCOMB);
    	SpinnerCOMC=(Spinner)findViewById(R.id.SpinnerCOMC);
    	SpinnerCOMD=(Spinner)findViewById(R.id.SpinnerCOMD);
    	SpinnerBaudRateCOMA=(Spinner)findViewById(R.id.SpinnerBaudRateCOMA);
    	SpinnerBaudRateCOMB=(Spinner)findViewById(R.id.SpinnerBaudRateCOMB);
    	SpinnerBaudRateCOMC=(Spinner)findViewById(R.id.SpinnerBaudRateCOMC);
    	SpinnerBaudRateCOMD=(Spinner)findViewById(R.id.SpinnerBaudRateCOMD);
    	radioButtonTxt=(RadioButton)findViewById(R.id.radioButtonTxt);
    	radioButtonHex=(RadioButton)findViewById(R.id.radioButtonHex);
    	
    	editTextCOMA.setOnEditorActionListener(new EditorActionEvent());
		editTextCOMB.setOnEditorActionListener(new EditorActionEvent());
		editTextCOMC.setOnEditorActionListener(new EditorActionEvent());
		editTextCOMD.setOnEditorActionListener(new EditorActionEvent());
		editTextTimeCOMA.setOnEditorActionListener(new EditorActionEvent());
		editTextTimeCOMB.setOnEditorActionListener(new EditorActionEvent());
		editTextTimeCOMC.setOnEditorActionListener(new EditorActionEvent());
		editTextTimeCOMD.setOnEditorActionListener(new EditorActionEvent());
		editTextCOMA.setOnFocusChangeListener(new FocusChangeEvent());
		editTextCOMB.setOnFocusChangeListener(new FocusChangeEvent());
		editTextCOMC.setOnFocusChangeListener(new FocusChangeEvent());
		editTextCOMD.setOnFocusChangeListener(new FocusChangeEvent());
		editTextTimeCOMA.setOnFocusChangeListener(new FocusChangeEvent());
		editTextTimeCOMB.setOnFocusChangeListener(new FocusChangeEvent());
		editTextTimeCOMC.setOnFocusChangeListener(new FocusChangeEvent());
		editTextTimeCOMD.setOnFocusChangeListener(new FocusChangeEvent());

    	radioButtonTxt.setOnClickListener(new radioButtonClickEvent());
    	radioButtonHex.setOnClickListener(new radioButtonClickEvent());
    	ButtonClear.setOnClickListener(new ButtonClickEvent());
    	ButtonSendCOMA.setOnClickListener(new ButtonClickEvent());
    	ButtonSendCOMB.setOnClickListener(new ButtonClickEvent());
    	ButtonSendCOMC.setOnClickListener(new ButtonClickEvent());
    	ButtonSendCOMD.setOnClickListener(new ButtonClickEvent());
    	toggleButtonCOMA.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
    	toggleButtonCOMB.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
    	toggleButtonCOMC.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
    	toggleButtonCOMD.setOnCheckedChangeListener(new ToggleButtonCheckedChangeEvent());
    	checkBoxAutoCOMA.setOnCheckedChangeListener(new CheckBoxChangeEvent());
    	checkBoxAutoCOMB.setOnCheckedChangeListener(new CheckBoxChangeEvent());
    	checkBoxAutoCOMC.setOnCheckedChangeListener(new CheckBoxChangeEvent());
    	checkBoxAutoCOMD.setOnCheckedChangeListener(new CheckBoxChangeEvent());
    	
    	ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, 
    			R.array.baudrates_value,android.R.layout.simple_spinner_item);
    	adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    	SpinnerBaudRateCOMA.setAdapter(adapter);
    	SpinnerBaudRateCOMB.setAdapter(adapter);
    	SpinnerBaudRateCOMC.setAdapter(adapter);
    	SpinnerBaudRateCOMD.setAdapter(adapter);
    	SpinnerBaudRateCOMA.setSelection(8);
    	SpinnerBaudRateCOMB.setSelection(8);
    	SpinnerBaudRateCOMC.setSelection(8);
    	SpinnerBaudRateCOMD.setSelection(8);
    	
    	mSerialPortFinder= new SerialPortFinder();
    	String[] entryValues = mSerialPortFinder.getAllDevicesPath();
    	List<String> allDevices = new ArrayList<String>();
		for (int i = 0; i < entryValues.length; i++) {
			allDevices.add(entryValues[i]);
		}
		ArrayAdapter<String> aspnDevices = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, allDevices);
		aspnDevices.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpinnerCOMA.setAdapter(aspnDevices);
		SpinnerCOMB.setAdapter(aspnDevices);
		SpinnerCOMC.setAdapter(aspnDevices);
		SpinnerCOMD.setAdapter(aspnDevices);
		if (allDevices.size()>0)
		{
			SpinnerCOMA.setSelection(0);
		}
		if (allDevices.size()>1)
		{
			SpinnerCOMB.setSelection(1);
		}
		if (allDevices.size()>2)
		{
			SpinnerCOMC.setSelection(2);
		}
		if (allDevices.size()>3)
		{
			SpinnerCOMD.setSelection(3);
		}
		SpinnerCOMA.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerCOMB.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerCOMC.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerCOMD.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerBaudRateCOMA.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerBaudRateCOMB.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerBaudRateCOMC.setOnItemSelectedListener(new ItemSelectedEvent());
		SpinnerBaudRateCOMD.setOnItemSelectedListener(new ItemSelectedEvent());
		DispAssistData(AssistData);
	}

	@Override
	public void setValue(BeanMark bean) {

	}

	//----------------------------------------------------串口号或波特率变化时，关闭打开的串口
    class ItemSelectedEvent implements Spinner.OnItemSelectedListener{
		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			if ((arg0 == SpinnerCOMA) || (arg0 == SpinnerBaudRateCOMA))
			{
				CloseComPort(ComA);
				checkBoxAutoCOMA.setChecked(false);
				toggleButtonCOMA.setChecked(false);
			}else if ((arg0 == SpinnerCOMB) || (arg0 == SpinnerBaudRateCOMB))
			{
				CloseComPort(ComB);
				checkBoxAutoCOMA.setChecked(false);
				toggleButtonCOMB.setChecked(false);
			}else if ((arg0 == SpinnerCOMC) || (arg0 == SpinnerBaudRateCOMC))
			{
				CloseComPort(ComC);
				checkBoxAutoCOMA.setChecked(false);
				toggleButtonCOMC.setChecked(false);
			}else if ((arg0 == SpinnerCOMD) || (arg0 == SpinnerBaudRateCOMD))
			{
				CloseComPort(ComD);
				checkBoxAutoCOMA.setChecked(false);
				toggleButtonCOMD.setChecked(false);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0)
		{}
    	
    }
	//----------------------------------------------------编辑框焦点转移事件
    class FocusChangeEvent implements EditText.OnFocusChangeListener{
		public void onFocusChange(View v, boolean hasFocus)
		{
			if (v==editTextCOMA)
			{
				setSendData(editTextCOMA);
			} else if (v==editTextCOMB)
			{
				setSendData(editTextCOMB);
			} else if (v==editTextCOMC)
			{
				setSendData(editTextCOMC);
			} else if (v==editTextCOMD)
			{
				setSendData(editTextCOMD);
			}else if (v==editTextTimeCOMA)
			{
				setDelayTime(editTextTimeCOMA);
			}else if (v==editTextTimeCOMB)
			{
				setDelayTime(editTextTimeCOMB);
			}else if (v==editTextTimeCOMC)
			{
				setDelayTime(editTextTimeCOMC);
			}else if (v==editTextTimeCOMD)
			{
				setDelayTime(editTextTimeCOMD);
			}
		}
    }
	//----------------------------------------------------编辑框完成事件
    class EditorActionEvent implements EditText.OnEditorActionListener{
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
		{
			if (v==editTextCOMA)
			{
				setSendData(editTextCOMA);
			} else if (v==editTextCOMB)
			{
				setSendData(editTextCOMB);
			} else if (v==editTextCOMC)
			{
				setSendData(editTextCOMC);
			} else if (v==editTextCOMD)
			{
				setSendData(editTextCOMD);
			}else if (v==editTextTimeCOMA)
			{
				setDelayTime(editTextTimeCOMA);
			}else if (v==editTextTimeCOMB)
			{
				setDelayTime(editTextTimeCOMB);
			}else if (v==editTextTimeCOMC)
			{
				setDelayTime(editTextTimeCOMC);
			}else if (v==editTextTimeCOMD)
			{
				setDelayTime(editTextTimeCOMD);
			}
			return false;
		}
    }
	//----------------------------------------------------Txt、Hex模式选择
    class radioButtonClickEvent implements RadioButton.OnClickListener{
		public void onClick(View v)
		{
			if (v==radioButtonTxt)
			{
				KeyListener TxtkeyListener = new TextKeyListener(Capitalize.NONE, false);
				editTextCOMA.setKeyListener(TxtkeyListener);
				editTextCOMB.setKeyListener(TxtkeyListener);
				editTextCOMC.setKeyListener(TxtkeyListener);
				editTextCOMD.setKeyListener(TxtkeyListener);
				AssistData.setTxtMode(true);
			}else if (v==radioButtonHex) {
				KeyListener HexkeyListener = new NumberKeyListener()
				{
					public int getInputType()
					{
						return InputType.TYPE_CLASS_TEXT;
					}
					@Override
					protected char[] getAcceptedChars()
					{
						return new char[]{'0','1','2','3','4','5','6','7','8','9',
								'a','b','c','d','e','f','A','B','C','D','E','F'};
					}
				};
				editTextCOMA.setKeyListener(HexkeyListener);
				editTextCOMB.setKeyListener(HexkeyListener);
				editTextCOMC.setKeyListener(HexkeyListener);
				editTextCOMD.setKeyListener(HexkeyListener);
				AssistData.setTxtMode(false);
			}
			editTextCOMA.setText(AssistData.getSendA());
			editTextCOMB.setText(AssistData.getSendB());
			editTextCOMC.setText(AssistData.getSendC());
			editTextCOMD.setText(AssistData.getSendD());
			setSendData(editTextCOMA);
	    	setSendData(editTextCOMB);
	    	setSendData(editTextCOMC);
	    	setSendData(editTextCOMD);
		}
    }
    //----------------------------------------------------�Զ�����
    class CheckBoxChangeEvent implements CheckBox.OnCheckedChangeListener{
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if (buttonView == checkBoxAutoCOMA){
				if (!toggleButtonCOMA.isChecked() && isChecked)
				{
					buttonView.setChecked(false);
					return;
				}
				SetLoopData(ComA,editTextCOMA.getText().toString());
				SetAutoSend(ComA,isChecked);
			} else if(buttonView == checkBoxAutoCOMB){
				if (!toggleButtonCOMB.isChecked() && isChecked)
				{
					buttonView.setChecked(false);
					return;
				}
				SetLoopData(ComB,editTextCOMB.getText().toString());
				SetAutoSend(ComB,isChecked);
			} else if(buttonView == checkBoxAutoCOMC){
				if (!toggleButtonCOMC.isChecked() && isChecked)
				{
					buttonView.setChecked(false);
					return;
				}
				SetLoopData(ComC,editTextCOMC.getText().toString());
				SetAutoSend(ComC,isChecked);
			} else if(buttonView == checkBoxAutoCOMD){
				if (!toggleButtonCOMD.isChecked() && isChecked)
				{
					buttonView.setChecked(false);
					return;
				}
				SetLoopData(ComD,editTextCOMD.getText().toString());
				SetAutoSend(ComD,isChecked);
			}
		}
    }
    //----------------------------------------------------���ť�����Ͱ�ť
    class ButtonClickEvent implements View.OnClickListener {
		public void onClick(View v)
		{
			if (v == ButtonClear){
				editTextRecDisp.setText("");
			} else if (v== ButtonSendCOMA){
				sendPortData(ComA, editTextCOMA.getText().toString());
			}else if (v== ButtonSendCOMB){
				sendPortData(ComB, editTextCOMB.getText().toString());
			}else if (v== ButtonSendCOMC){
				sendPortData(ComC, editTextCOMC.getText().toString());
			}else if (v== ButtonSendCOMD){
				sendPortData(ComD, editTextCOMD.getText().toString());
			}		
		}
    }
    //----------------------------------------------------�򿪹رմ���
    class ToggleButtonCheckedChangeEvent implements ToggleButton.OnCheckedChangeListener{
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
		{
			if (buttonView == toggleButtonCOMA){
				if (isChecked){
					if (toggleButtonCOMB.isChecked() && SpinnerCOMA.getSelectedItemPosition()==SpinnerCOMB.getSelectedItemPosition())
					{
						ShowMessage("串口"+SpinnerCOMA.getSelectedItem().toString()+"已打开");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMC.isChecked() && SpinnerCOMA.getSelectedItemPosition()==SpinnerCOMC.getSelectedItemPosition()) {
						ShowMessage("串口"+SpinnerCOMA.getSelectedItem().toString()+"已打开");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMD.isChecked() && SpinnerCOMA.getSelectedItemPosition()==SpinnerCOMD.getSelectedItemPosition()) {
						ShowMessage("串口"+SpinnerCOMA.getSelectedItem().toString()+"已打开");
						buttonView.setChecked(false);
					}else {
//						ComA=new SerialControl("/dev/s3c2410_serial0", "9600");
						ComA.setPort(SpinnerCOMA.getSelectedItem().toString());
						ComA.setBaudRate(SpinnerBaudRateCOMA.getSelectedItem().toString());
						OpenComPort(ComA);
					}
				}else {
					CloseComPort(ComA);
					checkBoxAutoCOMA.setChecked(false);
				}
			} else if (buttonView == toggleButtonCOMB){
				if (isChecked){
					if (toggleButtonCOMA.isChecked() && SpinnerCOMB.getSelectedItemPosition()==SpinnerCOMA.getSelectedItemPosition())
					{
						ShowMessage("����"+SpinnerCOMB.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMC.isChecked() && SpinnerCOMB.getSelectedItemPosition()==SpinnerCOMC.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMB.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMD.isChecked() && SpinnerCOMB.getSelectedItemPosition()==SpinnerCOMD.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMB.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else {
//						ComB=new SerialControl("/dev/s3c2410_serial1", "9600");
						ComB.setPort(SpinnerCOMB.getSelectedItem().toString());
						ComB.setBaudRate(SpinnerBaudRateCOMB.getSelectedItem().toString());
						OpenComPort(ComB);
					}
				}else {
					CloseComPort(ComB);
					checkBoxAutoCOMB.setChecked(false);
				}
			}else if (buttonView == toggleButtonCOMC){
				if (isChecked){
					if (toggleButtonCOMA.isChecked() && SpinnerCOMC.getSelectedItemPosition()==SpinnerCOMA.getSelectedItemPosition())
					{
						ShowMessage("����"+SpinnerCOMC.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMB.isChecked() && SpinnerCOMC.getSelectedItemPosition()==SpinnerCOMB.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMC.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMD.isChecked() && SpinnerCOMC.getSelectedItemPosition()==SpinnerCOMD.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMC.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else {
	//					ComC=new SerialControl("/dev/s3c2410_serial2", "9600");
						ComC.setPort(SpinnerCOMC.getSelectedItem().toString());
						ComC.setBaudRate(SpinnerBaudRateCOMC.getSelectedItem().toString());
						OpenComPort(ComC);
					}
				}else {
					CloseComPort(ComC);
					checkBoxAutoCOMC.setChecked(false);
				}
			}else if (buttonView == toggleButtonCOMD){
				if (isChecked){
					if (toggleButtonCOMA.isChecked() && SpinnerCOMD.getSelectedItemPosition()==SpinnerCOMA.getSelectedItemPosition())
					{
						ShowMessage("����"+SpinnerCOMD.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMB.isChecked() && SpinnerCOMD.getSelectedItemPosition()==SpinnerCOMB.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMD.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else if (toggleButtonCOMC.isChecked() && SpinnerCOMD.getSelectedItemPosition()==SpinnerCOMC.getSelectedItemPosition()) {
						ShowMessage("����"+SpinnerCOMD.getSelectedItem().toString()+"�Ѵ�");
						buttonView.setChecked(false);
					}else {
	//					ComD=new SerialControl("/dev/s3c2410_serial3", "9600");
						ComD.setPort(SpinnerCOMD.getSelectedItem().toString());
						ComD.setBaudRate(SpinnerBaudRateCOMD.getSelectedItem().toString());
						OpenComPort(ComD);
					}
				}else {
					CloseComPort(ComD);
					checkBoxAutoCOMD.setChecked(false);
				}
			}
		}
    }
    //----------------------------------------------------���ڿ�����
    public   class SerialControl extends SerialHelper{

//		public SerialControl(String sPort, String sBaudRate){
//			super(sPort, sBaudRate);
//		}
		public SerialControl(){
		}

		@Override
		public void onDataReceived(final ComBean ComRecData)
		{
			//Êý¾Ý½ÓÊÕÁ¿´ó»ò½ÓÊÕÊ±µ¯³öÈí¼üÅÌ£¬½çÃæ»á¿¨¶Ù,¿ÉÄÜºÍ6410µÄÏÔÊ¾ÐÔÄÜÓÐ¹Ø
			//Ö±½ÓË¢ÐÂÏÔÊ¾£¬½ÓÊÕÊý¾ÝÁ¿´óÊ±£¬¿¨¶ÙÃ÷ÏÔ£¬µ«½ÓÊÕÓëÏÔÊ¾Í¬²½¡£
			//ÓÃÏß³Ì¶¨Ê±Ë¢ÐÂÏÔÊ¾¿ÉÒÔ»ñµÃ½ÏÁ÷³©µÄÏÔÊ¾Ð§¹û£¬µ«ÊÇ½ÓÊÕÊý¾ÝËÙ¶È¿ìÓÚÏÔÊ¾ËÙ¶ÈÊ±£¬ÏÔÊ¾»áÖÍºó¡£
			//×îÖÕÐ§¹û²î²»¶à-_-£¬Ïß³Ì¶¨Ê±Ë¢ÐÂÉÔºÃÒ»Ð©¡£
			DispQueue.AddQueue(ComRecData);//Ïß³Ì¶¨Ê±Ë¢ÐÂÏÔÊ¾(ÍÆ¼ö)
			/*
			runOnUiThread(new Runnable()//Ö±½ÓË¢ÐÂÏÔÊ¾
			{
				public void run()
				{
					DispRecData(ComRecData);
				}
			});*/
		}

		@Override
		public String getConnInfo() {
			return "";
		}
	}
    //----------------------------------------------------ˢ����ʾ�߳�
    private class DispQueueThread extends Thread{
		private Queue<ComBean> QueueList = new LinkedList<ComBean>(); 
		@Override
		public void run() {
			super.run();
			while(!isInterrupted()) {
				final ComBean ComData;
		        while((ComData=QueueList.poll())!=null)
		        {
		        	runOnUiThread(new Runnable()
					{
						public void run()
						{
							DispRecData(ComData);
						}
					});
		        	try
					{
		        		Thread.sleep(100);//��ʾ���ܸߵĻ������԰Ѵ���ֵ��С��
					} catch (Exception e)
					{
						e.printStackTrace();
					}
		        	break;
				}
			}
		}

		public synchronized void AddQueue(ComBean ComData){
			QueueList.add(ComData);
		}
	}
	//----------------------------------------------------刷新界面数据

	private void DispAssistData(AssistBean AssistData)
	{
    	editTextCOMA.setText(AssistData.getSendA());
    	editTextCOMB.setText(AssistData.getSendB());
    	editTextCOMC.setText(AssistData.getSendC());
    	editTextCOMD.setText(AssistData.getSendD());
    	setSendData(editTextCOMA);
    	setSendData(editTextCOMB);
    	setSendData(editTextCOMC);
    	setSendData(editTextCOMD);
    	if (AssistData.isTxt())
		{
			radioButtonTxt.setChecked(true);
		} else
		{
			radioButtonHex.setChecked(true);
		}
    	editTextTimeCOMA.setText(AssistData.sTimeA);
    	editTextTimeCOMB.setText(AssistData.sTimeB);
    	editTextTimeCOMC.setText(AssistData.sTimeC);
    	editTextTimeCOMD.setText(AssistData.sTimeD);
    	setDelayTime(editTextTimeCOMA);
    	setDelayTime(editTextTimeCOMB);
    	setDelayTime(editTextTimeCOMC);
    	setDelayTime(editTextTimeCOMD);
	}
	//----------------------------------------------------保存、获取界面数据

	private void saveAssistData(AssistBean AssistData) {
    	AssistData.sTimeA = editTextTimeCOMA.getText().toString();
    	AssistData.sTimeB = editTextTimeCOMB.getText().toString();
    	AssistData.sTimeC = editTextTimeCOMC.getText().toString();
    	AssistData.sTimeD = editTextTimeCOMD.getText().toString();
    	SharedPreferences msharedPreferences = getSharedPreferences("ComAssistant", Context.MODE_PRIVATE);
        try {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            ObjectOutputStream oos = new ObjectOutputStream(baos);  
            oos.writeObject(AssistData); 
            String sBase64 = new String(Base64.encode(baos.toByteArray(),0)); 
            SharedPreferences.Editor editor = msharedPreferences.edit();  
            editor.putString("AssistData", sBase64);  
            editor.commit();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
    //----------------------------------------------------
    private AssistBean getAssistData() {  
    	SharedPreferences msharedPreferences = getSharedPreferences("ComAssistant", Context.MODE_PRIVATE);
    	AssistBean AssistData =	new AssistBean();
        try {  
            String personBase64 = msharedPreferences.getString("AssistData", "");  
            byte[] base64Bytes = Base64.decode(personBase64.getBytes(),0);  
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);  
            ObjectInputStream ois = new ObjectInputStream(bais);  
            AssistData = (AssistBean) ois.readObject();
            return AssistData;
        } catch (Exception e) {  
            e.printStackTrace();  
        }
		return AssistData;  
    }  
    //----------------------------------------------------�����Զ�������ʱ
    private void setDelayTime(TextView v){
    	if (v==editTextTimeCOMA)
		{
			AssistData.sTimeA = v.getText().toString();
			SetiDelayTime(ComA, v.getText().toString());
		}else if (v==editTextTimeCOMB)
		{
			AssistData.sTimeB = v.getText().toString();
			SetiDelayTime(ComB, v.getText().toString());
		}else if (v==editTextTimeCOMC)
		{
			AssistData.sTimeC = v.getText().toString();
			SetiDelayTime(ComC, v.getText().toString());
		}else if (v==editTextTimeCOMD)
		{
			AssistData.sTimeD = v.getText().toString();
			SetiDelayTime(ComD, v.getText().toString());
		}
    }
    //----------------------------------------------------�����Զ��������
    private void setSendData(TextView v){
    	if (v==editTextCOMA)
		{
			AssistData.setSendA(v.getText().toString());
			SetLoopData(ComA, v.getText().toString());
		} else if (v==editTextCOMB)
		{
			AssistData.setSendB(v.getText().toString());
			SetLoopData(ComB, v.getText().toString());
		} else if (v==editTextCOMC)
		{
			AssistData.setSendC(v.getText().toString());
			SetLoopData(ComC, v.getText().toString());
		} else if (v==editTextCOMD)
		{
			AssistData.setSendD(v.getText().toString());
			SetLoopData(ComD, v.getText().toString());
		}
    }
    //----------------------------------------------------�����Զ�������ʱ
    private void SetiDelayTime(SerialHelper ComPort,String sTime){
    	ComPort.setiDelay(Integer.parseInt(sTime));
    }
    //----------------------------------------------------�����Զ��������
    private void SetLoopData(SerialHelper ComPort,String sLoopData){
    	if (radioButtonTxt.isChecked())
		{
			ComPort.setTxtLoopData(sLoopData);
		} else if (radioButtonHex.isChecked())
		{
			ComPort.setHexLoopData(sLoopData);
		}
    }
    //----------------------------------------------------��ʾ�������
    private void DispRecData(ComBean ComRecData){
    	StringBuilder sMsg=new StringBuilder();
    	sMsg.append(ComRecData.sRecTime);
    	sMsg.append("[");
    	sMsg.append(ComRecData.sComPort);
    	sMsg.append("]");
    	if (radioButtonTxt.isChecked())
		{
			sMsg.append("[Txt] ");
			sMsg.append(new String(ComRecData.bRec));
		}else if (radioButtonHex.isChecked()) {
			sMsg.append("[Hex] ");
			sMsg.append(MyFunc.ByteArrToHex(ComRecData.bRec));
		}
    	sMsg.append("\r\n");
    	editTextRecDisp.append(sMsg);
    	iRecLines++;
    	editTextLines.setText(String.valueOf(iRecLines));
    	if ((iRecLines > 500) && (checkBoxAutoClear.isChecked()))//�ﵽ500���Զ����
		{
    		editTextRecDisp.setText("");
    		editTextLines.setText("0");
    		iRecLines=0;
		}
    }
    //----------------------------------------------------�����Զ�����ģʽ����
    private void SetAutoSend(SerialHelper ComPort, boolean isAutoSend){
    	if (isAutoSend)
		{
    		ComPort.startSend();
		} else
		{
			ComPort.stopSend();
		}
    }
    //----------------------------------------------------���ڷ���
    private void sendPortData(SerialHelper ComPort,String sOut){
    	if (ComPort!=null && ComPort.isOpen())
		{
    		if (radioButtonTxt.isChecked())
			{
				ComPort.sendMessage(sOut,null);
			}else if (radioButtonHex.isChecked()) {
				ComPort.sendHex(sOut);
			}
		}else {
			Toast.makeText(this,"ComPort is closed",1).show();
		}
    }
    //----------------------------------------------------�رմ���
    private void CloseComPort(SerialHelper ComPort){
    	if (ComPort!=null){
    		ComPort.stopSend();
    		ComPort.close();
		}
    }
    //----------------------------------------------------�򿪴���
    private void OpenComPort(SerialHelper ComPort){
		try
		{
			ComPort.open();
		} catch (SecurityException e) {
			ShowMessage("打开串口失败:没有串口读/写权限!");
		} catch (IOException e) {
			ShowMessage("打开串口失败:未知错误!");
		} catch (InvalidParameterException e) {
			ShowMessage("打开串口失败:参数错误!");
		}

	}
    //------------------------------------------��ʾ��Ϣ
  	private void ShowMessage(String sMsg)
  	{
  		Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
  	}
}