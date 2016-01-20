package com.sansheng.testcenter.collecttest;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class CollectTestItem {
    public String address;
    public String name;
    public String result;
    public String report;

    public CollectTestItem(String address, String name) {
        this.address = address;
        this.name = name;
    }

    public CollectTestItem(String address, String name, String result, String report) {
        this.address = address;
        this.name = name;
        this.result = result;
        this.report = report;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getResult() {
        return result;
    }

    public String getReport() {
        return report;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setReport(String report) {
        this.report = report;
    }
}
