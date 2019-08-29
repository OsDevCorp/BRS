package com.misis.brs.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.misis.brs.Database.Hometask;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

import java.util.Vector;

public class HometaskUpcomingViewAdapter extends BaseAdapter {

    private Context context;
    private static Vector<Hometask> hometasks;

    public HometaskUpcomingViewAdapter(Context context, Vector<Hometask> hometasks){
        this.context = context;
        this.hometasks = hometasks;
    }

    public static void setHometasks(Vector<Hometask> hometasks) {
        HometaskUpcomingViewAdapter.hometasks = hometasks;
    }

    @Override
    public int getCount() {
        return hometasks.size();
    }

    @Override
    public Object getItem(int position) {
        return hometasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_task_upcoming_simple_item,null);

        ((TextView) view.findViewById(R.id.taskText)).setText(hometasks.get(position).getDescription());
        ((TextView) view.findViewById(R.id.taskDate)).setText(TimeHelper.getTime(hometasks.get(position).getDeadline()));
        Log.d("taskText",((TextView) view.findViewById(R.id.taskText)).getText()+"");
        Log.d("taskDate",((TextView) view.findViewById(R.id.taskDate)).getText()+"");
        return view;
    }
}
