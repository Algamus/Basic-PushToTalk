package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by User on 9.05.2017.
 */
public class ReceiverUdp extends Thread {
    Context context;
    String username;
    String message;

    public byte[] bufferRaw;
    public byte[] bufferFlac;
    private String ServerIp;
    private int ServerPort;
    private int ServerPort2;
    private AudioTrack speaker;

    //Audio Configuration.
    private int rate;
    private int channelconfig;
    private int format;

    private static boolean flag;


    //
    DatagramSocket socket;
    DatagramPacket packet;



    ReceiverUdp(Context c){
        context=c;
        username=SharedPref.getUserName(context);
        message="L@"+username;
        ServerIp=SharedPref.getServerIp(context);
        ServerPort=Integer.parseInt(SharedPref.getUdpSenderPort(context));
        ServerPort2=Integer.parseInt(SharedPref.getUdpReceiverPort(context));
        //audio setup
        rate=8000;//44100, 22050, 11025, 8000
        channelconfig= AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        format= AudioFormat.ENCODING_PCM_16BIT;

    }

    public static void stopthethread(){
        flag=false;
    }
    public void run(){
        flag=true;
        try {
            Log.v("tag","connection");
            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.setBroadcast(true);
            socket.bind(new InetSocketAddress(ServerPort2));
            //new InetSocketAddress(ServerPort2);
            //socket = new DatagramSocket(ServerPort2);

            bufferRaw = new byte[256];
            int BufRawSize=256;//= AudioRecord.getMinBufferSize(rate,channelconfig,format);

            //connection signal
            InetAddress address= InetAddress.getByName(ServerIp);
            byte[] m = message.getBytes();
            packet = new DatagramPacket (m,m.length,address,ServerPort);
            socket.send(packet);
            Log.v("tag","Signaling");
            //

            speaker = new AudioTrack(AudioManager.STREAM_MUSIC,rate,channelconfig,format,BufRawSize*10,AudioTrack.MODE_STREAM);
            Log.v("tag","speakerready");
            speaker.play();
            Log.v("tag","sStart"+flag);

            while(flag){
                Log.v("tag","dongu");
                packet = new DatagramPacket(bufferRaw,bufferRaw.length);
                socket.receive(packet);
                bufferRaw=packet.getData();
                Log.v("tag","Goo"+bufferRaw.toString());
                speaker.write(bufferRaw, 0, BufRawSize);
            }
            speaker.stop();
            speaker.release();
            socket.close();
            Log.v("tag","bitti");
        } catch (SocketException e) {
            e.printStackTrace();
            Log.v("tag","e1");
            if(socket!=null){
                socket.close();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.v("tag","e2");
        } catch (IOException e) {
            e.printStackTrace();
            Log.v("tag","e3");
        }

        if(socket!=null) {
            if(!socket.isClosed()){
            socket.close();
        }}
    }
}
