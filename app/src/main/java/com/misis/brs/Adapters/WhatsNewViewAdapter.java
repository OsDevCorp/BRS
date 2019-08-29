package com.misis.brs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.misis.brs.R;

public class WhatsNewViewAdapter extends BaseAdapter {

    class UpdateNews{
        String date;
        String version;
        String updateNews;
    }

    private UpdateNews[] upNews;
    private Context context;

    public WhatsNewViewAdapter(String[] dates, String[] versions, String[] updateNews, Context context){
        upNews = new UpdateNews[dates.length];
        for (int i = 0; i < upNews.length ; i++) {
            upNews[i] = new UpdateNews();
            upNews[i].date = dates[i];
            upNews[i].version = versions[i];
            upNews[i].updateNews = updateNews[i];
        }
        this.context = context;
    }

    @Override
    public int getCount() {
        return upNews.length;
    }

    @Override
    public Object getItem(int position) {
        return upNews[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_whatsnew_simpe_item, null);

        ((TextView) view.findViewById(R.id.version)).setText(upNews[position].version);
        ((TextView) view.findViewById(R.id.date)).setText(upNews[position].date);
        ((TextView) view.findViewById(R.id.updateNews)).setText(upNews[position].updateNews);
        return view;
    }
}
