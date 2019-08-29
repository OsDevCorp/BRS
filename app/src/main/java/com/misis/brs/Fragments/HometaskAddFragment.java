package com.misis.brs.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.android.material.snackbar.Snackbar;
import com.misis.brs.Database.DBHelper;
import com.misis.brs.Database.Hometask;
import com.misis.brs.MainActivity;
import com.misis.brs.R;
import com.misis.brs.TimeHelper;


public class HometaskAddFragment extends Fragment {

    private long deadline;
    private Button add;
    Hometask addingHometask;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deadline = getArguments().getLong("date");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_edit_view_hometask, container, false);

        //изменение toolbar
        ((TextView) getActivity().findViewById(R.id.toolbarText)).setText(TimeHelper.getTime(deadline));
        ((Spinner) getActivity().findViewById(R.id.semester_picker)).setVisibility(View.INVISIBLE);

        //нижнее меню
        ((MainActivity) getActivity()).emptyBottomMenu();

        final EditText text = ((EditText) view.findViewById(R.id.textHometask));
        add = ((Button) view.findViewById(R.id.saveButton));
        final FragmentManager fm = getActivity().getFragmentManager();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //скрываем клавиатуру
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(add.getWindowToken(), 0);

                if (text.getText().toString().equals("")) {
                    final Snackbar notificationSnackbar = Snackbar.make(
                            v,
                            R.string.snackbarEmptyText,
                            Snackbar.LENGTH_LONG
                    );
                    notificationSnackbar.show();
                } else {
                    //создаём запись
                    addingHometask = new Hometask(deadline);
                    addingHometask.setCheckNotify(false);
                    addingHometask.setCheckDone(false);
                    addingHometask.setDescription(text.getText().toString());

                    SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("Prefs", 0);
                    addingHometask.setSemester(pref.getInt("semester", 0));

                    DBHelper.insertHometask(addingHometask);

                    //откатываемся в начало
                    fm.popBackStack();
                    fm.popBackStack();
                }
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
