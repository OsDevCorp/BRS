package com.misis.brs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.misis.brs.Database.News;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

import java.util.Vector;

public class NewsViewAdapter extends BaseAdapter{

    private Context context;
    private static Vector<News> news;

    public NewsViewAdapter(Context context, Vector<News> news){
        this.context = context;
        this.news = news;
    }

    @Override
    public int getCount() {
        return news.size();
    }

    public static void setNews(Vector<News> news) {
        NewsViewAdapter.news = news;
    }

    @Override
    public Object getItem(int position) {
        return news.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_news_simple_item,null);

        ((TextView) view.findViewById(R.id.title)).setText(news.get(position).getHeader());
        ((TextView) view.findViewById(R.id.date)).setText(TimeHelper.getTime(news.get(position).getDateNews()));
        ((TextView) view.findViewById(R.id.textNews)).setText(news.get(position).getDescription());

        return view;
    }
}