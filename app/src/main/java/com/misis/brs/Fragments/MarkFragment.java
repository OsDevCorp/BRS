package com.misis.brs.Fragments;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.misis.brs.Adapters.MarkViewAdapter;
import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Mark;
import com.misis.brs.Database.MarkType;
import com.misis.brs.MainActivity;
import com.misis.brs.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Arrays;
import java.util.Vector;

public class MarkFragment extends Fragment {

    private NumberPicker markPicker;
    private NumberPicker maxMarkPicker;
    private EditText description;
    private Button addPoints;
    private MarkViewAdapter markViewAdapter;
    private Spinner markTypeSpinner;
    private ListView pointsList;
    private AlertDialog.Builder builder;

    @Override
    public void onResume() {
        super.onResume();

        //для корректного отображения выбранного пункта меню
        ((MainActivity) getActivity()).bottomMenuStick(2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_points,container,false);
        markTypeSpinner = (Spinner)getActivity().findViewById(R.id.semester_picker);

        pointsList = (ListView) view.findViewById(R.id.points_list);
        markPicker = (NumberPicker) view.findViewById(R.id.picker);
        maxMarkPicker = (NumberPicker) view.findViewById(R.id.max_mark_picker);
        description = (EditText) view.findViewById(R.id.description_edit_text);
        addPoints = (Button) view.findViewById(R.id.addPoints);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText("");
        markTypeSpinner.setVisibility(View.VISIBLE);

        //настраиваем пикеры
        //TODO подумать почему вылетает за граници массива
        String[] numsForMaxMarkPicker = new String[20];
        String[] numsForMarkPicker = new String[21];

        for(int i=0;i<numsForMaxMarkPicker.length;i++){
            numsForMaxMarkPicker[i] = Integer.toString(i + 1);
        }
        for(int i=0;i<numsForMarkPicker.length;i++){
            numsForMarkPicker[i] = Integer.toString(i);
        }

        //базовая настройка
        markPicker.setMinValue(1);
        markPicker.setMaxValue(numsForMarkPicker.length);
        markPicker.setDisplayedValues(numsForMarkPicker);

        maxMarkPicker.setMinValue(1);
        maxMarkPicker.setMaxValue(numsForMaxMarkPicker.length);
        maxMarkPicker.setDisplayedValues(numsForMaxMarkPicker);

        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        int semester = pref.getInt("semester", 0);

        //зависимость от выбранного семестра
        int markTypes = R.array.types_of_marks_list_odd;
        if (semester % 2 != 0) {
            markTypes = R.array.types_of_marks_list_even;
        }

        //изменение spinner
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(getActivity(), markTypes, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(R.layout.view_mark_type_simple_dropdown_item);

        markTypeSpinner.setAdapter(adapter);
        markTypeSpinner.setSelection(0); // Выставляем спинер на первый элемент(счёт с 0)
        markTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] numsForMaxMarkPicker = new String[20];
                String[] numsForMarkPicker = new String[21];

                //в зависимости от выбранного пункта
                switch (position){
                    case 0:
                    case 1:
                        numsForMaxMarkPicker = new String[20];
                        numsForMarkPicker = new String[21];
                        for(int i=0;i<numsForMaxMarkPicker.length;i++){
                            numsForMaxMarkPicker[i] = Integer.toString(i + 1);
                        }
                        for(int i=0;i<numsForMarkPicker.length;i++){
                            numsForMarkPicker[i] = Integer.toString(i);
                        }
                        markPicker.setMinValue(1);
                        markPicker.setMaxValue(numsForMarkPicker.length);
                        markPicker.setDisplayedValues(numsForMarkPicker);
                        markPicker.setValue(6);

                        maxMarkPicker.setMinValue(1);
                        maxMarkPicker.setMaxValue(numsForMaxMarkPicker.length);
                        maxMarkPicker.setDisplayedValues(numsForMaxMarkPicker);
                        maxMarkPicker.setValue(5);
                        break;
                    case 2:
                    case 3:
                        numsForMaxMarkPicker = new String[1];
                        numsForMaxMarkPicker[0] = Integer.toString(10);

                        numsForMarkPicker = new String[11];
                        for(int i=0;i<numsForMarkPicker.length;i++)
                            numsForMarkPicker[i]=Integer.toString(i);
                        markPicker.setMinValue(1);
                        markPicker.setMaxValue(numsForMarkPicker.length);
                        markPicker.setDisplayedValues(numsForMarkPicker);
                        markPicker.setValue(11);

                        maxMarkPicker.setMinValue(1);
                        maxMarkPicker.setMaxValue(numsForMaxMarkPicker.length);
                        maxMarkPicker.setDisplayedValues(numsForMaxMarkPicker);
                        break;
                    case 4:
                    case 5:
                        numsForMaxMarkPicker = new String[1];
                        numsForMaxMarkPicker[0] = Integer.toString(5);

                        numsForMarkPicker = new String[6];
                        for(int i=0;i<numsForMarkPicker.length;i++)
                            numsForMarkPicker[i]=Integer.toString(i);

                        markPicker.setMinValue(1);
                        markPicker.setMaxValue(numsForMarkPicker.length);
                        markPicker.setDisplayedValues(numsForMarkPicker);
                        markPicker.setValue(6);

                        maxMarkPicker.setMinValue(1);
                        maxMarkPicker.setMaxValue(numsForMaxMarkPicker.length);
                        maxMarkPicker.setDisplayedValues(numsForMaxMarkPicker);
                        break;
                    case 6:
                    case 7:
                        numsForMaxMarkPicker = new String[1];
                        numsForMaxMarkPicker[0] = Integer.toString(10);

                        numsForMarkPicker = new String[6];
                        for(int i=0;i<numsForMarkPicker.length;i++)
                            numsForMarkPicker[i]=Integer.toString(i*2);
                        markPicker.setMinValue(1);
                        markPicker.setMaxValue(numsForMarkPicker.length);
                        markPicker.setDisplayedValues(numsForMarkPicker);
                        markPicker.setValue(6);

                        maxMarkPicker.setMinValue(1);
                        maxMarkPicker.setMaxValue(numsForMaxMarkPicker.length);
                        maxMarkPicker.setDisplayedValues(numsForMaxMarkPicker);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //обрабатываем нажатие кнопки добавления оценки
        addPoints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int markValue, maxMarkValue = 0;
                //выявляем элемент от его позиции и типа оценки
                markValue = markPicker.getValue()-1;
                switch (markTypeSpinner.getSelectedItemPosition()){
                    case 6:
                    case 7:
                        markValue =((markPicker.getValue()-1)*2);
                        break;
                    default: break;
                }
                switch (markTypeSpinner.getSelectedItemPosition()){
                    case 0:
                    case 1:
                        maxMarkValue = maxMarkPicker.getValue();
                        break;
                    case 2:
                    case 3:
                    case 6:
                    case 7:
                       maxMarkValue = 10;
                        break;
                    case 4:
                    case 5:
                        maxMarkValue = 5;
                        break;
                }
                //Проверка на корректность ввода
               if (markValue > maxMarkValue)
                {
                    final Snackbar notificationSnackbar = Snackbar.make(
                            v,
                            R.string.snackbarHigherMax,
                            Snackbar.LENGTH_SHORT
                    );
                    notificationSnackbar.show();
                    return;
                }
                //создаём добавляемую оценку
                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
                Mark addingMark = new Mark();
                addingMark.setMaxMark(maxMarkValue);
                addingMark.setMark(markValue);
                addingMark.setSemester(pref.getInt("semester", 0));
                MarkType[] types = MarkType.values();
                addingMark.setMarkType(types[markTypeSpinner.getSelectedItemPosition()]);
                addingMark.setDescription(description.getText().toString());
                //проверяем на возможность добавления
                int returned = DBHelper.isAbleToAdd(addingMark);
                //обрабатываем различные случаи переполнения по типу оценки
                switch (returned){
                    case 0:
                        DBHelper.insertMark(addingMark);
                        description.setText("");
                        break;
                    case 1:
                        final Snackbar notificationSnackbar = Snackbar.make(
                                v,
                                R.string.snackbarLimitCount,
                                Snackbar.LENGTH_LONG
                        );
                        notificationSnackbar.show();
                        break;
                    case 2:
                        final Snackbar notificationSnackbar1 = Snackbar.make(
                                v,
                                R.string.snackbarLimitPoints,
                                Snackbar.LENGTH_LONG
                        );
                        notificationSnackbar1.show();
                        break;
                }

                //обновляем список оценок на экране
                refreshMarkList();
            }
        });

        //обработка адаптера. Выводим оценки за семестр при создании вида
        Vector<Mark> marks = new Vector<>();
        Mark[] bdMarks = DBHelper.selectMarksForSemester(pref.getInt("semester",0));
        if (bdMarks != null) {
            marks.addAll(Arrays.asList(bdMarks));
        }
        markViewAdapter = new MarkViewAdapter(getActivity(),marks);
        pointsList.setAdapter(markViewAdapter);
        //обработка удаления записи об оценке
        pointsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                //создаём диалог
                builder = new AlertDialog.Builder(getActivity());//необходим именно этот метод другой вариант взятия контекста не работает
                builder.setTitle(R.string.pointDialogTitle);
                builder.setMessage(R.string.message);
                //удаляем запись
                builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DBHelper.deleteMarkById(((Mark)markViewAdapter.getItem(position)).getId());
                        //обновляем список
                        refreshMarkList();
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
                return false;
            }
        });
        return view;
    }

    private void refreshMarkList() {
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
        //подгружаем записи из БД
        Mark[] bdMarks = DBHelper.selectMarksForSemester(pref.getInt("semester",0));
        Vector<Mark> marks = new Vector<>();
        if (bdMarks != null) {
            marks = new Vector<>(Arrays.asList(bdMarks));
        }
        MarkViewAdapter.setMarks(marks);
        //говорим об изменении массива для перерисовки
        markViewAdapter.notifyDataSetChanged();
    }
}
