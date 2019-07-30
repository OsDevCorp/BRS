package com.misis.brs.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.misis.brs.Adapters.NewsViewAdapter;
import com.misis.brs.Database.News;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

import java.util.Vector;

public class NewsFragment extends Fragment {

    private NewsViewAdapter newsViewAdapter;
    private ListView listView;
    private Vector<News> news;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);

       //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.news_toolbar);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        news = new Vector<>();
        News item = new News();
        item.setDescription("I don't know if anybody has come across this problem but when we are in FS00 and we click on the little box beside the GL account # to look up a number we have found that if you enter an * and then number and * again in GL Long Text it will not find it. IE *703*. We have none GL number number descriptions for our internal enties but we can find them using the search tool. Anybody know why they GL Long Text does not recognize numbe7777rs? I looked in sap notes and could not find anything.");
        item.setHeader("Новость");
        item.setDateNews(new Long(1563543062));

        news.add(item);
        news.add(item);

        newsViewAdapter = new NewsViewAdapter(getActivity(),news);
        listView =(ListView) view.findViewById(R.id.news_list);
        listView.setAdapter(newsViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsViewFragment newsViewFragment = new NewsViewFragment();
                Bundle args = new Bundle();
                args.putString("title",news.elementAt(position).getHeader());
                args.putLong("date", news.elementAt(position).getDateNews());
                args.putString("text",news.elementAt(position).getDescription());
                newsViewFragment.setArguments(args);

                ((MainActivity) getActivity()).replaceFragment(R.id.themaincontainer,newsViewFragment);
            }
        });
        return view;
    }

}
