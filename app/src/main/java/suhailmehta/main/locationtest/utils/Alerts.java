package suhailmehta.main.locationtest.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import suhailmehta.main.locationtest.R;

/**
 * Created by suhailmehta on 24/03/15.
 */
public class Alerts {

    public static void showGPSDisabledAlert(final Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getResources().getString(R.string.location_disable))
                .setIcon(R.drawable.ic_launcher)
                .setMessage(
                        context.getResources().getString(R.string.location_disable_body))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.enable_gps),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton(context.getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        ((Activity)context).finish();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public static void showNetworkNotAvailableAlert(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder
                .setTitle(context.getResources().getString(R.string.connection_error))
                .setIcon(R.drawable.ic_launcher)
                .setMessage(
                        context.getResources().getString(R.string.connection_error_body))
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                // TODO : No case was mentioned in the document for this situation
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

}
