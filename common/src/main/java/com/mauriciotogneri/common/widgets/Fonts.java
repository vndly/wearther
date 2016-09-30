package com.mauriciotogneri.common.widgets;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import java.util.HashMap;
import java.util.Map;

public class Fonts
{
    private static final Map<String, Typeface> fonts = new HashMap<>();
    private static final String FONTS_PATH = "fonts/";

    private static final String WEATHER_ICONS = "0";

    public static void init(Context context)
    {
        Fonts.loadFont(context.getAssets(), "weathericons", Fonts.WEATHER_ICONS);
    }

    private static void loadFont(AssetManager assets, String name, String key)
    {
        Fonts.fonts.put(key, Typeface.createFromAsset(assets, Fonts.FONTS_PATH + name + ".ttf"));
    }

    public static Typeface getFont(String name)
    {
        Typeface fontFamily = Fonts.fonts.get(name);

        return Typeface.create(fontFamily, Typeface.NORMAL);
    }
}