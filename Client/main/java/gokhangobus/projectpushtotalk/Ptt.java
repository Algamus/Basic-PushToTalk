package gokhangobus.projectpushtotalk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 8.05.2017.
 */
public class Ptt extends Activity {

    private Context context;

    private Button ButtonSpeak;
    private Button ButtonLogOut;
    private TextView onlinebox;
    private TextView user;

    Thread TcpBridge;
    Thread TcpBridge2;
    Thread UdpSender;
    Thread UdpReceiver;

    TheHandler PttHandler;

    public static boolean speaking;
    public static boolean listening;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pttlayout);
        context=this;
        PttHandler = new TheHandler(this);
        onlinebox = (TextView) findViewById(R.id.OnlineList);
        user = (TextView) findViewById(R.id.usernamebox);
        user.setText(SharedPref.getUserName(context));
        initilizer();

        ButtonLogOut = (Button) findViewById(R.id.buttonlogout);
        ButtonLogOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //LogOut button
                //thread recv stop signal
                //connection stop signalling
                gokhangobus.projectpushtotalk.TcpBridge.stopthethread();
                if(UdpSender!=null){
                    if(UdpSender.isAlive()) {
                        stopSenderUdp();
                    }
                }
                if(UdpReceiver!=null){
                    if(UdpReceiver.isAlive()) {
                        stopReceiverUdp();
                    }
                }
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);


            }
        });



        ButtonSpeak = (Button) findViewById(R.id.buttonspeak);
        ButtonSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch ( motionEvent.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        //
                        speaking=true;
                        gokhangobus.projectpushtotalk.TcpBridge.setState(1);
                        break;

                    case MotionEvent.ACTION_UP:
                        //
                        gokhangobus.projectpushtotalk.TcpBridge.setState(3);
                        speaking=false;
                        break;
                }
                return true;
            }
        });




    }




    private void initilizer(){

        speaking=false;
        listening=false;

        TcpBridge = new Thread(new TcpBridge(context,PttHandler,1));//firstface
        TcpBridge2 = new Thread(new TcpBridge(context,PttHandler,2));//Secondface



//        UdpReceiver = new Thread(new TcpLogin());

        TcpBridge.start();
        TcpBridge2.start();


    }

    public void setButtonSpeakVisibility(Boolean b){
        ButtonSpeak.setEnabled(b);
    }
    public void setButtonColor(int i){
        ButtonSpeak.setBackgroundColor(i);

    }
    public void startSenderUdp(){
        UdpSender = new Thread(new SenderUdp(context));
        UdpSender.start();
    }
    public void stopSenderUdp(){
        SenderUdp.stopthethread();
        try {
            if(UdpSender!=null){
                if(UdpSender.isAlive()) {
                    UdpSender.join();
                }}
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void startReceiverUdp(){
        UdpReceiver = new Thread(new ReceiverUdp(context));
        UdpReceiver.start();
    }
    public void stopReceiverUdp(){
        ReceiverUdp.stopthethread();
    }

    public static class TheHandler extends Handler {

        public static final int STATE_SPEAKING_1 = 0;
        public static final int STATE_SPEAKING_2 = 1;
        public static final int STATE_SPEAKING_3 = 2;
        public static final int STATE_SPEAKING_4 = 3;
        public static final int STATE_LISTEN_1 = 4;
        public static final int STATE_LISTEN_2 = 5;

        private Ptt parent;

        public TheHandler(Ptt parent) {
            super();
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case STATE_SPEAKING_1://gidenkonusma set up

                    parent.setButtonColor(Color.YELLOW);

                    break;
                case STATE_SPEAKING_2:

                    parent.startSenderUdp();
                    parent.setButtonColor(Color.GREEN);
                    break;
                case STATE_SPEAKING_3:

                    parent.setButtonColor(Color.WHITE);
                    parent.stopSenderUdp();
                    break;
                case STATE_SPEAKING_4://gelen konusma var

                    parent.setButtonColor(Color.RED);
                    break;
                case STATE_LISTEN_1://gelen konusma

                    parent.setButtonSpeakVisibility(false);
                    parent.setButtonColor(Color.RED);
                    parent.startReceiverUdp();
                    break;
                case STATE_LISTEN_2://gelen konusma var

                    parent.stopReceiverUdp();
                    parent.setButtonColor(Color.WHITE);
                    parent.setButtonSpeakVisibility(true);
                    break;
                default:
                    super.handleMessage(msg);
            }

        }

    }

}
