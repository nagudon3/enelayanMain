package com.unimas.enelayan2019.Model;

public class test {
    private String pid;
    private String productName;
    private String cid;

    public test() {
    }

    public test(String pid, String productName, String cid) {
        this.pid = pid;
        this.productName = productName;
        this.cid = cid;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPid() {
        return pid;
    }

    public String getCid() {
        return cid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }
}
