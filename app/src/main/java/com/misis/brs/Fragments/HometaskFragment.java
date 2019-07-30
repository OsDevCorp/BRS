package com.misis.brs.Fragments;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.misis.brs.Adapters.HometaskViewAdapter;
import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Hometask;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

import java.util.Arrays;
import java.util.Vector;

public class HometaskFragment extends Fragment {

    private HometaskViewAdapter hometasksViewAdapter;
    private ListView hometaskList;
    private AlertDialog.Builder builder;
    private FloatingActionButton addHometask;

    @Override
    public void onResume() {
        super.onResume();
        //TODO раскомментировать после добавки сохранения в бд
        //refreshHometaskList();
        ((MainActivity) getActivity()).bottomMenuStick(3);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText("");
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        hometaskList = (ListView) view.findViewById(R.id.tasks_list);
        addHometask = (FloatingActionButton) view.findViewById(R.id.addButton);

        //обработка адаптер. Выводим спиоск дз при создании вида
        final Vector<Hometask> hometasks = new Vector<>();
        Hometask ht = new Hometask(1564351428);
        ht.setCheckDone(false);
        ht.setCheckNotify(true);
        ht.setDescription("Writing Blog");

        hometasks.add(ht);

        //TODO раскомментировать после добавления создания дз
        /*SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        Hometask[] bdHometasks = DBHelper.selectHometaskForSemester(pref.getInt("semester",0));
        if (bdHometasks != null) {
            hometasks.addAll(Arrays.asList(bdHometasks));
        }
        */
        hometasksViewAdapter = new HometaskViewAdapter(getActivity(),hometasks);
        hometaskList.setAdapter(hometasksViewAdapter);
        //удаление по долгому нажатию
        hometaskList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                builder = new AlertDialog.Builder(getActivity());//необходим именно этот метод другой вариант взятия контекста не работает
                builder.setTitle(R.string.hometaskDialogTitle);
                builder.setMessage(R.string.message);
                //удаляем запись
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO раскомментировать после добавки сохранения в бд
                        //DBHelper.deleteHometaskByDeadline(((Hometask)hometasksViewAdapter.getItem(position)).getDeadline());
                        //обновляем список
                        //refreshHometaskList();
                    }
                });
                //не удадяем запись
                builder.setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //ничего не делаем
                    }
                });
                builder.setCancelable(true);
                builder.create();
                builder.show();
                return true; // такое значение нужно, чтобы при долгом нажатии так же не срабатывал и слушатель короткого нажатия
            }
        });

        //просмотр и редактирование по нажатию
        hometaskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //создаём экземпляр класса для замены на него
                //через bundle передаём необходимые для отображения переменные
                HometaskViewEditFragment htView = new HometaskViewEditFragment();
                Bundle args = new Bundle();
                args.putLong("deadline",hometasks.elementAt(position).getDeadline());
                args.putString("description",hometasks.elementAt(position).getDescription());
                args.putBoolean("checkDone",hometasks.elementAt(position).getCheckDone());
                args.putBoolean("checkNotif",hometasks.elementAt(position).getCheckNotify());
                args.putInt("semester",hometasks.elementAt(position).getSemester());
                args.putLong("timeNotif",hometasks.elementAt(position).getTimeNotification());
                htView.setArguments(args);

                ((MainActivity) getActivity()).replaceFragment(R.id.themaincontainer,htView);
            }
        });

        //добавление дз по кнопке
        addHometask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    public void refreshHometaskList() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        //подгружаем записи из БД
        Hometask[] bdHometasks = DBHelper.selectHometaskForSemester(pref.getInt("semester",0));
        Vector<Hometask> hometasks = new Vector<>();
        if (bdHometasks != null) {
            hometasks = new Vector<>(Arrays.asList(bdHometasks));
        }
        HometaskViewAdapter.setHometasks(hometasks);
        //говорим об изменении массива для перерисовки
        hometasksViewAdapter.notifyDataSetChanged();
    }
}
