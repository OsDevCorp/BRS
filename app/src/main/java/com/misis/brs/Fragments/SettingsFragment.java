package com.misis.brs.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

public class SettingsFragment extends Fragment{

    private EditText studName;
    private EditText group;
    private EditText teacherName;
    private EditText day1;
    private EditText day2;
    private EditText day3;
    private Spinner day1Pick;
    private Spinner day2Pick;
    private Spinner day3Pick;
    private Button save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_settings,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.settings_toolbar);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.GONE);

        studName = (EditText) view.findViewById(R.id.studNameEdit);
        group = (EditText) view.findViewById(R.id.groupEdit);
        teacherName = (EditText) view.findViewById(R.id.teacherNameEdit);
        day1 = (EditText) view.findViewById(R.id.day1Edit);
        day2 = (EditText) view.findViewById(R.id.day2Edit);
        day3 = (EditText) view.findViewById(R.id.day3Edit);
        day1Pick = (Spinner) view.findViewById(R.id.day1Spin);
        day2Pick = (Spinner) view.findViewById(R.id.day2Spin);
        day3Pick = (Spinner) view.findViewById(R.id.day3Spin);
        save = (Button) view.findViewById(R.id.saveButton);

        //задаём значения по умолчанию
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        studName.setText(pref.getString("studName", ""));
        group.setText(pref.getString("group", ""));
        teacherName.setText(pref.getString("teacherName", ""));
        day1.setText(pref.getString("day1", ""));
        day2.setText(pref.getString("day2", ""));
        day3.setText(pref.getString("day3", ""));

        //добавляем обработчик сохранения
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //для сохранения данных
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
                SharedPreferences.Editor editor = pref.edit();
                String day;
                editor.putString("studName",studName.getText().toString());
                editor.putString("group",group.getText().toString());
                editor.putString("teacherName",teacherName.getText().toString());
                day = (String)day1Pick.getSelectedItem();
                editor.putString("day1",day + " " + day1.getText().toString());
                day = (String)day2Pick.getSelectedItem();
                editor.putString("day2",day + " " + day2.getText().toString());
                day = (String)day3Pick.getSelectedItem();
                editor.putString("day3",day + " " + day3.getText().toString());
                editor.apply();

                //скрываем клавиатуру
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(save.getWindowToken(), 0);

                //обновляем header
                ((MainActivity)getActivity()).setHeader();

                //сообщение о сохранени
                final Snackbar notificationSnackbar = Snackbar.make(
                        v,
                        R.string.snackbarSave,
                        Snackbar.LENGTH_LONG
                );
                notificationSnackbar.show();
            }
        });

        return view;
    }

    //Обрабатываем скрытие клавиатуры при выходе из фрагмента
    @Override
    public void onPause() {
        super.onPause();
        //скрываем клавиатуру
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);

    }
}
