package com.foodapp.orderapp.Utils;

import android.app.Dialog;

import com.foodapp.orderapp.R;

import static android.text.Html.fromHtml;

public class Util {

    public static String CITYID="1";  //// Ghaziabad
    ///////////////show progress dialog for Async Task
    public static void showPgDialog(Dialog dialog) {

        dialog.setContentView(R.layout.dialogprogress);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();


//        progressDialog.setMessage("Please Wait....");
//        progressDialog.show();
    }

    public static void cancelPgDialog(Dialog dialog) {
//        if (progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

    }


}

