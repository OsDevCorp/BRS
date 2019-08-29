package com.misis.brs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;

import android.view.Menu;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.misis.brs.Database.DBHelper;
import com.misis.brs.Fragments.BugReportFragment;
import com.misis.brs.Fragments.ContactsFragment;
import com.misis.brs.Fragments.HomeFragment;
import com.misis.brs.Fragments.HometaskFragment;
import com.misis.brs.Fragments.MarkFragment ;
import com.misis.brs.Fragments.NewsFragment;
import com.misis.brs.Fragments.SettingsFragment;
import com.misis.brs.Fragments.WhatsNewFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ContactsFragment contactsFragment;
    private HomeFragment homeFragment;
    private HometaskFragment hometaskFragment;
    private MarkFragment  marksFragment;
    private NewsFragment newsFragment;
    private SettingsFragment settingsFragment;
    private WhatsNewFragment whatsNewFragment;
    private BugReportFragment bugReportFragment;

    private ImageButton ibDropdown;
    private TextView studName;
    private TextView group;
    private TextView teacherName;
    private TextView schedule;

    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private DrawerLayout drawer;

    //закрыта ли секция с экстра информацией в боковом меню
    private Boolean isClosed;
    private Boolean isOpen = false;
    private LinearLayout extraInfo;
    private LinearLayout llFirstLabel, llSecondLabel, llThirdLabel;

    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.nav_view_bottom);

        //инициализируем БД
        dbHelper = DBHelper.getInstance(getApplicationContext(),"test");

        //обрабатываем header бокового меню
        ibDropdown = navigationView.getHeaderView(0).findViewById(R.id.extraInfoButton);
        extraInfo = navigationView.getHeaderView(0).findViewById(R.id.extra);
        extraInfo.setVisibility(View.GONE);

        //сама информация
        teacherName = extraInfo.findViewById(R.id.teacherName);
        schedule = extraInfo.findViewById(R.id.schedule);
        group = navigationView.getHeaderView(0).findViewById(R.id.userGroup);
        studName = navigationView.getHeaderView(0).findViewById(R.id.userName);

        //присваиваем значения
        setHeader();

        isClosed=true; //информация скрыта

        //обработка открытия/закрытия доп информации
        ibDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClosed)
                    extraInfo.setVisibility(View.VISIBLE);
                else
                    extraInfo.setVisibility(View.GONE);
                isClosed = !isClosed;
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_forward);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);

        //инициализируем фрагменты
        contactsFragment = new ContactsFragment();
        homeFragment = new HomeFragment();
        hometaskFragment = new HometaskFragment();
        marksFragment = new MarkFragment ();
        newsFragment = new NewsFragment();
        settingsFragment = new SettingsFragment();
        whatsNewFragment = new WhatsNewFragment();
        bugReportFragment = new BugReportFragment();

        //нижнее меню
        llFirstLabel = findViewById(R.id.llMenuFirstLabel);
        llSecondLabel = findViewById(R.id.llMenuSecondLabel);
        llThirdLabel = findViewById(R.id.llMenuThirdLabel);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        replaceFragment(R.id.themaincontainer,homeFragment);
                        bottomMenuStick(1);
                        return true;
                    case R.id.navigation_points:
                        replaceFragment(R.id.themaincontainer,marksFragment);
                        bottomMenuStick(2);
                        return true;
                    case R.id.navigation_hometask:
                        replaceFragment(R.id.themaincontainer,hometaskFragment);
                        bottomMenuStick(3);
                        return true;
                }
                return false;
        }
        });

        //загружаем стартовый фрагмент
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.themaincontainer,homeFragment);
        ft.commit();
    }

    public void setHeader() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("Prefs", 0);
        if(!pref.getString("studName", "").equals("")){
            studName.setText(pref.getString("studName", ""));
        }else {
            studName.setText(R.string.student_s_name);
        }
        if(!pref.getString("group", "").equals("")){
            group.setText(pref.getString("group", ""));
        }else {
            group.setText(R.string.group);
        }
        if(!pref.getString("teacherName", "").equals("")){
            teacherName.setText(pref.getString("teacherName", ""));
        }else {
            teacherName.setText(R.string.teacherName);
        }
        String str = "";
        str += pref.getString("day1", "") + "\n";
        str += pref.getString("day2", "") + "\n";
        str += pref.getString("day3", "") + "\n";
        if(!str.equals("\n\n\n")){
            schedule.setText(str);
        }else {
            schedule.setText(R.string.scheduleExample);
        }
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        try {
            switch (id) {
//                case R.id.News:
//                    replaceFragment(R.id.themaincontainer,newsFragment);
//                    break;
                case R.id.Contacts:
                    replaceFragment(R.id.themaincontainer,contactsFragment);
                    break;
                case R.id.BugReport:
                    replaceFragment(R.id.themaincontainer,bugReportFragment);
                    break;
                case R.id.Help:
                    replaceFragment(R.id.themaincontainer,whatsNewFragment);
                    break;
                case R.id.Settings:
                    replaceFragment(R.id.themaincontainer,settingsFragment);
                    break;

            }

            emptyBottomMenu();

        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }

        drawer.closeDrawers();
        return true;
    }

    public void replaceFragment(int res, Fragment fragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(res,fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    /**
     * меняет положение маркера выбора
     * @param configuration номер с лева на право маркерованного пункта начиная с 1
     */
    public void bottomMenuStick(int configuration){
        switch (configuration){
            case 1:
                llFirstLabel.setVisibility(View.VISIBLE);
                llSecondLabel.setVisibility(View.INVISIBLE);
                llThirdLabel.setVisibility(View.INVISIBLE);
                break;
            case 2:
                llFirstLabel.setVisibility(View.INVISIBLE);
                llSecondLabel.setVisibility(View.VISIBLE);
                llThirdLabel.setVisibility(View.INVISIBLE);
                break;
            case 3:
                llFirstLabel.setVisibility(View.INVISIBLE);
                llSecondLabel.setVisibility(View.INVISIBLE);
                llThirdLabel.setVisibility(View.VISIBLE);
                break;
        }
    }
    public void emptyBottomMenu(){
        llFirstLabel.setVisibility(View.GONE);
        llSecondLabel.setVisibility(View.GONE);
        llThirdLabel.setVisibility(View.GONE);
    }
}
