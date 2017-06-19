package gokhangobus.projectpushtotalk;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Main extends AppCompatActivity {

    Button Login;
    Button Register;
    Button Setting;
    TextView Username;
    TextView Password;
    TextView info;

    static String infotext;

    Thread thread;
    Context c;
  //  Static

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Username=(TextView) findViewById(R.id.theusername);
        Password=(TextView) findViewById(R.id.thepassword);
        info=(TextView) findViewById(R.id.informationbox);

        Username.setText(SharedPref.getUserName(this));

        c=this;//this problemi

        Login=(Button) findViewById(R.id.buttonlogin);
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Login button

                thread = new Thread(new TcpLogin(c,Username.getText().toString(),Password.getText().toString()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(infotext.equals("OK")){
                    info.setText("Login Successfull...");
                    SharedPref.setUserName(c,Username.getText().toString());
                    Intent i = new Intent(getApplicationContext(), Ptt.class);
                    startActivity(i);
                }else if(infotext.equals("NOK")){
                    info.setText("UserName or Password is not correct...");
                }else{
                    info.setText("Error: "+infotext);
                }
                infotext="";

            }
        });
        Register=(Button) findViewById(R.id.buttonregister);
        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Register button
                thread = new Thread(new TcpRegister(c,Username.getText().toString(),Password.getText().toString()));
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(infotext.equals("OK")){
                    info.setText("Register Successfull...");
                }else if(infotext.equals("CLONE")){
                    info.setText("UserName already exists.");
                }else{
                    info.setText("Error: "+infotext);
                }
                infotext="";
            }
        });
        Setting=(Button) findViewById(R.id.buttonsetting);
        Setting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Setting button
                Intent i = new Intent(getApplicationContext(), SettingPTT.class);
                startActivity(i);
            }
        });
    }



   /* private void gogo2(){
            Intent i = new Intent(getApplicationContext(), SendVoice.class);
            startActivity(i);
 Thread thread = new Thread(new TheThread(this,t));
thread.start();
    }*/
}

