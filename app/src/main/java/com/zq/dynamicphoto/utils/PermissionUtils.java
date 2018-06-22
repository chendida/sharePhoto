package com.zq.dynamicphoto.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;

import com.zq.dynamicphoto.R;

/**
 * Created by Administrator on 2018/6/22.
 */

public class PermissionUtils {
    public static void showSeePermissionSelectDialog(Context context, final TextView tvWhoCanSee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context); //定义一个AlertDialog
        final String[] sees = {context.getResources().getString(R.string.everyone_can_see),
                context.getResources().getString(R.string.one_can_see)};
        builder.setItems(sees, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int which) {
                if (which == 0) {
                    tvWhoCanSee.setText(sees[which]);
                } else {
                    tvWhoCanSee.setText(sees[which]);
                }
            }
        });
        builder.show();
    }
}
