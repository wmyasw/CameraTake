package com.wmy.main.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wmy.main.base.DataTypeAdaptor;

import java.security.PublicKey;

/**
 * Created by Administrator on 2018/4/24/024.
 */

public class GsonUtils {
    public static Gson buildGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapterFactory(DataTypeAdaptor.FACTORY);
        return gsonBuilder.create();
    }
}
