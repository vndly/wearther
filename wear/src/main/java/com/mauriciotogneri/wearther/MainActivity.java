package com.mauriciotogneri.wearther;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.mauriciotogneri.common.api.Forecast;
import com.mauriciotogneri.common.api.Message;
import com.mauriciotogneri.common.api.MessageApi.Messages;
import com.mauriciotogneri.common.api.MessageApi.Paths;
import com.mauriciotogneri.common.utils.Serializer;
import com.mauriciotogneri.common.widgets.Fonts;
import com.mauriciotogneri.wearther.WearableConnectivity.OnDeviceNodeDetected;
import com.mauriciotogneri.wearther.WearableConnectivity.WearableEvents;

import java.util.List;

public class MainActivity extends Activity implements WearableEvents
{
    private String nodeId = "";
    private WearableConnectivity connectivity;
    private View progressBar;
    private WearableListView list;
    private ForecastAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fonts.init(this);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener()
        {
            @Override
            public void onLayoutInflated(WatchViewStub stub)
            {
                onLoad();
            }
        });
    }

    private void onLoad()
    {
        progressBar = findViewById(R.id.progress_bar);
        list = (WearableListView) findViewById(R.id.list);

        progressBar.setVisibility(View.VISIBLE);
        list.setVisibility(View.GONE);

        adapter = new ForecastAdapter(this);

        list.setAdapter(adapter);

        connectivity = new WearableConnectivity(this, this);
        connectivity.connect();
    }

    private void display(List<Forecast> forecasts)
    {
        progressBar.setVisibility(View.GONE);
        list.setVisibility(View.VISIBLE);

        adapter.setData(forecasts);
    }

    @Override
    public void onConnectedSuccess()
    {
        connectivity.getDefaultDeviceNode(new OnDeviceNodeDetected()
        {
            @Override
            public void onDefaultDeviceNode(String deviceNodeId)
            {
                if (!TextUtils.isEmpty(deviceNodeId))
                {
                    nodeId = deviceNodeId;

                    connectivity.sendMessage(Messages.getWeather(nodeId));
                }
                else
                {
                    displayToast(getString(R.string.loading_error));
                }
            }
        });
    }

    @Override
    public void onConnectedFail()
    {
        displayToast(getString(R.string.loading_error));
    }

    private void displayToast(final String message)
    {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMessageReceived(Message message)
    {
        if (TextUtils.equals(message.getPath(), Paths.RESULT_WEATHER))
        {
            List<Forecast> list = Serializer.deserialize(message.getPayload());
            display(list);
        }
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        if (connectivity != null)
        {
            connectivity.disconnect();
        }
    }
}