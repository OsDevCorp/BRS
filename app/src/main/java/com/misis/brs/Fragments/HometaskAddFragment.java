package com.misis.brs.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Hometask;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;

import java.util.Locale;

public class HometaskAddFragment extends Fragment {

    private long deadline;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deadline = getArguments().getLong("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_view_hometask,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(TimeHelper.getTime(deadline));
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        final EditText text = ((EditText) view.findViewById(R.id.textHometask));
        Button add = ((Button) view.findViewById(R.id.saveButton));
        final FragmentManager fm = getActivity().getFragmentManager();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(text.getText().toString().equals("")){
                    final Snackbar notificationSnackbar = Snackbar.make(
                            v,
                            R.string.snackbarEmptyText,
                            Snackbar.LENGTH_LONG
                    );
                    notificationSnackbar.show();
                }else {
                    //создаём запись 
                    Hometask addingHometask = new Hometask(deadline);
                    addingHometask.setCheckNotify(true);
                    addingHometask.setCheckDone(false);
                    addingHometask.setDescription(text.getText().toString());

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
                    addingHometask.setSemester(pref.getInt("semester",0));
                    // минус 9 часов ставим в 15:00
                    addingHometask.setTimeNotification(addingHometask.getDeadline()-32400);
                    DBHelper.insertHometask(addingHometask);
                    //TODO включить ноификацию

                    //выписываем время нотификации
                    //добавляем поддержку языков
                    String lang = Locale.getDefault().getDisplayLanguage();
                    String notif = "";
                    switch (lang){
                        case "English":
                            notif = "Notification will be in " + TimeHelper.getNotifTime(addingHometask.getTimeNotification());
                            break;
                        case "русский":
                            notif = "Уведомление будет в " + TimeHelper.getNotifTime(addingHometask.getTimeNotification());
                            break;
                    }
                    final Snackbar notificationSnackbar = Snackbar.make(
                            v,
                           notif,
                            Snackbar.LENGTH_LONG
                    );
                    notificationSnackbar.show();
                    //откатываемся в начало
                    fm.popBackStack();
                    fm.popBackStack();
                }
            }
        });
        return view;
    }
}
