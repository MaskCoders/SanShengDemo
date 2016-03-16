package com.sansheng.testcenter.center;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class Protocol {
    private String AFN = "afn";
    private String FN = "fn";

    public char afn;
    public String parentN;
    public int fn;
    public int t;
    public int o;
    public String n;

    public Protocol(){
        //do nothing
    }

    public Protocol(char afn, String parentDescribe, int fn, int o, int t, String describe) {
        this.afn = afn;
        this.parentN = parentDescribe;
        this.fn = fn;
        this.n = describe;
        this.o = o;
        this.t = t;
    }

    public String toString() {
        return "[" + AFN + " : " + afn + "]" + "\n" +
                "[" + FN + " : " + fn + "]" + "\n";
    }

}
