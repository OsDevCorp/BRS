package com.misis.brs.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.misis.brs.Adapters.WhatsNewViewAdapter;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

public class WhatsNewFragment extends Fragment {

    private String[] dates;
    private String[] versions;
    private String[] updateNews;
    private WhatsNewViewAdapter viewAdapter;
    private ListView upNewsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_whatsnew,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.side_menu_updates);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        dates = getResources().getStringArray(R.array.date);
        versions = getResources().getStringArray(R.array.versions);
        updateNews = getResources().getStringArray(R.array.update_news);

        viewAdapter = new WhatsNewViewAdapter(dates,versions,updateNews,getActivity());
        upNewsList = (ListView) view.findViewById(R.id.whatsnew_list);

        upNewsList.setAdapter(viewAdapter);
        upNewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WhatsNewViewFragment wvf = new WhatsNewViewFragment();
                Bundle args = new Bundle();
                args.putString("version", versions[position]);
                args.putString("text", updateNews[position]);
                args.putString("date", dates[position]);
                wvf.setArguments(args);

                ((MainActivity) getActivity()).replaceFragment(R.id.themaincontainer,wvf);
            }
        });
        return view;
    }
}
