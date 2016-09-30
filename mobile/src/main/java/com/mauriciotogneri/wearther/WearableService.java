package com.mauriciotogneri.wearther;

import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;
import com.mauriciotogneri.common.api.Forecast;
import com.mauriciotogneri.common.api.Message;
import com.mauriciotogneri.common.api.MessageApi.Messages;
import com.mauriciotogneri.common.api.MessageApi.Paths;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WearableService extends WearableListenerService
{
    private static final int TIMEOUT = 1000 * 10; // in milliseconds

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

        // TODO
        result.add(new Forecast(12, 24, 60, "Sunny"));

        reply(Messages.resultWeather(nodeId, result));
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