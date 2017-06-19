package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import gokhangobus.projectpushtotalk.SharedPref;

/**
 * Created by User on 4.05.2017.
 */
public class TcpLogin extends Thread {

    Context context;

    String Username;
    String Password;
    String ServerIp;
    int ServerPort;

    Socket s; //socket

    OutputStream opt;//out to server
    DataOutputStream out;

    InputStream inFromServer; //in from server
    BufferedReader in;


    TcpLogin(Context c,String user,String psw){
        context = c;
        Username=user;
        Password=psw;
        ServerIp= SharedPref.getServerIp(c);
        ServerPort = Integer.parseInt(SharedPref.getLoginRegisterPort(c));

    }

    public void run() {

        try {
            s = new Socket(ServerIp, ServerPort);

            opt = s.getOutputStream();
            out = new DataOutputStream(opt);

            out.writeUTF("Login"+"@"+Username+"@"+Password);//sending login@username@password

            inFromServer = s.getInputStream();
            in = new BufferedReader( new InputStreamReader(inFromServer));


            Main.infotext=in.readLine();


            inFromServer.close();
            in.close();
            out.close();
            opt.close();
            s.close();

        }catch (Exception e){

        }

    }
}
