package gokhangobus.projectpushtotalk;

import android.content.Context;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by User on 4.05.2017.
 */
public class TcpRegister extends Thread{
    Context context;

    String Username;
    String Password;
    String ServerIp;
    int ServerPort;

    Socket s;

    OutputStream opt;//out to server
    DataOutputStream out;

    InputStream ifs; //in from server
    BufferedReader in;


    TcpRegister(Context c , String user,String psw){
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

            out.writeUTF("Register" + "@" + Username + "@" + Password);//sending login@username@password

            ifs = s.getInputStream();
            in = new BufferedReader( new InputStreamReader(ifs));

            Main.infotext=in.readLine();

            s.close();
        }catch (Exception e){

        }
    }
}
