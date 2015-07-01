package bit.hillcg2.bluetoothmouse;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;
import java.util.UUID;
import android.view.View.OnDragListener;


public class MainActivity extends ActionBarActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private ArrayList<BluetoothDevice> deviceList;
    private BluetoothAdapter BTAdapter;
    private ConnectedThread connectionThread;
    private RelativeLayout mainLayout;

    private float oldX;
    private float oldY;

    private Button btnleftClick;
    private Button btnRightClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        mainLayout = (RelativeLayout)findViewById(R.id.mainLayout);
        mainLayout.setOnTouchListener(new touchHandle());

        btnleftClick = (Button)findViewById(R.id.btnLeftClick);
        btnleftClick.setOnClickListener(new doLeftClick());

        btnRightClick = (Button)findViewById(R.id.btnRightClick);
        btnRightClick.setOnClickListener(new doRightClick());

        BTAdapter = BluetoothAdapter.getDefaultAdapter();

        if (BTAdapter == null) {
            Toast.makeText(this, "Device does not support bluetooth", Toast.LENGTH_LONG).show();
            finish();
        }

        if (!BTAdapter.isEnabled()) {
            Intent enableBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBT, REQUEST_ENABLE_BT);
        }

        deviceList = new ArrayList<BluetoothDevice>();


        Set<BluetoothDevice> pairedDevices = BTAdapter.getBondedDevices();
        // If there are paired devices
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                deviceList.add(device);
            }
        }

        for(BluetoothDevice curr : deviceList)
        {
            if(curr.getName().equals("CAMHILL-PC"))
            {
                Toast.makeText(getBaseContext(), "Connecting to " + curr.getName(), Toast.LENGTH_LONG).show();
                ConnectThread connectThread = new ConnectThread(curr);
                connectThread.run();
            }
        }
    }

    public class doLeftClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            connectionThread.write("Lclick");
        }
    }

    public class doRightClick implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            connectionThread.write("Rclick");
        }
    }

    public class touchHandle implements View.OnTouchListener{
        private static final int MAX_CLICK_DURATION = 200;
        private long startClickTime;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();

            switch(action)
            {
                case MotionEvent.ACTION_DOWN:
                    oldX = event.getX();
                    oldY = event.getY();

                    startClickTime = Calendar.getInstance().getTimeInMillis();
                    break;
                case MotionEvent.ACTION_UP:
                    long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;

                    if(clickDuration < MAX_CLICK_DURATION)
                        connectionThread.write("Lclick");

                    break;
                case MotionEvent.ACTION_MOVE:
                    float differenceX = oldX - event.getX();
                    float differenceY = oldY - event.getY();

                    int roundedX = Math.round(differenceX);
                    int roundedY = Math.round(differenceY);

                    String differenceString = String.valueOf(roundedX) + "," + String.valueOf(roundedY) + ", junk";

                    oldX = event.getX();
                    oldY = event.getY();

                    connectionThread.write(differenceString);
                    break;
            }
            return true;
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes; // bytes returned from read()

            while (true) {
                try {
                    bytes = mmInStream.read(buffer);

                    //send bytes to UI
                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String message) {
            try {
                byte[] bytes = message.getBytes();
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            mmDevice = device;

            try {
                tmp = mmDevice.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) {
            }
            mmSocket = tmp;
        }

        public void run() {
            BTAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) {
                }
                return;
            }
            connectionThread = new ConnectedThread(mmSocket);

            //Freezes program
            // connectionThread.run();

            //cancel();
        }

        /**
         * Will cancel an in-progress connection, and close the socket
         */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode != RESULT_OK) {
                Toast.makeText(this, "App needs bluetooth", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}

