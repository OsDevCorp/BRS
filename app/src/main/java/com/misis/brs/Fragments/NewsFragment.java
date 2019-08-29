package com.misis.brs.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.misis.brs.Adapters.NewsViewAdapter;
import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.News;
import com.misis.brs.MainActivity;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutionException;

public class NewsFragment extends Fragment {

    private NewsViewAdapter newsViewAdapter;
    private ListView listView;
    private Vector<News> news;
    public int count = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);

       //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.news_toolbar);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);
        //получаем новости
        news = new Vector<>();

        if (isOnline(getActivity())){
           getNews(-1);
           for(int cur = 0; cur < count; cur++){
               news.add(getNews(Integer.valueOf(cur)));
           }
        }else{
            final Snackbar notificationSnackbar = Snackbar.make(
                    getActivity().findViewById(R.id.themaincontainer),
                    R.string.snackbarNoOnline,
                    Snackbar.LENGTH_LONG
            );
        }
        newsViewAdapter = new NewsViewAdapter(getActivity(),news);
        listView =(ListView) view.findViewById(R.id.news_list);
        listView.setAdapter(newsViewAdapter);
        final Vector<News> finalNews = news;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsViewFragment newsViewFragment = new NewsViewFragment();
                Bundle args = new Bundle();
                args.putString("title", finalNews.elementAt(position).getHeader());
                args.putLong("date", finalNews.elementAt(position).getDateNews());
                args.putString("text", finalNews.elementAt(position).getDescription());
                newsViewFragment.setArguments(args);

                ((MainActivity) getActivity()).replaceFragment(R.id.themaincontainer,newsViewFragment);
            }
        });
        return view;
    }

    private News getNews(Integer integer) {
        //Скачиваем новости
        @SuppressLint("StaticFieldLeak")
        AsyncTask<Integer,Void,News> task = new AsyncTask<Integer, Void, News>() {
            @Override
            protected News doInBackground(Integer... integers) {
                News newsItem = new News();
                int tmp = 0;
                try {
                    URL url = new URL("http://iyakt.team/category/alerts/rss");
                    XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                    //убираем namespace xml
                    factory.setNamespaceAware(false);
                    XmlPullParser xpp = factory.newPullParser();

                    //выдаём поток данных
                    xpp.setInput(getInputStream(url),"UTF_8");
                    boolean insideItem = false;
                    //тип эвента при чтении
                    int eventType = xpp.getEventType();

                   newsItem = new News();
                    while(eventType != XmlPullParser.END_DOCUMENT){
                        if(eventType == XmlPullParser.START_TAG){
                            if(xpp.getName().equalsIgnoreCase("item")){
                                insideItem = true;
                            }else if(xpp.getName().equalsIgnoreCase("title") && insideItem){
                                newsItem.setHeader(xpp.nextText());
                            }else if(xpp.getName().equalsIgnoreCase("description") && insideItem) {
                                newsItem.setDescription(xpp.nextText());
                            }
                            //выходим из элемента
                        }else if(eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")){
                            insideItem = false;
                            newsItem.setDateNews(System.currentTimeMillis()/1000);

                            if(count != -1 && integers[0] == tmp ){
                                Log.d("item",newsItem.getHeader()+newsItem.getDescription());
                                return newsItem;
                            }
                            tmp++;

                        }
                        //переходим на следующий эвент
                        eventType = xpp.next();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                if(count == -1){
                    count = tmp;
                    return null;
                }else
                    return newsItem;
            }
        };
        //выполняем таск
        task.execute(integer);
        try {
            return task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream(URL url){
        try {
            return url.openConnection().getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
