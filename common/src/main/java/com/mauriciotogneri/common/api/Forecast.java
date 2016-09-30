package com.mauriciotogneri.common.api;

import java.io.Serializable;

public class Forecast implements Serializable
{
    public final Integer min;
    public final Integer max;
    public final Integer code;
    public final String description;

    public Forecast(Integer min, Integer max, Integer code, String description)
    {
        this.min = min;
        this.max = max;
        this.code = code;
        this.description = description;
    }
}