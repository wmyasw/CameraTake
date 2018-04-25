package com.wmy.main.entity;


import java.io.Serializable;
import java.util.List;


public class ResultEntity<T> implements Serializable {
    private Long errcode;
    private String errmsg;
    private List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getErrcode() {
        return errcode;
    }

    public void setErrcode(Long errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    @Override
    public String toString() {
        return "{" +
                "errcode=" + errcode +
                ", errmsg='" + errmsg + '\'' +
                ", data=" + data +
                '}';
    }
}