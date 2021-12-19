package com.AgungJmartAK.jmart_android;

import android.content.Context;
import android.content.SharedPreferences;

import com.AgungJmartAK.jmart_android.model.Account;

/**
 * This is class for managing login session.
 *
 * @author Agung Firmansyah
 */
public class SessionManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session user";

    public SessionManager(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(Account account){
        int id = account.id;
        editor.putInt(SESSION_KEY, id).commit();
    }

    public int getSession(){
        return sharedPreferences.getInt(SESSION_KEY,-1);
    }

    public void removeSession(){
        editor.putInt(SESSION_KEY,-1).commit();
    }

}
