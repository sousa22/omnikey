package com.sec.omnium.omnikey.akts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.hce.HceMainActivity;


public class MenuActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        final Intent intent = new Intent(this, FriendsActivity.class);
        final Intent intentNFC = new Intent(this, HceMainActivity.class);

        TextView name = (TextView) findViewById(R.id.name);
        TextView email = (TextView) findViewById(R.id.email);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        name.setText(extras.getString("name"));
        email.setText(extras.getString("email"));


        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.actionA);
        actionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                Bundle b = new Bundle();
//
//                b.putString("email", "sousandrei@gmail.com");
//                b.putString("name", "Andrei");
//
//                intent.putExtras(b);

//                final View card = rootView.findViewById(R.id.card_view2);
//                final View logo = rootView.findViewById(R.id.logoMain);

//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((android.app.Activity) mContext,
//                        Pair.create(logo, "logo"),
//                        Pair.create(card, "card"));

                startActivity(intent);
            }
        });

        final FloatingActionButton actionB = (FloatingActionButton) findViewById(R.id.actionB);
        actionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

//                Bundle b = new Bundle();
//
//                b.putString("email", "sousandrei@gmail.com");
//                b.putString("name", "Andrei");
//
//                intent.putExtras(b);

//                final View card = rootView.findViewById(R.id.card_view2);
//                final View logo = rootView.findViewById(R.id.logoMain);

//                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((android.app.Activity) mContext,
//                        Pair.create(logo, "logo"),
//                        Pair.create(card, "card"));

                startActivity(intentNFC);
            }
        });

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
