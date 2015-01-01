package com.sec.omnium.omnikey.akts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.mainClasses.NavigationMain;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity{

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new FragmentHome()).commit();
        }

        // FIM ONCREATE ======================================================================================================================================================

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }



    public void post() {

//        // Create a new HttpClient and Post Header
//        String downloadedString= null;
//
//        HttpClient httpclient = new DefaultHttpClient();
//
//
//        //for registerhttps://te
//        HttpPost httppost = new HttpPost("http://posttestserver.com/post.php");
//        //add data
//        try{
//            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//            nameValuePairs.add(new BasicNameValuePair("email", "mike.bulurt66@gmail.com"));
//            nameValuePairs.add(new BasicNameValuePair("password", "qwert"));
//
//            //add data
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//
//            InputStream in = response.getEntity().getContent();
//            StringBuilder stringbuilder = new StringBuilder();
//            BufferedReader bfrd = new BufferedReader(new InputStreamReader(in),1024);
//            String line;
//            while((line = bfrd.readLine()) != null)
//                stringbuilder.append(line);
//
//            downloadedString = stringbuilder.toString();
//
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("downloadedString:in login:::"+downloadedString);
//
//
//       updateMsg(downloadedString);
//

        String url = "http://posttestserver.com/post.php";
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        try {
            JSONObject email = new JSONObject();
            JSONObject pass = new JSONObject();
            email.put("email", "SentEmail");
            pass.put("pass", "SentPass");

            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            pairs.add(new BasicNameValuePair("key1", "value1"));
            pairs.add(new BasicNameValuePair("key2", "value2"));
            post.setEntity(new UrlEncodedFormEntity(pairs));


            HttpResponse response = client.execute(post);

//            postData transmitter = new postData();
//            transmitter.execute(new JSONObject[] {email, pass});

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public void alert(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

//    public class LoginFragment extends Fragment {
//
//        public static final String IMAGE_RESOURCE_ID = "iconResourceID";
//        public static final String ITEM_NAME = "itemName";
//
//        public LoginFragment() {
//
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
//            final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
//
//            final FloatingActionButton actionA = (FloatingActionButton) rootView.findViewById(R.id.actionA);
//            final FloatingActionButton actionB = (FloatingActionButton) rootView.findViewById(R.id.actionB);
//
//            actionA.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    actionA.setTitle("oi");
//                    Toast.makeText(MainActivity.this, "Clicked " + actionA.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            actionB.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    actionA.setTitle("oi");
//                    Toast.makeText(MainActivity.this, "Clicked " + actionB.getTitle(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
////            rootView.findViewById(R.id.pink_icon).setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View v) {
////                    Toast.makeText(MainActivity.this, "Clicked pink Floating Action Button", Toast.LENGTH_SHORT).show();
////                }
////            });
////
////            FloatingActionButton button = (FloatingActionButton) rootView.findViewById(R.id.setter);
////            button.setSize(FloatingActionButton.SIZE_MINI);
////            button.setColorNormalResId(R.color.pink);
////            button.setColorPressedResId(R.color.pink_pressed);
////            button.setIcon(R.drawable.ic_launcher);
//
//
//            return rootView;
//        }
//    }


    public class FragmentHome extends Fragment {



        private ImageView mLogo;
        private CardView mCard;
        private LinearLayout mCardLayout;

        private AnimatorSet animLogo;
        private AnimatorSet animCard;

        float logoY;
        int fragmentWidth;
        int fragmentHeight;
        float cardYMov;
        float cardYSize;
        int calls;
        int i = 0;

        public static final String IMAGE_RESOURCE_ID = "iconResourceID";
        public static final String ITEM_NAME = "itemName";

        public FragmentHome() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final View rootView = inflater.inflate(R.layout.main_fragment, container, false);


            mLogo = (ImageView) rootView.findViewById(R.id.logoMain);
            mCard = (CardView) rootView.findViewById(R.id.card_view);
            mCardLayout = (LinearLayout) rootView.findViewById(R.id.card_layout);




            animLogo = new AnimatorSet();

            final RelativeLayout.LayoutParams paramsLogo = (RelativeLayout.LayoutParams) mLogo.getLayoutParams();
            int logoStartYMargin = paramsLogo.topMargin;

            final ValueAnimator resetPosYLogo = ValueAnimator.ofInt(logoStartYMargin, logoStartYMargin);
            resetPosYLogo.setTarget(mLogo);

            final ObjectAnimator resetAlphaLogo = ObjectAnimator.ofFloat(mLogo, "alpha", 0, 0);
            ObjectAnimator resetPosZLogo = ObjectAnimator.ofFloat(mLogo, "elevation", 0, 5);

            ObjectAnimator appearLogo = ObjectAnimator.ofFloat(mLogo, "alpha", 0, 1);
            appearLogo.setDuration(200);
            appearLogo.setStartDelay(700);


            final ValueAnimator moveUp = ValueAnimator.ofInt(0, 0);
            moveUp.setTarget(mLogo);
            moveUp.setDuration(600);
            moveUp.setStartDelay(1000);


            animLogo.play(resetAlphaLogo).with(resetPosYLogo).with(resetPosZLogo);
            animLogo.play(appearLogo).after(resetAlphaLogo);
            animLogo.play(moveUp).after(appearLogo);


            moveUp.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsLogo.topMargin = (Integer) animation.getAnimatedValue();
                    mLogo.requestLayout();
                }
            });

            resetPosYLogo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsLogo.topMargin = (Integer) animation.getAnimatedValue();
                    mLogo.requestLayout();
                }
            });



            animCard = new AnimatorSet();

            final RelativeLayout.LayoutParams paramsCard = (RelativeLayout.LayoutParams) mCard.getLayoutParams();
            final int cardStartYMargin = 0;
            int cardStartXMargin = 0;

            final ValueAnimator resetPosXCard = ValueAnimator.ofInt(cardStartXMargin, cardStartXMargin);
            resetPosXCard.setTarget(mCard);

            final ValueAnimator resetPosYCard = ValueAnimator.ofInt(cardStartYMargin, cardStartYMargin);
            resetPosYCard.setTarget(mCard);


            final ObjectAnimator resetAlphaCard = ObjectAnimator.ofFloat(mCard, "alpha", 1, 1);
            final ObjectAnimator resetSizeXCard = ObjectAnimator.ofFloat(mCard, "scaleX", 0, 0);
            final ObjectAnimator resetSizeYCard = ObjectAnimator.ofFloat(mCard, "scaleY", 0, 0);

            ObjectAnimator appearCard = ObjectAnimator.ofFloat(mCard, "alpha", 1, 1);
            appearCard.setStartDelay(1600);

            ObjectAnimator scaleX = ObjectAnimator.ofFloat(mCard, "scaleX", 0, 1);
            scaleX.setDuration(500);
            scaleX.setStartDelay(200);

            final ObjectAnimator scaleY = ObjectAnimator.ofFloat(mCard, "scaleY", 0, 1);
            scaleY.setDuration(500);
            scaleY.setStartDelay(400);

            final ValueAnimator moveDown = ValueAnimator.ofInt(0, 0);
            moveDown.setTarget(mCard);
            moveDown.setDuration(500);
            moveDown.setStartDelay(400);

            animCard.play(resetAlphaCard).with(resetSizeXCard).with(resetSizeYCard).with(resetPosXCard).with(resetPosYCard);
            animCard.play(appearCard).after(resetAlphaCard);
            animCard.play(scaleX).with(scaleY).with(moveDown);
            animCard.play(scaleX).after(appearCard);

            resetPosYCard.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.topMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
                    mCardLayout.requestLayout();
                }
            });

            resetPosXCard.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.leftMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
                    mCardLayout.requestLayout();
                }
            });

            moveDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.topMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
                    mCardLayout.requestLayout();
                }
            });



            scaleX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mCard.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    calls++;

                    fragmentWidth = getView().getWidth();
                    fragmentHeight = getView().getHeight();

                    RelativeLayout.LayoutParams paramsCardTemp = (RelativeLayout.LayoutParams) mCard.getLayoutParams();
                    paramsCard.width = fragmentWidth - (fragmentWidth / 10);
                    paramsCard.height = fragmentHeight - (fragmentHeight/ 3);
                    mCard.setLayoutParams(paramsCard);


                    mLogo = (ImageView) getView().findViewById(R.id.logoMain);
                    logoY = mLogo.getY();

                    cardYMov = (logoY - (mLogo.getHeight() / 3));

                    mCard.setScaleX(0);
                    mCard.setScaleY(0);


                    resetPosXCard.setIntValues((int) 0, (int) 0);
                    resetPosYCard.setIntValues((int) cardStartYMargin, (int) (logoY - (fragmentHeight / 4)));
                    moveDown.setIntValues((int) 0,  (int) cardYMov);

                    resetPosYLogo.setIntValues((int) logoY, (int) logoY);
                    moveUp.setIntValues((int) logoY, (int) (logoY - (fragmentHeight / 4)));




                    if(fragmentWidth > 0)
                    {
                        getView().getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });



            mLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("MEU LOG", "Card Height:" + mCard.getHeight());
//                    Log.d("MEU LOG", "Card Wight:" + mCard.getWidth());

//                    mCardLayout.setVisibility(View.GONE);
                    mCard.setVisibility(View.GONE);

                       animLogo.start();
                       animCard.start();
                       i++;

                }
            });

            final Intent intent = new Intent(getActivity(), NavigationMain.class);

            Button btn1 = (Button) rootView.findViewById(R.id.btn1);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startActivity(intent);

//                    Fragment newFragment = new LoginFragment();
//                    FragmentTransaction transaction = getActivity().getFragmentManager().beginTransaction();
//
//                    transaction.replace(R.id.container, newFragment);
//                    transaction.addToBackStack(null);
//
//                    transaction.commit();

                }
            });


            return rootView;
        }
    }

}


