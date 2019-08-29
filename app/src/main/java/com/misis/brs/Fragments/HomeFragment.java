package com.misis.brs.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Mark;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

import java.util.Locale;

public class HomeFragment extends Fragment {

    private Spinner semester;
    private SharedPreferences pref;
    private TextView score;
    private CardView hint;
    private TextView mark;
    private ImageButton lamp;
    private boolean hintIsOpen = false;

    @Override
    public void onResume() {
        super.onResume();

        //для корректного отображения выбранного пункта меню
        ((MainActivity) getActivity()).bottomMenuStick(1);
        //пересчитываем сумму оценок
        scoreSum();
        //выставляем правильное значение оценки
        mark.setText(getMark());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        semester = (Spinner)getActivity().findViewById(R.id.semester_picker);

        score = (TextView) view.findViewById(R.id.score_label);

        hint = (CardView) view.findViewById(R.id.hint);
        mark = (TextView) view.findViewById(R.id.yourMark);
        lamp = (ImageButton) view.findViewById(R.id.idea);

        //скрываем подсказку
        hint.setVisibility(View.GONE);
        //ставим фон hint
        Resources res = getResources();
        Drawable background = res.getDrawable(R.drawable.hint_background);
        hint.setBackground(background);

        //ставим фон mark
        background = res.getDrawable(R.drawable.circle_background);
        mark.setBackground(background);
        //выставляем правильное значение оценки
        mark.setText(getMark());

        //обрабатываем нажатие на иконку
        lamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hintIsOpen){
                    hint.setVisibility(View.GONE);
                }else{
                    hint.setVisibility(View.VISIBLE);
                }
                hintIsOpen = !hintIsOpen;
            }
        });

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText("");
        semester.setVisibility(View.VISIBLE);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.semesters_list, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.view_semester_simple_dropdown_item);
        semester.setAdapter(adapter);

        //если семестр до этого не выбирался, то по умолчанию идёт 1ый семестр
        pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        if (!pref.contains("semester"))
        {
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("semester", 1);
            editor.apply();
        }
        //устанавливаем уже запомненное значение семестра
        int semesterValue = pref.getInt("semester", 1);
        semester.setSelection(semesterValue - 1); //так как считается с 0
        semester.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //запоминаем выбранный семестр
                pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("semester", position + 1); //так как считается с 0
                editor.apply();
                //пересчитываем сумму для выбранного семестра
                scoreSum();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //cчитаем сумму оценок
        scoreSum();
        return view;
    }

    private void scoreSum(){
        int sum=0;
        pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        Mark [] marks = DBHelper.selectMarksForSemester(pref.getInt("semester",1));
        if (marks != null) {
            for (int i = 0; i <marks.length ; i++) {
               sum+=marks[i].getMark();
            }
        }
        //переводим на русский
        String lang = Locale.getDefault().getDisplayLanguage();
        switch (lang){
            case "English":
                score.setText("YOUR SCORE\n" + sum + "/100");
                break;
            case "русский":
                score.setText("ВАШИ БАЛЛЫ\n" + sum + "/100");
                break;
        }

    }

    private String getMark(){
        int sum=0;
        pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        Mark [] marks = DBHelper.selectMarksForSemester(pref.getInt("semester",1));
        if (marks != null) {
            for (int i = 0; i <marks.length ; i++) {
                sum+=marks[i].getMark();
            }
        }
        if(sum == 0) return "0";
        if(sum >  0  && sum <= 50) return "2";
        if(sum >= 51 && sum <= 69) return "3";
        if(sum >= 70 && sum <= 84) return "4";
        if(sum >= 85) return "5";
        return "0";
    }
}
