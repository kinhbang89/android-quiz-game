package fi.metropolia.translatorskeleton.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Bang on 29/04/16.
 */
public class SharedPrefManager {

    private static SharedPrefManager instance;
    private static Context context;
    private SharedPreferences sharedPref;

    private SharedPrefManager(Context context){
        this.context = context;
    }

    public static SharedPrefManager getInstance(Context context){
        if (instance == null)
            instance = new SharedPrefManager(context);
        return instance;
    }

    public static <T> void saveToPref(T obj, String prefName, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(prefName,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Gson gson = new Gson();
        String objString = gson.toJson(obj);
        editor.putString(key, objString);

        editor.commit();
    }

    public static <T> T loadFromPref(final Class<T> aClass,String prefName, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        T temp = null;
        System.out.println("weird thing is about" + key);
        if (sharedPref.contains(key)){
            String jsonDict = sharedPref.getString(key, "");
            Gson gson = new Gson();
            temp = gson.fromJson(jsonDict,aClass);
        }
        return  temp;
    }

}
