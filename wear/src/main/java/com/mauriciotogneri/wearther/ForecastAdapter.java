package com.mauriciotogneri.wearther;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mauriciotogneri.common.api.Forecast;
import com.mauriciotogneri.wearther.ForecastAdapter.ForecastViewHolder;

public class ForecastAdapter extends BaseAdapter<Forecast, ForecastViewHolder>
{
    public ForecastAdapter(Context context)
    {
        super(context);
    }

    @Override
    protected synchronized void fill(Forecast forecast, ForecastViewHolder viewHolder)
    {
        viewHolder.date.setText(forecast.date());
        viewHolder.icon.setImageResource(forecast.icon());
        viewHolder.temperature.setText(forecast.temperature());
        viewHolder.description.setText(forecast.description());
    }

    @Override
    protected ForecastViewHolder getViewHolder(View view)
    {
        return new ForecastViewHolder(view);
    }

    @Override
    protected int getLayoutId()
    {
        return R.layout.row_forecast;
    }

    public static class ForecastViewHolder extends BaseViewHolder<Forecast>
    {
        public final TextView date;
        public final ImageView icon;
        public final TextView temperature;
        public final TextView description;

        public ForecastViewHolder(View itemView)
        {
            super(itemView);

            this.date = (TextView) itemView.findViewById(R.id.date);
            this.icon = (ImageView) itemView.findViewById(R.id.icon);
            this.temperature = (TextView) itemView.findViewById(R.id.temperature);
            this.description = (TextView) itemView.findViewById(R.id.description);
        }
    }
}