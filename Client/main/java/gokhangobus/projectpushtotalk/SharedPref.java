package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import static android.content.SharedPreferences.*;

/**
 * Created by User on 4.05.2017.
 */
public class SharedPref {


    public static String getServerIp(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String serverıp = mSharedPrefs.getString("ServerIp", c.getString(R.string.ServerIp));
        return serverıp;
    }
    public static void setServerIp(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("ServerIp", s );
        mPrefsEditor.commit();
    }


    public static String getLoginRegisterPort(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String port = mSharedPrefs.getString("LoginRegisterPort", c.getString(R.string.TcpLoginRegisterPort));
        return port;
    }
    public static void setLoginRegisterPort(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("LoginRegisterPort", s );
        mPrefsEditor.commit();
    }


    public static String getTcpBridgePort(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String port = mSharedPrefs.getString("BridgePort", c.getString(R.string.TcpBridgePort));
        return port;
    }
    public static void setTcpBridgePort(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("BridgePort", s );
        mPrefsEditor.commit();
    }

    public static String getTcpBridgePort2(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String port = mSharedPrefs.getString("BridgePort2", c.getString(R.string.TcpBridgePort2));
        return port;
    }
    public static void setTcpBridgePort2(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("BridgePort2", s );
        mPrefsEditor.commit();
    }


    public static String getUdpSenderPort(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String port = mSharedPrefs.getString("UdpSenderPort", c.getString(R.string.UdpSenderPort));
        return port;
    }
    public static void setUdpSenderPort(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("UdpSenderPort", s );
        mPrefsEditor.commit();
    }


    public static String getUdpReceiverPort(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String port = mSharedPrefs.getString("UdpReceiverPort", c.getString(R.string.UdpReceiverPort));
        return port;
    }
    public static void setUdpReceiverPort(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("UdpReceiverPort", s );
        mPrefsEditor.commit();
    }


    public static String getUserName(Context c) {

        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        String uname = mSharedPrefs.getString("TheUserName","");
        return uname;
    }
    public static void setUserName(Context c,String s){
        SharedPreferences mSharedPrefs = c.getSharedPreferences("SettingFile",c.MODE_PRIVATE);
        Editor mPrefsEditor = mSharedPrefs.edit();
        mPrefsEditor.putString("TheUserName", s );
        mPrefsEditor.commit();
    }
}
