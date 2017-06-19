package gokhangobus.projectpushtotalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by User on 4.05.2017.
 */
public class SettingPTT extends Activity {

    Button Save;
    Button Default;
    Button Back;
    TextView Serverip;
    TextView LoginRegisterPort;
    TextView TcpBridgePort;
    TextView TcpBridgePort2;
    TextView UdpSenderPort;
    TextView UdpReceiverPort;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_layout);

        Serverip=(TextView) findViewById(R.id.editserverip);
        LoginRegisterPort=(TextView) findViewById(R.id.editloginregisterport);
        TcpBridgePort=(TextView) findViewById(R.id.edittcpbridgeport);
        TcpBridgePort2=(TextView) findViewById(R.id.edittcpbridgeport2);
        UdpSenderPort=(TextView) findViewById(R.id.editudpsenderport);
        UdpReceiverPort=(TextView) findViewById(R.id.editudpreceiverport);

        Serverip.setText(SharedPref.getServerIp(getApplicationContext()));
        LoginRegisterPort.setText(SharedPref.getLoginRegisterPort(getApplicationContext()));
        TcpBridgePort.setText(SharedPref.getTcpBridgePort(getApplicationContext()));
        TcpBridgePort2.setText(getApplicationContext().getString(R.string.TcpBridgePort2));
        UdpSenderPort.setText(SharedPref.getUdpSenderPort(getApplicationContext()));
        UdpReceiverPort.setText(SharedPref.getUdpReceiverPort(getApplicationContext()));

        Save=(Button) findViewById(R.id.buttonsettingsave);
        Save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Save button
                SharedPref.setServerIp(getApplicationContext(),Serverip.getText().toString());
                SharedPref.setLoginRegisterPort(getApplicationContext(),LoginRegisterPort.getText().toString());
                SharedPref.setTcpBridgePort(getApplicationContext(),TcpBridgePort.getText().toString());
                SharedPref.setTcpBridgePort2(getApplicationContext(),TcpBridgePort2.getText().toString());
                SharedPref.setUdpSenderPort(getApplicationContext(),UdpSenderPort.getText().toString());
                SharedPref.setUdpReceiverPort(getApplicationContext(),UdpReceiverPort.getText().toString());
            }
        });
        Default=(Button) findViewById(R.id.buttonsettingsetdefault);
        Default.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Default button
                Serverip.setText(getApplicationContext().getString(R.string.ServerIp));
                LoginRegisterPort.setText(getApplicationContext().getString(R.string.TcpLoginRegisterPort));
                TcpBridgePort.setText(getApplicationContext().getString(R.string.TcpBridgePort));
                TcpBridgePort2.setText(getApplicationContext().getString(R.string.TcpBridgePort2));
                UdpSenderPort.setText(getApplicationContext().getString(R.string.UdpSenderPort));
                UdpReceiverPort.setText(getApplicationContext().getString(R.string.UdpReceiverPort));
            }
        });
        Back=(Button) findViewById(R.id.buttonsettingback);
        Back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v){
                //Back button
                Intent i = new Intent(getApplicationContext(), Main.class);
                startActivity(i);
            }
        });

    }
}
