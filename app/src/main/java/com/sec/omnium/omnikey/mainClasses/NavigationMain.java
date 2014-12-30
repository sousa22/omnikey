package com.sec.omnium.omnikey.mainClasses;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.adapter.NavigationAdapter;
import com.sec.omnium.omnikey.fragments.DownloadFragment;
import com.sec.omnium.omnikey.fragments.RouteFragment;
import com.sec.omnium.omnikey.utils.Constant;
import com.sec.omnium.omnikey.utils.Menus;
import com.sec.omnium.omnikey.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class NavigationMain extends ActionBarActivity{
    private long backPressedTime = 0;

    private int mLastPosition = 1;
    private ListView mListDrawer;

    private DrawerLayout mLayoutDrawer;
    private RelativeLayout mUserDrawer;
    private RelativeLayout mRelativeDrawer;

    private FragmentManager mFragmentManager;
    private NavigationAdapter mNavigationAdapter;
//    private ActionBarDrawerToggleCompat mDrawerToggle;
    private android.support.v7.app.ActionBarDrawerToggle actionBarDrawerToggle;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.navigation_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        getSupportActionBar().setIcon(R.drawable.ic_launcher);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mListDrawer = (ListView) findViewById(R.id.listDrawer);
        mRelativeDrawer = (RelativeLayout) findViewById(R.id.relativeDrawer);
        mLayoutDrawer = (DrawerLayout) findViewById(R.id.layoutDrawer);

        mUserDrawer = (RelativeLayout) findViewById(R.id.userDrawer);
        mUserDrawer.setOnClickListener(userOnClick);

        if (mListDrawer != null) {

            // All header menus should be informed here
            // listHeader.add(MENU POSITION)
            List<Integer> mListHeader = new ArrayList<Integer>();
            mListHeader.add(0);

            // All menus which will contain an accountant should be informed here
            // Counter.put ("POSITION MENU", "VALUE COUNTER");
            SparseIntArray  mCounter = new SparseIntArray();
            mCounter.put(Constant.MENU_DONE, 99);
//            mCounter.put(Constant.MENU_MAPS,10);
            mNavigationAdapter = new NavigationAdapter(this, NavigationList.getNavigationAdapter(this, mListHeader, mCounter, null));
        }

        mListDrawer.setAdapter(mNavigationAdapter);
        mListDrawer.setOnItemClickListener(new DrawerItemClickListener());

        actionBarDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this, mLayoutDrawer, toolbar, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view)
            {
//                super.onDrawerClosed(view);
//                invalidateOptionsMenu();
//                syncState();
                supportInvalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView)
            {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu();
//                syncState();
                mNavigationAdapter.notifyDataSetChanged();
                supportInvalidateOptionsMenu();
            }
            public void onDrawerSlide(View draverView, float slideOffSet){

                View view = getWindow().getDecorView();
                final FloatingActionsMenu actionMain = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);

//                Log.d("DRAWER", "Offset: " + slideOffSet);
            }
        };

        mLayoutDrawer.setDrawerListener(actionBarDrawerToggle);

//        mDrawerToggle = new ActionBarDrawerToggleCompat(this, mLayoutDrawer);
//        mLayoutDrawer.setDrawerListener(mDrawerToggle);


        if (savedInstanceState != null) {
            setLastPosition(savedInstanceState.getInt(Constant.LAST_POSITION));

            setTitleFragments(mLastPosition);
            mNavigationAdapter.resetarCheck();
            mNavigationAdapter.setChecked(mLastPosition, true);
        }else{
            setLastPosition(mLastPosition);
            setFragmentList(mLastPosition);
        }
    }

    private void setFragmentList(int posicao){

        Fragment mFragment = null;
        mFragmentManager = getSupportFragmentManager();

        switch (posicao) {
            case Constant.MENU_DOWNLOADS:
                mFragment = new DownloadFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_DOWNLOADS));
                break;
            case Constant.MENU_ROUTER:
                mFragment = new RouteFragment().newInstance(Utils.getTitleItem(NavigationMain.this, Constant.MENU_ROUTER));
                break;
//              MENUS AKI PORRA
        }

        if (mFragment != null){
            setTitleFragments(mLastPosition);
            mNavigationAdapter.resetarCheck();
            mNavigationAdapter.setChecked(posicao, true);
            mFragmentManager.beginTransaction().replace(R.id.content_frame, mFragment).commit();
        }
    }

    private void hideMenus(Menu menu, int posicao, View view) {

        boolean drawerOpen = mLayoutDrawer.isDrawerOpen(mRelativeDrawer);
        final FloatingActionsMenu actionMain = (FloatingActionsMenu) view.findViewById(R.id.multiple_actions);

        switch (posicao) {
            case Constant.MENU_DOWNLOADS:
//                menu.findItem(Menus.ADD).setVisible(!drawerOpen);
//                menu.findItem(Menus.UPDATE).setVisible(!drawerOpen);
                menu.findItem(Menus.SEARCH).setVisible(!drawerOpen);
                if(drawerOpen == true){
                    actionMain.setVisibility(View.GONE);
                } else {
                    actionMain.setVisibility(View.VISIBLE);
                }
                break;

            case Constant.MENU_ROUTER:
//                menu.findItem(Menus.ADD).setVisible(!drawerOpen);
                menu.findItem(Menus.SEARCH).setVisible(!drawerOpen);
                break;
        }
    }

    private void setTitleFragments(int position){
        setIconActionBar(Utils.iconNavigation[position]);
        setSubtitleActionBar(Utils.getTitleItem(NavigationMain.this, position));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt(Constant.LAST_POSITION, mLastPosition);
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case Menus.HOME:
                if (mLayoutDrawer.isDrawerOpen(mRelativeDrawer)) {
                    mLayoutDrawer.closeDrawer(mRelativeDrawer);

                } else {
                    mLayoutDrawer.openDrawer(mRelativeDrawer);
                }
                return true;
            default:

//                if (mDrawerToggle.onOptionsItemSelected(item)) {
                if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
                    return true;
                }

                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts

        if(mLayoutDrawer.isDrawerOpen(mRelativeDrawer)) {
            mLayoutDrawer.closeDrawer(mRelativeDrawer);
            return;
        } else{

            long t = System.currentTimeMillis();
            if (t - backPressedTime > 2000) {    // 2 secs
                backPressedTime = t;
                Toast.makeText(this, "Press back again to exit",
                        Toast.LENGTH_SHORT).show();
            } else {
                // this guy is serious
                // clean up
                super.onBackPressed();
               // bye
            }

        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        View view = getWindow().getDecorView();
        hideMenus(menu, mLastPosition, view);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
//        mDrawerToggle.syncState();
        actionBarDrawerToggle.syncState();
    }

    public void setTitleActionBar(CharSequence informacao) {
        getSupportActionBar().setTitle(informacao);
    }

    public void setSubtitleActionBar(CharSequence informacao) {
        getSupportActionBar().setSubtitle(informacao);
    }

    public void setIconActionBar(int icon) {
        getSupportActionBar().setIcon(icon);
    }

    public void setLastPosition(int posicao){
        this.mLastPosition = posicao;
    }

//    private class ActionBarDrawerToggleCompat extends ActionBarDrawerToggle {
//
//        public ActionBarDrawerToggleCompat(Activity mActivity, DrawerLayout mDrawerLayout){
//            super(
//                    mActivity,
//                    mDrawerLayout,
//                    R.drawable.ic_chevron_right_black_48dp,
//                    R.string.nav_drawer_open,
//                    R.string.nav_drawer_close);
//        }
//
//        @Override
//        public void onDrawerClosed(View view) {
//            supportInvalidateOptionsMenu();
//        }
//
//        @Override
//        public void onDrawerOpened(View drawerView) {
//            mNavigationAdapter.notifyDataSetChanged();
//            supportInvalidateOptionsMenu();
//        }
//    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
//        mDrawerToggle.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int posicao, long id) {
            setLastPosition(posicao);
            setFragmentList(mLastPosition);
            mLayoutDrawer.closeDrawer(mRelativeDrawer);
        }
    }

    private OnClickListener userOnClick = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            mLayoutDrawer.closeDrawer(mRelativeDrawer);
        }
    };
}