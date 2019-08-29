package com.misis.brs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.misis.brs.R;

public class WhatsNewViewFragment extends Fragment {

    private String version;
    private String text;
    private String date;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        version = getArguments().getString("version","");
        text = getArguments().getString("text","");
        date = getArguments().getString("date","");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whatsnew_view_simple_item,container,false);

        ((TextView) view.findViewById(R.id.title)).setText(version);
        ((TextView) view.findViewById(R.id.date)).setText(date);
        ((TextView) view.findViewById(R.id.text)).setText(text);
        return view;
    }
}
