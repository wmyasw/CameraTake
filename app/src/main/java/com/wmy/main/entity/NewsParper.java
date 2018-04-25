package com.wmy.main.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/25/025.
 */

public class NewsParper implements Serializable {

    /**
     * id : 1
     * name : 人民日报
     */

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
