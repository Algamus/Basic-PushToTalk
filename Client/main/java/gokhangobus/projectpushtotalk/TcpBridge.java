package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.os.Message;
import android.util.Log;

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
public class TcpBridge extends  Thread{
    Context context;

    String Username;
    String Password;
    String ServerIp;
    int ServerPort;

    static Socket s;
    static Socket r;

    OutputStream opt;//out to server
    DataOutputStream out;

    InputStream inFromServer; //in from server
    BufferedReader in;


    private static boolean flag;

    Ptt.TheHandler handler;
    private static int state;
    private int x;

    String Answer;

    TcpBridge(Context c, Ptt.TheHandler handler,int x){
        context = c;
        this.handler = handler;
        Username=SharedPref.getUserName(context);
        ServerIp= SharedPref.getServerIp(c);
        if(x==1){
            ServerPort = Integer.parseInt(SharedPref.getTcpBridgePort(c));
        }else{
            ServerPort = Integer.parseInt(SharedPref.getTcpBridgePort2(c));
        }
        flag=true;
        state=0;
        this.x=x;

    }

    public static void stopthethread(){
        flag=false;
    }
    public static void setState(int s){//1-sender set up,3-senderfinish
        state=s;
    }





    public void run() {

        if(x==1) {//firstside maybesender
            try {
                s = new Socket(ServerIp, ServerPort);

                opt = s.getOutputStream();
                out = new DataOutputStream(opt);

                out.writeUTF("ConnectS" + "@" + Username);//sending

                inFromServer = s.getInputStream();
                in = new BufferedReader(new InputStreamReader(inFromServer));

                while (flag) {

                    if(Ptt.speaking==false && Ptt.listening==false){
                        //state=0;//sıkıntı
                    }

                    if (state == 0) {//idle


                    } else if (state == 1) {//ben konusuyorum
                        sleep(100);
                        out.writeUTF("WTSPEAK@G");
                        Answer=in.readLine();
                        if(Answer.equals("OKWAIT")){
                            state=2;
                        }else if(Answer.equals("NOK")){
                            handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_SPEAKING_4, ""));//gelen konusma var demek
                            state=0;
                        }else{
                            state=0;
                        }
                    } else if (state == 2 && Ptt.speaking) {
                        handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_SPEAKING_1, ""));//buttonsarı
                        Answer=in.readLine();
                        if(Answer.equals("START")){
                            //go speak
                            handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_SPEAKING_2, ""));
                            state=0;
                        }else{
                            //error

                        }
                    } else if (state == 3) {
                        out.writeUTF("STOPSPEAKING@G");
                        handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_SPEAKING_3, ""));
                        state=0;
                    } else {
                        //
                    }


                }


                inFromServer.close();
                in.close();
                out.close();
                opt.close();
                s.close();

            } catch (Exception e) {

            }
        }else if(x==2){//receiverside
            try {
                r = new Socket(ServerIp, ServerPort);

                opt = r.getOutputStream();
                out = new DataOutputStream(opt);

                out.writeUTF("ConnectR" + "@" + Username);//sending

                inFromServer = r.getInputStream();
                in = new BufferedReader(new InputStreamReader(inFromServer));

                while (flag) {

                    Answer=in.readLine();
                    if(Answer.equals("DONE")){
                        if(Ptt.listening){

                            handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_LISTEN_2, ""));
                            Ptt.listening=false;
                            state=0;
                        }
                    }else if(Answer.equals("LISTEN")){
                        if(Ptt.listening==false){
                            Ptt.listening=true;
                            handler.sendMessage(Message.obtain(handler,Ptt.TheHandler.STATE_LISTEN_1, ""));
                            out.writeUTF("LISTENOK@G");
                        }
                    }else{

                    }




                }


                inFromServer.close();
                in.close();
                out.close();
                opt.close();
                s.close();

            } catch (Exception e) {

            }


        }else{
            //error
        }
    }
}
