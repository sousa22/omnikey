package com.sec.omnium.omnikey.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

/**
 * Created by Usuario on 17/02/2015.
 */
public class Utils {

    public static void alert(final Context context, final String msg, final String title) {
        new Thread(){
            public void run() {
                ((Activity)context).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                    // 1. Instantiate an AlertDialog.Builder with its constructor
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    // 2. Chain together various setter methods to set the dialog characteristics
                    builder.setTitle(title);
                    builder.setMessage("" + msg);

//                    builder.setNegativeButton("negative", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    });
//
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });
//
//                    builder.setPositiveButton("positive", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//
//                        }
//                    });


                    // 3. Get the AlertDialog from create()
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    }
                });
            }
        }.start();
    }
//

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }


}
