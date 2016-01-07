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
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int SEND_MSG = 2;
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
            SLAVE_RESPONSE_READ_DATA_ERR(0xD1),

            MAIN_REQUEST_FRAME(0x12),
            SLAVE_RESPONSE_FRAME(0x92),
            SLAVE_RESPONSE_FRAME_ERR(0xD2),

            MAIN_REQUEST_WRITE(0x14),
            SLAVE_RESPONSE_WRITE(0x94),
            SLAVE_RESPONSE_WRITE_ERR(0xD4),

            MAIN_REQUEST_RED_ADDRESS(0x13),
            SLAVE_RESPONSE_RED_ADDRESS(0x93),
            // SLAVE_RESPONSE_RED_ADDRESS_ERR (0xD3),

            MAIN_REQUEST_WRITE_ADDRESS(0x15),
            SLAVE_RESPONSE_WRITE_ADDRESS(0x95),

            CALIBRATION_TIME(0x08),

            MAIN_REQUEST_FROZEN_DATA(0x16),
            SLAVE_RESPONSE_FROZEN_DATA(0x96),
            SLAVE_RESPONSE_FROZEN_DATA_ERR(0xD6),

            MAIN_REQUEST_COMMUNICATION_RATE(0x17),
            SLAVE_RESPONSE_COMMUNICATION_RATE(0x97),
            SLAVE_RESPONSE_COMMUNICATION_RATE_ERR(0xD7),

            MAIN_REQUEST_CHANGE_PW(0x18),
            SLAVE_RESPONSE_CHANGE_PW(0x98),
            SLAVE_RESPONSE_CHANGE_PW_ERR(0xD8),

            MAIN_REQUEST_ALL_CLEAR(0x19),
            SLAVE_RESPONSE_ALL_CLEAR(0x99),
            SLAVE_RESPONSE_ALL_CLEAR_ERR(0xD9),

            MAIN_REQUEST_CLEAR(0x1A),
            SLAVE_RESPONSE_CLEAR(0x9A),
            SLAVE_RESPONSE_CLEAR_ERR(0xDA),

            MAIN_REQUEST_EVENT_CLEAR(0x1B),
            SLAVE_RESPONSE_EVENT_CLEAR(0x9B),
            SLAVE_RESPONSE_EVENT_CLEAR_ERR(0xDB),
            //1.13 新增：跳合闸、报警、保电
            MAIN_REQUEST_CTL(0x1C),
            SLAVE_RESPONSE_CTL(0x9C),
            SLAVE_RESPONSE_CTL_ERR(0xDC),
            //1.14 新增：多功能端子输出控制命令
            MAIN_REQUEST_CTL_CMD(0x1D),
            SLAVE_RESPONSE_CTL_CMD(0x9D),
            SLAVE_RESPONSE_CTL_CMD_ERR(0xDD),
            //1.15 新增：安全认证命令
            MAIN_REQUEST_AUTH(0x03),
            SLAVE_RESPONSE_AUTH(0x83),
            SLAVE_RESPONSE_AUTH_ERR(0xC3);
            private int value;

            private C(int v) {
                value = v;
            }
            public int getValue(){
                return value;
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
