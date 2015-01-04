package com.sec.omnium.omnikey.utils;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.mainClasses.NavigationMain;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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

    public static void alert(Context context, String msg, String title) {
//        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setTitle(title);
        builder.setMessage("" + msg);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static int dpToPx(int dp)
    {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static class Login extends AsyncTask<Void, Void, String>{

        private Context mContext;
        private View rootView;


        Encryption _crypt;
        private String imei = "";
        private String key = "";
        private String iv = "";


        private String login;
        private String pass;

        HttpResponse response;

        public Login(Context context, View rootView){
            this.mContext = context;
            this.rootView = rootView;
        }

        @Override
        protected void onPreExecute() {

            TelephonyManager mngr = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            imei = mngr.getDeviceId();

            try {
                _crypt = new Encryption();
                key = Encryption.SHA256("sample", 32); //32 bytes = 256 bit
                iv = Encryption.generateRandomIV(16); //16 bytes = 128 bit

                key = imei;
                key += new StringBuilder(imei).reverse().toString();

                iv = imei + 0;


            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }


        @Override
        protected String doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();

            HttpConnectionParams.setConnectionTimeout(httpclient.getParams(), 10000); //Timeout Limit
            JSONObject json = new JSONObject();

            EditText loginField = (EditText) rootView.findViewById(R.id.login);
            login = loginField.getText().toString();

            EditText passField = (EditText) rootView.findViewById(R.id.pass);
            pass = passField.getText().toString();


//            login = "boludo@tacosmail.jajaja.com";
//            pass = "LaConchaDeTuMadre";

            try {
                // Add your data

//                HttpPost httppost = new HttpPost("http://192.168.0.12:8080/post");
                HttpPost httppost = new HttpPost("http://162.243.86.70:8080/post");

//                String encoded = _crypt.encrypt(text, key, iv); //encrypt
//                String decoded = _crypt.decrypt(encoded, key,iv); //decrypt

                login = _crypt.encrypt(login, key, iv);
                pass = _crypt.encrypt(pass, key, iv);

                json.put("login", login);
                json.put("pass", pass);
                json.put("imei", imei);

                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);

                response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if(response != null){
                try {

                    InputStream inputStream = null;
                    String Fresult = null;

                    inputStream = response.getEntity().getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }

                    Fresult = sb.toString();

                    String decoded = _crypt.decrypt(Fresult, key,iv); //decrypt

                    JSONObject jObject = new JSONObject(decoded);

                    final Intent intent = new Intent(mContext, NavigationMain.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    if(jObject.getBoolean("successful")) {
                        Bundle b = new Bundle();
                        String email = jObject.getJSONObject("user").getJSONObject("local").getString("email").toString();
                        String name = jObject.getJSONObject("user").getJSONObject("local").getString("name").toString();
                        b.putString("email", email);
                        b.putString("name", name);
                        intent.putExtras(b);
                        mContext.startActivity(intent);
//                        Encryption _crypt = new Encryption();
//                        String user = _crypt.decrypt(login, key, iv); //encrypt
//                        alert(mContext, "User: " + jObject.getJSONObject("user").toString(), "POST Response");
                    } else {
                        alert(mContext, jObject.getString("msg"), "Error");
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                    alert(mContext, "Error: " + e, "ERROR IOException");
                } catch (JSONException e) {
                    e.printStackTrace();
                    alert(mContext, "Error: " + e, "ERROR JSONException");
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

            } else {
                alert(mContext, "Response is null", "POST Error");
            }


        }
    }


    public static class SetLoginScreen extends AsyncTask<Void, Void, String> {

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

        public SetLoginScreen(Context context, View rootView){
            this.mContext=context;
            this.rootView=rootView;
        }

        @Override
        protected String doInBackground(Void... params) {

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
            appearCard.setStartDelay(200);

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

            animCard.setInterpolator(new AnticipateInterpolator());

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

            final Button btn1 = (Button) rootView.findViewById(R.id.btn1);

            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    mContext.startActivity(intent);


                    Login task = new Login(mContext, rootView);
                    task.execute();


                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }
    }


}