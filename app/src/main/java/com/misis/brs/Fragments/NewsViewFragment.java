package com.misis.brs.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.misis.brs.Database.News;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

public class NewsViewFragment extends Fragment {
    private News news;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        news = new News();
        news.setHeader(getArguments().getString("title",""));
        news.setDateNews(getArguments().getLong("date",0));
        news.setDescription(getArguments().getString("text",""));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_view_simple_item,container,false);
        ((TextView) view.findViewById(R.id.title)).setText(news.getHeader());
        ((TextView) view.findViewById(R.id.date)).setText(TimeHelper.getTime(news.getDateNews()));
        ((TextView) view.findViewById(R.id.text)).setText(news.getDescription());

        return view;
    }
}
