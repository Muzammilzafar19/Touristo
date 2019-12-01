package com.ecomway.touristo.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public SharePref(Context context) {
        prefs = context.getSharedPreferences("Touristo", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void setImagePath(String path) {
        editor.putString("imagePath", path);
        editor.commit();
    }
    public String getImagePath()
    {
        return prefs.getString("imagePath","");
    }
    public void setName(String name)
    {
        editor.putString("name", name);
        editor.commit();
    }
    public String getName()
    {
        return prefs.getString("name","");
    }
    public void setUserName(String username)
    {
        editor.putString("username", username);
        editor.commit();
    }
    public String getUserName()
    {
        return prefs.getString("username","");
    }
    public void setGender(String gender)
    {
        editor.putString("gender", gender);
        editor.commit();
    }
    public String getGender()
    {
        return prefs.getString("gender","");
    }
    public void setCountry(String country)
    {
        editor.putString("country", country);
        editor.commit();

    }
    public String getCountry()
    {
        return prefs.getString("country","");
    }
    public void setAge(String age)
    {
        editor.putString("age", age);
        editor.commit();
    }
    public String getAge()
    {
        return prefs.getString("age","");
    }
    public void setUserId(String id)
    {
        editor.putString("userid", id);
        editor.commit();
    }
    public String getUserId()
    {
        return prefs.getString("userid","");
    }
    public void setLoginIndicator(String yes)
    {
        editor.putString("login", yes);
        editor.commit();
    }
    public String getLoginIndicator()
    {
        return prefs.getString("login","");
    }
    public void setEmail(String email)
    {
        editor.putString("email", email);
        editor.commit();
    }
    public String getEmail()
    {
        return prefs.getString("email","");
    }

}
