package com.mauriciotogneri.common.api;

import com.mauriciotogneri.common.utils.Serializer;

import java.util.ArrayList;

public class MessageApi
{
    private MessageApi()
    {
    }

    public static final class Paths
    {
        public static final String GET_WEATHER = "/get_weather";
        public static final String RESULT_WEATHER = "/result_weather";
    }

    public static final class Messages
    {
        public static Message getWeather(String nodeId)
        {
            return new Message(nodeId, Paths.GET_WEATHER);
        }

        public static Message resultWeather(String nodeId, ArrayList<Forecast> elements)
        {
            return new Message(nodeId, Paths.RESULT_WEATHER, Serializer.serialize(elements));
        }
    }
}