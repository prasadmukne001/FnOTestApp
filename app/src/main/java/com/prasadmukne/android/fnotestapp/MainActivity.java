package com.prasadmukne.android.fnotestapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.prasadmukne.android.fnotestapp.ui.home.HomeFragment;
import com.prasadmukne.android.fnotestapp.utils.FNOViewModel;
import com.prasadmukne.android.fnotestapp.utils.SharedPrefUtility;
import com.prasadmukne.android.fnotestapp.view.adapter.ViewPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Prasad Mukne on 23-07-2020.
 */
public class MainActivity extends AppCompatActivity {

    private final int GROUP_ID = 1000;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private FNOViewModel mFnoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFnoViewModel = ViewModelProviders.of(this).get(FNOViewModel.class);

        initialiseUI();

        setupNavigationViewListener();

        setupDataObserver();
    }

    private void initialiseUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mViewPager = findViewById(R.id.viewpager);
        initialiseData();
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mDrawerToggle);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle.syncState();
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initialiseData() {
        String fragStackList = SharedPrefUtility.getInstance(getApplication()).getValue(SharedPrefUtility.FRAG_STACK);
        if (null == fragStackList || fragStackList.equals("[]"))
            mFnoViewModel.addFragment(getResources().getString(R.string.menu_default_page));
    }

    private void setupNavigationViewListener() {
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                navigationViewSelector(item);
                mDrawer.closeDrawers();
                if (item.getItemId() == 0) {
                    showAddPageDialog();
                } else {
                    int position = mFnoViewModel.getFragByPosition(item.getTitle().toString());
                    mViewPager.setCurrentItem(position, true);
                }
                return true;
            }
        });
    }

    private void navigationViewSelector(MenuItem item) {
        mNavigationView.getMenu().setGroupCheckable(GROUP_ID, false, false);
        mNavigationView.getMenu().setGroupCheckable(GROUP_ID, true, true);
        mNavigationView.setCheckedItem(item);
        item.setChecked(true);
    }

    private void setupDataObserver() {
        mFnoViewModel.getData().observe(this, new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(@Nullable ArrayList<String> s) {

                setupViewPager();

                setupNavigationViewOnObservableChange();
            }
        });
    }

    private void setupNavigationViewOnObservableChange() {
        int position = 1;
        mNavigationView.getMenu().removeGroup(GROUP_ID);
        Menu menu = mNavigationView.getMenu();
        menu.add(GROUP_ID, 0, Menu.CATEGORY_CONTAINER, getResources().getString(R.string.menu_add_page)).setIcon(R.drawable.ic_menu_add_page);
        for (String fragName : mFnoViewModel.getFragmentStack()) {
            if (position == 1) {
                menu.add(GROUP_ID, position, Menu.CATEGORY_CONTAINER, getResources().getString(R.string.menu_default_page)).setIcon(R.drawable.ic_menu_home_page);
                navigationViewSelector(menu.getItem(position));
            } else {
                menu.add(GROUP_ID, position, Menu.CATEGORY_CONTAINER, fragName).setIcon(R.drawable.ic_menu_slideshow);
                navigationViewSelector(menu.getItem(position));
                mViewPager.setCurrentItem(position, true);
            }
            position++;
        }
    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (String fragName : mFnoViewModel.getFragmentStack()) {
            adapter.addFragment(new HomeFragment(fragName), fragName);
        }
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void showAddPageDialog() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View addFragDialogView = factory.inflate(R.layout.dialog_add_page, null);
        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(addFragDialogView);
        addFragDialogView.findViewById(R.id.add_page_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enteredName = ((EditText) addFragDialogView.findViewById(R.id.page_name_edit_text)).getText().toString();
                if (enteredName.trim().equals(""))
                    Toast.makeText(MainActivity.this, R.string.empty_name, Toast.LENGTH_SHORT).show();
                else if (!mFnoViewModel.checkIfFragNameExists(enteredName)) {
                    mFnoViewModel.addFragment(enteredName);
                } else {
                    Toast.makeText(MainActivity.this, R.string.name_exists, Toast.LENGTH_SHORT).show();
                }
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }
}
