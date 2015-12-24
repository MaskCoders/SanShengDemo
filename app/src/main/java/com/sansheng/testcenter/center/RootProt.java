package com.sansheng.testcenter.center;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class RootProt {
    public String afn;
    int t;
    int o;
    public String n;
    public List<Protocol> mChildArray = new ArrayList<Protocol>();

    public RootProt() {
    }

    public RootProt(String afn, String n, int t, int o) {
        this.afn = afn;
        this.n = n;
        this.t = t;
        this.o = o;
    }

    public RootProt(String afn, String n, int t, int o, List<Protocol> childArray) {
        this.afn = afn;
        this.n = n;
        this.t = t;
        this.o = o;
        this.mChildArray = childArray;
    }

    public void setChildArray(List<Protocol> childArray) {
        this.mChildArray = childArray;
    }

    public void addProtocol(Protocol child) {
        mChildArray.add(child);
    }

    public int getChildCount() {
        return mChildArray.size();
    }

}
