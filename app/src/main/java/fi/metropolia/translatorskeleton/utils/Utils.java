package fi.metropolia.translatorskeleton.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;

import com.google.gson.Gson;

import fi.metropolia.translatorskeleton.R;
import fi.metropolia.translatorskeleton.model.Setting;

/**
 * Created by Bang on 24/04/16.
 */
public class Utils {

    public static AlertDialog.Builder alertDialogBuilder(String title, String message, Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        return builder;
    }

    public static void saveSettingToSp(Setting s, Context context) {
        String setting_pref_key = context.getResources().getString(R.string.setting_pref_key);
        String setting_key = context.getResources().getString(R.string.setting_key);

        SharedPreferences preferences = context.getSharedPreferences(
                setting_pref_key,
                android.content.Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(s);
        prefsEditor.putString(setting_key, json);

        prefsEditor.commit();
    }

    // Get Preference
    public static Setting loadSettingFromSp(Context context) {
        String setting_pref_key = context.getResources().getString(R.string.setting_pref_key);
        String setting_key = context.getResources().getString(R.string.setting_key);

        SharedPreferences preferences = context.getSharedPreferences(
                setting_pref_key,
                android.content.Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = preferences.getString(setting_key, "");
        Setting obj = gson.fromJson(json, Setting.class);
        return obj;
    }

}
