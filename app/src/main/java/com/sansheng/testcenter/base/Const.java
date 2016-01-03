package com.sansheng.testcenter.base;

/**
 * Created by hua on 12/18/15.
 */
public  interface Const {
    static final int PORT = 8001;
    static final String ERRCODE = "ERRCODE";
    static final String CONN_SUCCESS = "CONN_SUCCESS";
    static final String HOST = "127.0.0.1";

    static final int CONN_SER_CLS = -2;
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int INPUT_ERR = -3;

    interface WhmConst{
        interface C{
            String MAIN_REQUEST_READ_DATA = "11";
            String SLAVE_RESPONSE_READ_DATA = "91";
            //int READ_DATA = 4;
            String SLAVE_RESPONSE_READ_DATA_ERR = "D1";

            String MAIN_REQUEST_FRAME = "12";
            String SLAVE_RESPONSE_FRAME = "92";
            String SLAVE_RESPONSE_FRAME_ERR = "D2";

            String MAIN_REQUEST_WRITE = "14";
            String SLAVE_RESPONSE_WRITE = "94";
            String SLAVE_RESPONSE_WRITE_ERR = "D4";

            String MAIN_REQUEST_RED_ADDRESS = "13";
            String SLAVE_RESPONSE_RED_ADDRESS = "93";
            //String SLAVE_RESPONSE_RED_ADDRESS_ERR = "D3";

            String MAIN_REQUEST_WRITE_ADDRESS = "15";
            String SLAVE_RESPONSE_WRITE_ADDRESS = "95";

            String CALIBRATION_TIME = "08";

            String MAIN_REQUEST_FROZEN_DATA = "16";
            String SLAVE_RESPONSE_FROZEN_DATA = "96";
            String SLAVE_RESPONSE_FROZEN_DATA_ERR = "D6";

            String MAIN_REQUEST_COMMUNICATION_RATE= "17";
            String SLAVE_RESPONSE_COMMUNICATION_RATE = "97";
            String SLAVE_RESPONSE_COMMUNICATION_RATE_ERR = "D7";

            String MAIN_REQUEST_CHANGE_PW= "18";
            String SLAVE_RESPONSE_CHANGE_PW = "98";
            String SLAVE_RESPONSE_CHANGE_PW_ERR = "D8";

            String MAIN_REQUEST_ALL_CLEAR= "19";
            String SLAVE_RESPONSE_ALL_CLEAR = "99";
            String SLAVE_RESPONSE_ALL_CLEAR_ERR = "D9";

            String MAIN_REQUEST_CLEAR= "1A";
            String SLAVE_RESPONSE_CLEAR = "9A";
            String SLAVE_RESPONSE_CLEAR_ERR = "DA";

            String MAIN_REQUEST_EVENT_CLEAR= "1B";
            String SLAVE_RESPONSE_EVENT_CLEAR = "9B";
            String SLAVE_RESPONSE_EVENT_CLEAR_ERR    = "DB";
            //1.13 新增：跳合闸、报警、保电
            String MAIN_REQUEST_CTL= "1C";
            String SLAVE_RESPONSE_CTL = "9C";
            String SLAVE_RESPONSE_CTL_ERR = "DC";
            //1.14 新增：多功能端子输出控制命令
            String MAIN_REQUEST_CTL_CMD= "1D";
            String SLAVE_RESPONSE_CTL_CMD = "9D";
            String SLAVE_RESPONSE_CTL_CMD_ERR = "DD";
            //1.15 新增：安全认证命令
            String MAIN_REQUEST_AUTH= "03";
            String SLAVE_RESPONSE_AUTH = "83";
            String SLAVE_RESPONSE_AUTH_ERR = "C3";

        }
    }
}
