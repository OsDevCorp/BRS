package com.misis.brs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.misis.brs.R;

public class BugReportFragment extends Fragment {

    private WebView web;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bug_report,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.bug_report);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        web = (WebView) view.findViewById(R.id.web);
        web.getSettings().setJavaScriptEnabled(true);
        //для того чтобы окно отображалось в приложении, а не на стороне
        web.setWebViewClient(new WebViewClient());
        //TODO вписать нужную ссылку
        web.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSeRyA1-lUxSvjyu8nwMyqXe-MQb9L4I2bdJUu_P3tK2dQdwRQ/viewform");
        return view;
    }
}
