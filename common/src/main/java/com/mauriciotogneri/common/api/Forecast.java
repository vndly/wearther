package com.mauriciotogneri.common.api;

import com.mauriciotogneri.common.R;

import java.io.Serializable;

public class Forecast implements Serializable
{
    private final String date;
    private final Integer min;
    private final Integer max;
    private final Integer code;
    private final String description;

    public Forecast(String date, Integer min, Integer max, Integer code, String description)
    {
        this.date = date;
        this.min = min;
        this.max = max;
        this.code = code;
        this.description = description;
    }

    public String date()
    {
        return date;
    }

    public String temperature()
    {
        return String.format("%sº/%sº", min, max);
    }

    public String description()
    {
        return description;
    }

    public int icon()
    {
        return R.drawable.sunny;
    }
}