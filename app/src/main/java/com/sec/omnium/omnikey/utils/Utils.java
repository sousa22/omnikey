package com.sec.omnium.omnikey.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.mainClasses.NavigationMain;

public class Utils {

    //Set all the navigation icons and always to set "zero 0" for the item is a category
//    public static int[] iconNavigation = new int[] {
//            0, R.drawable.ic_action_download, R.drawable.ic_action_divisao, R.drawable.ic_action_delete,
//            R.drawable.ic_action_update, R.drawable.ic_action_done, 0, R.drawable.ic_action_settings,
//            R.drawable.ic_action_add, R.drawable.ic_action_discard, 0, R.drawable.ic_action_map, R.drawable.ic_action_share};

    public static int[] iconNavigation = new int[] {
            0, android.R.drawable.ic_menu_upload, android.R.drawable.ic_menu_directions, android.R.drawable.ic_menu_delete,
            android.R.drawable.ic_menu_rotate, android.R.drawable.ic_menu_set_as, 0, android.R.drawable.ic_menu_edit,
            android.R.drawable.ic_menu_add, android.R.drawable.ic_menu_close_clear_cancel, 0, android.R.drawable.ic_menu_compass, android.R.drawable.ic_menu_share};

    //get title of the item navigation
    public static String getTitleItem(Context context, int posicao){
        String[] titulos = context.getResources().getStringArray(R.array.nav_menu_items);
        return titulos[posicao];
    }

    public void alert(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static class MyAsyncTask extends AsyncTask<String, Void, String> {

        private Context mContext;
        private View rootView;
        private ImageView mLogo;
        private CardView mCard;

        private AnimatorSet animLogo;
        private AnimatorSet animCard;

        private float logoY;
        private int fragmentWidth;
        private int fragmentHeight;
        private float cardYMov;

        public MyAsyncTask(Context context, View rootView){
            this.mContext=context;
            this.rootView=rootView;
        }

        @Override
        protected String doInBackground(String... params) {

            mLogo = (ImageView) rootView.findViewById(R.id.logoMain);
            mCard = (CardView) rootView.findViewById(R.id.card_view);





            animLogo = new AnimatorSet();

            final RelativeLayout.LayoutParams paramsLogo = (RelativeLayout.LayoutParams) mLogo.getLayoutParams();
            int logoStartYMargin = paramsLogo.topMargin;

            final ValueAnimator resetPosYLogo = ValueAnimator.ofInt(logoStartYMargin, logoStartYMargin);
            resetPosYLogo.setTarget(mLogo);

            final ObjectAnimator resetAlphaLogo = ObjectAnimator.ofFloat(mLogo, "alpha", 0, 0);
            ObjectAnimator resetPosZLogo = ObjectAnimator.ofFloat(mLogo, "elevation", 0, 5);

            ObjectAnimator appearLogo = ObjectAnimator.ofFloat(mLogo, "alpha", 0, 1);
            appearLogo.setDuration(200);
            appearLogo.setStartDelay(500);


            final ValueAnimator moveUp = ValueAnimator.ofInt(0, 0);
            moveUp.setTarget(mLogo);
            moveUp.setDuration(600);
            moveUp.setStartDelay(300);

            animLogo.setInterpolator(new OvershootInterpolator());


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
            appearCard.setStartDelay(800);

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

            animCard.setInterpolator(new BounceInterpolator());

            animCard.play(resetAlphaCard).with(resetSizeXCard).with(resetSizeYCard).with(resetPosXCard).with(resetPosYCard);
            animCard.play(appearCard).after(resetAlphaCard);
            animCard.play(scaleX).with(scaleY).with(moveDown);
            animCard.play(scaleX).after(appearCard);

            resetPosYCard.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.topMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
                }
            });

            resetPosXCard.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.leftMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
                }
            });

            moveDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    paramsCard.topMargin = (Integer) animation.getAnimatedValue();
                    mCard.requestLayout();
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

                    fragmentWidth = rootView.getWidth();
                    fragmentHeight = rootView.getHeight();

                    paramsCard.width = fragmentWidth - (fragmentWidth / 10);
                    paramsCard.height = fragmentHeight - (fragmentHeight/ 3);
                    mCard.setLayoutParams(paramsCard);


                    mLogo = (ImageView) rootView.findViewById(R.id.logoMain);
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
                        rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }
                }
            });



            mLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mCard.setVisibility(View.GONE);

                    animLogo.start();
                    animCard.start();

                }
            });

            final Intent intent = new Intent(mContext, NavigationMain.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Button btn1 = (Button) rootView.findViewById(R.id.btn1);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    mContext.startActivity(intent);


                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
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

//        String url = "http://posttestserver.com/post.php";
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost(url);
//
//        try {
//            JSONObject email = new JSONObject();
//            JSONObject pass = new JSONObject();
//            email.put("email", "SentEmail");
//            pass.put("pass", "SentPass");
//
//            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//            pairs.add(new BasicNameValuePair("key1", "value1"));
//            pairs.add(new BasicNameValuePair("key2", "value2"));
//            post.setEntity(new UrlEncodedFormEntity(pairs));
//
//
//            HttpResponse response = client.execute(post);
//
////            postData transmitter = new postData();
////            transmitter.execute(new JSONObject[] {email, pass});
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        } catch (ClientProtocolException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }

}