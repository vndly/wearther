package com.mauriciotogneri.wearther;

import com.google.gson.Gson;

public class JsonUtils
{
    private static final Gson gson = new Gson();

    public static <T> T fromJson(String input, Class<T> clazz)
    {
        return gson.fromJson(input, clazz);
    }

    public static String toJson(Object object)
    {
        return gson.toJson(object);
    }
}