package com.sansheng.testcenter.tools;

/**
 * Created by hua on 12/28/15.
 */
public interface Const {
    public interface AFN{
        int H0 = 0;
        int H1 = 1;
        int H2 = 2;
        int H3 = 3;
        int H4 = 4;
        int H5= 5;
        int H6 = 6;
        int H7 = 7;
        int H8 = 8;
        int H9 = 9;
        int HA = 10;
        int HB = 11;
        int HC = 12;
        int HD = 13;
        int HE = 14;
        int HF = 15;
        int H10 = 16;
        //11H～FFH  备用

    }

    public interface SEQ{
        //D7
        int TPV_WITHOUT_TIME = 0;
        int TPV_WITH_TIME = 1;
        //D6
        int FI_MORE_MIDDLE = 0;
        int FI_MORE_END = 1<<1;
        //D5
        int FI_MORE_FIRST = 2<<2;
        int FI_SINGLE = 3<<2;
        //D5
        int CONFIRM  = 1<<3;
        int CONFIRM_NO  = 0;
        //D3-D0
        int PSEQ_TMP = 15<<4;
    }

    /**
     * DA 2字节
     * DT 2字节
     */
    public interface DataUnitTip {

    }
    public interface AUX{

    }
}
