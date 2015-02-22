package com.sec.omnium.omnikey.UI;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sec.omnium.omnikey.R;
import com.sec.omnium.omnikey.web.socket;

/**
 * Created by Usuario on 17/02/2015.
 */
public class SetScreens {

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
        protected String doInBackground(final Void... params) {

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
//            ValueAnimator appearLogo = (ValueAnimator) ViewAnimationUtils.createCircularReveal(mLogo, (int) mLogo.getX(),(int) mLogo.getY(), (int)mLogo.getX() ,0);
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

            animCard.setInterpolator(new OvershootInterpolator());

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


                }
            });


            final Button btn1 = (Button) rootView.findViewById(R.id.btn1);


            btn1.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {

//                    Login task = new Login(mContext, rootView);
//                    task.execute();

                    new socket.Login(mContext, rootView).execute();

//                    final Intent intent = new Intent(mContext, MenuActivity.class);
//                    final Intent intent2 = new Intent(mContext, FriendsActivity.class);
//
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                    Bundle b = new Bundle();
//
//                    b.putString("email", "sousandrei@gmail.com");
//                    b.putString("name", "Andrei");
//
//                    intent.putExtras(b);
//
//                    final View card = rootView.findViewById(R.id.card_view2);
//                    final View logo = rootView.findViewById(R.id.logoMain);

//                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((android.app.Activity) mContext,
//                            Pair.create(logo, "logo"),
//                            Pair.create(card, "card"),
//                            Pair.create(card, "card2"));

//                    mContext.startActivity(intent, options.toBundle());

//                    mContext.startActivity(intent2);


                }
            });



            return null;
        }

        @Override
        protected void onPostExecute(String result) {



            mCard.setVisibility(View.GONE);

            animLogo.start();
            animCard.start();



        }
    }


}
