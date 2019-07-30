package com.misis.brs.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.app.Fragment;

import com.misis.brs.R;

public class SettingsFragment extends Fragment {

    private Switch notif;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = (View) inflater.inflate(R.layout.fragment_settings,container,false);

        //изменение toolbar
        ((TextView)getActivity().findViewById(R.id.toolbarText)).setText(R.string.settings_toolbar);
        ((Spinner)getActivity().findViewById(R.id.semester_picker)).setVisibility(View.GONE);

        notif = (Switch) view.findViewById(R.id.notif_switch);

        notif.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //TODO реализовать переключение
            }
        });
        return view;
    }
}
