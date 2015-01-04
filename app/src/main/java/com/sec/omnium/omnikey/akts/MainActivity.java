package com.sec.omnium.omnikey.akts;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.utils.Utils;


public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new FragmentHome()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public class FragmentHome extends Fragment {

        public static final String IMAGE_RESOURCE_ID = "iconResourceID";
        public static final String ITEM_NAME = "itemName";

        public FragmentHome() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final View rootView = inflater.inflate(R.layout.main_fragment, container, false);

            Utils.SetLoginScreen task = new Utils.SetLoginScreen(getActivity(), rootView);
            task.execute();


            return rootView;
        }
    }

}


