package com.mauriciotogneri.wearther;

import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mauriciotogneri.common.api.Forecast;
import com.mauriciotogneri.common.api.Message;
import com.mauriciotogneri.common.api.MessageApi.Messages;
import com.mauriciotogneri.common.api.MessageApi.Paths;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WearableService extends WearableListenerService
{
    private static final int TIMEOUT = 1000 * 10; // in milliseconds

    private static final String URL = "https://query.yahooapis.com/v1/public/yql?q=select * from weather.forecast where u='c' and woeid in (select woeid from geo.places(1) where text=\"geneva, ch\")&format=json";

    @Override
    public void onMessageReceived(MessageEvent messageEvent)
    {
        String nodeId = messageEvent.getSourceNodeId();
        String path = messageEvent.getPath();

        if (TextUtils.equals(path, Paths.GET_WEATHER))
        {
            getWeather(nodeId);
        }
    }

    private void getWeather(String nodeId)
    {
        ArrayList<Forecast> result = new ArrayList<>();

        JsonObject json = null;

        while (json == null)
        {
            json = jsonWeather();
        }

        JsonObject query = json.get("query").getAsJsonObject();
        JsonObject results = query.get("results").getAsJsonObject();
        JsonObject channel = results.get("channel").getAsJsonObject();
        JsonObject item = channel.get("item").getAsJsonObject();
        JsonArray array = item.getAsJsonArray("forecast");

        DateFormat parseFormat = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        DateFormat printFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);

        for (int i = 0; i < array.size(); i++)
        {
            JsonObject element = array.get(i).getAsJsonObject();

            Integer code = element.get("code").getAsInt();
            Integer min = element.get("low").getAsInt();
            Integer max = element.get("high").getAsInt();
            String text = element.get("text").getAsString();
            String day = element.get("day").getAsString();

            try
            {
                Date date = parseFormat.parse(element.get("date").getAsString());
                day = printFormat.format(date);
            }
            catch (Exception e)
            {
            }

            result.add(new Forecast(day, min, max, code, text));
        }

        reply(Messages.resultWeather(nodeId, result));
    }

    private JsonObject jsonWeather()
    {
        try
        {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            Response response = client.newCall(request).execute();

            return new JsonParser().parse(response.body().string()).getAsJsonObject();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private void reply(final Message message)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                GoogleApiClient client = new GoogleApiClient.Builder(WearableService.this).addApi(Wearable.API).build();
                ConnectionResult connectionResult = client.blockingConnect(TIMEOUT, TimeUnit.MILLISECONDS);

                if (connectionResult.isSuccess())
                {
                    Wearable.MessageApi.sendMessage(client, message.getNodeId(), message.getPath(), message.getPayload());
                }

                client.disconnect();
            }
        }).start();
    }
}