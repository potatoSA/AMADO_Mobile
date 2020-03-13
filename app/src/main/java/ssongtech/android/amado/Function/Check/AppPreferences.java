package ssongtech.android.amado.Function.Check;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class AppPreferences {

    public static final String email = "email";
    public static final String gender = "gender";
    public static final String nick = "nick";
    public static final String age = "age";
    public static final String value = "value";
    public static final String key = "key";
    public static final String type = "type";
    public static final String provider = "provider";

    public static Context context;

    public static SharedPreferences _sharedPrefs;
    public static SharedPreferences.Editor _prefsEditor;

    public AppPreferences(Context context) {
        this._sharedPrefs = context.getSharedPreferences("Data", Activity.MODE_PRIVATE);
        this._prefsEditor = _sharedPrefs.edit();
    }



    public String Sharde_getEmail() {
        return _sharedPrefs.getString(email, "");
    }
    public String Sharde_getGender() {
        return _sharedPrefs.getString(gender, "");
    }
    public String Sharde_getNick() {
        return  _sharedPrefs.getString(nick, "");
    }
    public String Sharde_getAge() {
        return  _sharedPrefs.getString(age, "");
    }
    public String Sharde_getValue() {
        return  _sharedPrefs.getString(value, "");
    }
    public String Sharde_getKey() {
        return  _sharedPrefs.getString(key, "");
    }
    public String Sharde_getType(){
        return  _sharedPrefs.getString(type, "");
    }
    public String Sharde_getProvideer(){
        return  _sharedPrefs.getString(provider, "");
    }

    public void Sharde_Del(){
        this._prefsEditor = _sharedPrefs.edit();
        _prefsEditor.clear();
        _prefsEditor.commit();
    }

    public void Sharde_setEmail(String getEmail) {
        _prefsEditor.putString(email, getEmail);
        _prefsEditor.commit();
    }


    public void Sharde_setGender(String getGender) {
        _prefsEditor.putString(gender, getGender);
        _prefsEditor.commit();
    }

    public void Sharde_setNick(String getNick) {
        _prefsEditor.putString(nick, getNick);
        _prefsEditor.commit();
    }

    public void Sharde_setAge(String getAge) {
        _prefsEditor.putString(age, getAge);
        _prefsEditor.commit();
    }

    public void Sharde_setValue(String getValue) {
        _prefsEditor.putString(value, getValue);
        _prefsEditor.commit();
    }

    public void Sharde_setKey(String getKey) {
        _prefsEditor.putString(key, getKey);
        _prefsEditor.commit();
    }

    public void Sharde_setType(String getType) {
        _prefsEditor.putString(type, getType);
        _prefsEditor.commit();
    }


    public void Sharde_setProvider(String getProvider) {
        _prefsEditor.putString(provider, getProvider);
        _prefsEditor.commit();
    }




}
