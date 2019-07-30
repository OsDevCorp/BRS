package com.misis.brs.Fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Hometask;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

public class HometaskViewEditFragment extends Fragment {
    private Hometask hometask;
    private AlertDialog.Builder builder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hometask = new Hometask(getArguments().getLong("deadline",0));
        hometask.setDescription(getArguments().getString("description",""));
        hometask.setCheckDone(getArguments().getBoolean("checkDone",false));
        hometask.setCheckNotify(getArguments().getBoolean("checkNotif",false));
        hometask.setSemester(getArguments().getInt("semester",0));
        hometask.setTimeNotification(getArguments().getLong("timeNotif",0));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_view_hometask,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(TimeHelper.getTime(hometask.getDeadline()));
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        final EditText description = (EditText) view.findViewById(R.id.textHometask);
        description.setText(hometask.getDescription());

        ((Button) view.findViewById(R.id.saveButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentManager fm = getActivity().getFragmentManager();
                builder = new AlertDialog.Builder(getActivity());//необходим именно этот метод другой вариант взятия контекста не работает
                builder.setTitle(R.string.hometaskEditDialogTitle);
                builder.setMessage(R.string.messageEdit);
                //соглашаемся
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //проверка на пустое дз
                        if(description.getText().toString().equals("")){
                            builder = new AlertDialog.Builder(getActivity());//необходим именно этот метод другой вариант взятия контекста не работает
                            builder.setTitle(R.string.hometaskDialogTitle);
                            builder.setMessage(R.string.message);
                            //удаляем запись
                            builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //TODO раскомментировать после добавки сохранения в бд
                                    //DBHelper.deleteHometaskByDeadline(((Hometask)hometasksViewAdapter.getItem(position)).getDeadline());
                                    fm.popBackStack();
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
                        }else {
                            //сохраняем изменения в бд
                            hometask.setDescription(description.getText().toString());
                            //TODO раскомментировать после добавки сохранения в бд
                            //DBHelper.updateHometask(hometask);
                            //откатываемся назад ко всем дз
                            fm.popBackStack();
                        }

                    }
                });
                //отказываемся
                builder.setNegativeButton(R.string.reject, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ничего не делаем
                        fm.popBackStack();
                    }
                });
                builder.setCancelable(false);
                builder.create();
                builder.show();
            }
        });

        return view;
    }
}
