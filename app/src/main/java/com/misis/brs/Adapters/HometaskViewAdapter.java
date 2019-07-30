package com.misis.brs.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Hometask;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

import java.util.Vector;

public class HometaskViewAdapter extends BaseAdapter {

    private Context context;
    private static Vector<Hometask> hometasks;

    public HometaskViewAdapter(Context context, Vector<Hometask> hometasks){
        this.context = context;
        this.hometasks = hometasks;
    }

    public static void setHometasks(Vector<Hometask> hometasks) {
        HometaskViewAdapter.hometasks = hometasks;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.view_task_simple_item,null);

        CheckBox notification = ((CheckBox) view.findViewById(R.id.notification));
        CheckBox done = ((CheckBox) view.findViewById(R.id.done));
        ((TextView) view.findViewById(R.id.taskText)).setText(hometasks.get(position).getDescription());
        ((TextView) view.findViewById(R.id.taskDate)).setText(TimeHelper.getTime(hometasks.get(position).getDeadline()));
        notification.setChecked(hometasks.get(position).getCheckNotify());
        done.setChecked(hometasks.get(position).getCheckDone());

        final Hometask task = hometasks.get(position);

        //обработка нажатия на checkBox
        notification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO добавить отмену уведомления
                task.setCheckNotify(isChecked);
                DBHelper.updateHometask(task);
            }
        });

        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //изменяем значение и сохраняем в базу данных
                task.setCheckDone(isChecked);
                DBHelper.updateHometask(task);
            }
        });

        return view;
    }
}
