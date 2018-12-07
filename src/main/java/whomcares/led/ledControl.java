package whomcares.led;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.os.AsyncTask;

import java.io.IOException;
import java.util.UUID;


public class ledControl extends ActionBarActivity {
    public static final int MAX_SPEED = 9;
    Button forward, reverse, btnDis, servo;
    SeekBar leftBar, rightBar;
    TextView speed;
    String address = null;
    int leftTotal = MAX_SPEED; int rightTotal = MAX_SPEED;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS); //receive the address of the bluetooth device

        //view of the ledControl
        setContentView(R.layout.activity_led_control);

        //call the widgtes
        //forward = (Button)findViewById(R.id.button2);
        //reverse = (Button)findViewById(R.id.button3);
        servo = (Button)findViewById(R.id.button2);
        btnDis = (Button)findViewById(R.id.button4);
        leftBar = (SeekBar)findViewById(R.id.leftBar);
        rightBar = (SeekBar)findViewById(R.id.rightBar);
        speed = (TextView)findViewById(R.id.lumn);
        leftBar.setProgress(MAX_SPEED);
        rightBar.setProgress(MAX_SPEED);

        new ConnectBT().execute(); //Call the class to connect

        servo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveServo();
            }
        });

        btnDis.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Disconnect(); //close connection
            }
        });

        /**FOR THE FOLLOWING METHODS
         * SENDING THE INFORMATION TO THE BLUETOOTH STREAM IS CALLED TWICE
         * ONCE, THEN DELAY 1 MILLISECOND, THEN CALLED ONCE MORE
         * THIS IS TO ENSURE THE MICROCONTROLLER HAS TIME TO ACCOUNT FOR MANY BACK TO BACK INSTRUCTIONS
         * AS THIS DELAY IS ONLY 1 MILLISECOND,
         * IT STILL ALLOWS FOR MORE THAN 500 INSTRUCTIONS OT BE SENT PER SECOND
         */

        leftBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //called whenever the left seekbar moves
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    String toReturn = "";
                    leftTotal = (int)progress;
                    if (leftTotal == MAX_SPEED)
                    {
                        toReturn = "FL0";
                    }
                    else if(leftTotal > MAX_SPEED && leftTotal <= MAX_SPEED * 2)
                    {
                        toReturn = "FL" + String.valueOf((leftTotal - MAX_SPEED) * 20);
                    }
                    else if(leftTotal < MAX_SPEED && leftTotal >= 0)
                    {
                        toReturn = "BL" + String.valueOf((MAX_SPEED - leftTotal) * 20);
                    }
                    else {
                        msg("Error");
                    }
                    //speed.setText(toReturn + "   ");
                    speed.setText(String.valueOf(leftTotal - MAX_SPEED) + " " + String.valueOf(rightTotal - MAX_SPEED));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    try
                    {
                        btSocket.getOutputStream().write((toReturn + ":").toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");

                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    try
                    {
                        btSocket.getOutputStream().write((toReturn + ":").toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                leftBar.setProgress(MAX_SPEED);
                leftTotal = MAX_SPEED;
                speed.setText(String.valueOf(leftTotal - MAX_SPEED) + " " + String.valueOf(rightTotal - MAX_SPEED));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                try
                {
                    btSocket.getOutputStream().write("FL0:".toString().getBytes());
                    //msg("FL0");
                }
                catch(IOException e)
                {
                    //msg("Error");
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                try
                {
                    btSocket.getOutputStream().write("FL0:".toString().getBytes());
                    //msg("FL0");
                }
                catch(IOException e)
                {
                    //msg("Error");
                }
            }
        });
        rightBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser==true)
                {
                    String toReturn = "";
                    rightTotal = (int)progress;
                    if (rightTotal == MAX_SPEED)
                    {
                        toReturn = "FR0";
                    }
                    else if(rightTotal > MAX_SPEED && rightTotal <= MAX_SPEED * 2)
                    {
                        toReturn = "FR" + String.valueOf((rightTotal - MAX_SPEED) * 20);
                    }
                    else if(rightTotal < MAX_SPEED && rightTotal >= 0)
                    {
                        toReturn = "BR" + String.valueOf((MAX_SPEED - rightTotal) * 20);
                    }
                    else {
                        //msg("Error");
                    }
                    //speed.setText("   " + toReturn);
                    speed.setText(String.valueOf(leftTotal - MAX_SPEED) + " " + String.valueOf(rightTotal - MAX_SPEED));
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    try
                    {
                        //msg(toReturn);
                        btSocket.getOutputStream().write((toReturn + ":").toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");

                    }
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                    try
                    {
                        //msg(toReturn);
                        btSocket.getOutputStream().write((toReturn + ":").toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        msg("Error");

                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                rightBar.setProgress(MAX_SPEED);
                rightTotal = MAX_SPEED;
                speed.setText(String.valueOf(leftTotal - MAX_SPEED) + " " + String.valueOf(rightTotal - MAX_SPEED));
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                try
                {
                    btSocket.getOutputStream().write("FR0:".toString().getBytes());
                }
                catch(IOException e)
                {
                    msg("Error");
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                }
                try
                {
                    btSocket.getOutputStream().write("FR0:".toString().getBytes());
                }
                catch(IOException e)
                {
                    msg("Error");
                }
            }
        });
    }

    private void Disconnect()
    {
        if (btSocket!=null) //If the btSocket is busy
        {
            try
            {
                btSocket.close(); //close connection
            }
            catch (IOException e)
            { msg("Error");}
        }
        finish(); //return to the first layout

    }

    private void moveServo()
    {
        if(btSocket!= null)
        {
            try
            {
                btSocket.getOutputStream().write("SV00:".toString().getBytes());
            }
            catch(IOException e)
            {
                msg("Error");
            }
        }
    }

    // fast way to call Toast
    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(ledControl.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }
}
