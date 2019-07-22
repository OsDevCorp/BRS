package com.misis.brs.Fragments;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Mark;
import com.misis.brs.MainActivity;
import com.misis.brs.R;

public class HomeFragment extends Fragment {

    private Spinner semester;
    private SharedPreferences pref;
    private TextView score;
    @Override
    public void onResume() {
        super.onResume();

        //для корректного отображения выбранного пункта меню
        ((MainActivity) getActivity()).bottomMenuStick(1);
        //пересчитываем сумму оценок
        scoreSum();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        semester = (Spinner)getActivity().findViewById(R.id.semester_picker);

        score = (TextView)view.findViewById(R.id.score_label);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText("");
        semester.setVisibility(View.VISIBLE);

        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getActivity(), R.array.semesters_list, android.R.layout.simple_spinner_item);
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
        score.setText("TOTAL SCORE\n" + sum + "/100");
    }
}
