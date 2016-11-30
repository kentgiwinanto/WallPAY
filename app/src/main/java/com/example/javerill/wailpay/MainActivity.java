package com.example.javerill.wailpay;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Provides UI for the main screen.
 */
public class MainActivity extends ActionBarActivity {


    private DrawerLayout mDrawerLayout;

    public FrameLayout layoutBG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(MainActivity.this);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_logo_abt);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        // Adding Toolbar to Main screen
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        // Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_account_circle_black_24dp);
        tabs.getTabAt(1).setIcon(R.drawable.icon_coin);
        tabs.getTabAt(2).setIcon(R.drawable.ic_account_balance_wallet_black_24dp);
        tabs.getTabAt(3).setIcon(R.drawable.ic_history_black_24dp);
        tabs.getTabAt(4).setIcon(R.drawable.ic_settings_black_24dp);

        // Create Navigation drawer and inlfate layout
        // NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        layoutBG = (FrameLayout) findViewById( R.id.dimbg);
        layoutBG.getForeground().setAlpha(0);
        //WalletContentFragment.updateActivity(this);
        // Adding menu icon to Toolbar
//        ActionBar supportActionBar = getSupportActionBar();
//        if (supportActionBar != null) {
//            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
//            supportActionBar.setDisplayHomeAsUpEnabled(true);
//        }

        //Set behavior of Navigation drawer
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    // This method will trigger on item Click of navigation menu
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        // Set item in checked state
//                        menuItem.setChecked(true);
//
//                        // TODO: handle navigation
//
//                        // Closing drawer on item click
//                        mDrawerLayout.closeDrawers();
//                        return true;
//                    }
//                });
        // Adding Floating Action Button to bottom right of main view
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Snackbar.make(v, "Hello Snackbar!",
//                        Snackbar.LENGTH_LONG).show();
//            }
//        });
    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new AccountContentFragment());
        adapter.addFragment(new TransactionContentFragment());
        adapter.addFragment(new WalletContentFragment());
        adapter.addFragment(new HistoryContentFragment());
        adapter.addFragment(new SettingsContentFragment());
        viewPager.setAdapter(adapter);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        //private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
            //mFragmentTitleList.add(title);
        }

        //@Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }
}