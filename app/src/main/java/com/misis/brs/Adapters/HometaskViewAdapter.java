package com.misis.brs.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

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

        Resources res = context.getResources();
        Drawable border;

        final CheckBox done = (CheckBox) view.findViewById(R.id.done);
        final LinearLayout cover = (LinearLayout) view.findViewById(R.id.linearLayout);
        final CardView card = (CardView) view.findViewById(R.id.card);

        //выставляем цевет обводки
        border = hometasks.get(position).getCheckDone() ?
                res.getDrawable(R.drawable.card_background_on):
                res.getDrawable(R.drawable.card_background_off);
        card.setBackground(border);

        ((TextView) view.findViewById(R.id.taskText)).setText(hometasks.get(position).getDescription());
        ((TextView) view.findViewById(R.id.taskDate)).setText(TimeHelper.getTime(hometasks.get(position).getDeadline()));
        done.setChecked(hometasks.get(position).getCheckDone());

        final Hometask task = hometasks.get(position);

        done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //изменяем значение и сохраняем в базу данных
                task.setCheckDone(isChecked);
                DBHelper.updateHometask(task);

                Resources res = context.getResources();
                Drawable border;

                //перекрашиваем цвет обводки
                border = isChecked ?
                     res.getDrawable(R.drawable.card_background_on):
                     res.getDrawable(R.drawable.card_background_off);
                card.setBackground(border);
            }
        });
        //чтобы и область вокруг чекбокса реагировала на нажатие
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //изменяем значение и сохраняем в базу данных
                task.setCheckDone(!task.getCheckDone());
                //не забываем изменять отображение чекера
                //так как тут вызывается метод onCheckedChanged, то обновлять запись в бд не нужно
                done.setChecked(task.getCheckDone());
            }
        });
        return view;
    }
}
