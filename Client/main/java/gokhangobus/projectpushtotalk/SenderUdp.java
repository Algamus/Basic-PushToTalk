package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

import javaFlacEncoder.FLACEncoder;
import javaFlacEncoder.FLAC_FileEncoder;
import javaFlacEncoder.StreamConfiguration;

//import javaFlacEncoder.FLAC_FileEncoder;
import java.io.File;
/**
 * Created by User on 8.05.2017.
 */
public class SenderUdp extends Thread{
    Context context;
    String username;
    String message;

    public byte[] bufferRaw;
    public byte[] bufferFlac;
    private String ServerIp;
    private int ServerPort;
    AudioRecord recorder;

    //Audio Configuration.
    private int rate;
    private int channelconfig;
    private int format;

    private static boolean flag;


    //
    DatagramSocket socket;
    DatagramPacket packet;



    SenderUdp(Context c){
        context=c;
        username=SharedPref.getUserName(context);
        message="S@"+username;
        ServerIp=SharedPref.getServerIp(context);
        ServerPort=Integer.parseInt(SharedPref.getUdpSenderPort(context));

        //audio setup
        rate=8000;//44100, 22050, 11025, 8000
        channelconfig=AudioFormat.CHANNEL_CONFIGURATION_STEREO;
        format= AudioFormat.ENCODING_PCM_16BIT;
    }

    public static void stopthethread(){
        flag=false;
    }

    public void run(){
        flag=true;
        try{
            int BufRawSize=256;//= AudioRecord.getMinBufferSize(rate,channelconfig,format);

            socket= new DatagramSocket();

            bufferRaw = new byte[BufRawSize];

            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC,rate,channelconfig,format,BufRawSize*10);

            //connection signal
            InetAddress address= InetAddress.getByName(ServerIp);
            byte[] m = message.getBytes();
            packet = new DatagramPacket (m,m.length,address,ServerPort);
            socket.send(packet);
            //
            recorder.startRecording();

            while(flag){

                recorder.read(bufferRaw,0,bufferRaw.length);
                packet = new DatagramPacket (bufferRaw,bufferRaw.length,address,ServerPort);
                socket.send(packet);

            }
            recorder.stop();
            recorder.release();
            socket.close();



        }catch (Exception e){

        }

    }



}
