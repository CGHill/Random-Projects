using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Runtime.InteropServices;
using System.Threading;
using InTheHand;
using InTheHand.Net.Bluetooth;
using InTheHand.Net.Ports;
using InTheHand.Net.Sockets;
using System.IO;

namespace BluetoothMouseController
{
    public partial class Form1 : Form
    {
        [DllImport("user32.dll", CharSet = CharSet.Auto, CallingConvention = CallingConvention.StdCall)]
        public static extern void mouse_event(int dwFlags, int dx, int dy, int cButtons, int dwExtraInfo);

        private const int MOUSEEVENTF_LEFTDOWN = 0x02;
        private const int MOUSEEVENTF_LEFTUP = 0x04;
        private const int MOUSEEVENTF_RIGHTDOWN = 0x08;
        private const int MOUSEEVENTF_RIGHTUP = 0x10;

        BluetoothListener bluetoothListener;
        BluetoothClient conn;

        Thread bluetoothServerThread;

        //bool changeInMouse;
        bool connected;
        bool connecting;

        int mouseX;
        int mouseY;
        

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            connecting = false;
            connected = false;
            //changeInMouse = false;
            this.Cursor = new Cursor(Cursor.Current.Handle);
            mouseX = Cursor.Position.X;
            mouseY = Cursor.Position.Y;
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            updateUI("Timer start broadcast");
            connecting = true;
            connectAsServer();
            //timer1.Enabled = true;
        }

        private void timer1_Tick(object sender, EventArgs e)
        {
            /*if (!connecting && !connected)
            {
                updateUI("Timer start broadcast");
                connecting = true;
                connectAsServer();
            }*/
            /*if (changeInMouse)
            {
                this.Cursor = new Cursor(Cursor.Current.Handle);
                Cursor.Position = new Point(mouseX, mouseY);
                changeInMouse = false;
            }*/
        }

        public void doMouseClick()
        {
            int X = Cursor.Position.X;
            int Y = Cursor.Position.Y;
            mouse_event(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, X, Y, 0, 0);
        }

        private void connectAsServer()
        {
            try
            {
                bluetoothServerThread = new Thread(new ThreadStart(ServerConnectThread));
                bluetoothServerThread.Start();
            }
            catch (ThreadStartException e)
            {
                updateUI("Thread failed to start");
                connecting = false;
            }
        }

        Guid mUUID = new Guid("00001101-0000-1000-8000-00805F9B34FB");

        public void ServerConnectThread()
        {
            updateUI("Server started, waiting for client");
            bluetoothListener = new BluetoothListener(mUUID);
            bluetoothListener.Start();
            conn = bluetoothListener.AcceptBluetoothClient();

            updateUI("Client has connected");
            connected = true;
            connecting = false;

            Stream mStream = conn.GetStream();

            while (connected)
            {
                try
                {
                    byte[] received = new byte[1024];
                    mStream.Read(received, 0, received.Length);
                    string receivedString = Encoding.ASCII.GetString(received);
                    //updateUI("Received: " + receivedString);
                    handleBluetoothInput(receivedString);
                    //byte[] send = Encoding.ASCII.GetBytes("Hello world");
                    //mStream.Write(send, 0, send.Length);
                }
                catch (IOException e)
                {
                    connected = false;
                    connecting = false;
                    updateUI("Client disconnected");
                    disconnectBluetooth();
                }
            }
        }

        private void handleBluetoothInput(string input)
        {
            if (input.Contains("Lclick"))
            {
                mouse_event(MOUSEEVENTF_LEFTDOWN | MOUSEEVENTF_LEFTUP, mouseX, mouseY, 0, 0);
            }
            else if (input.Contains("Rclick"))
            {
                mouse_event(MOUSEEVENTF_RIGHTDOWN | MOUSEEVENTF_RIGHTUP, mouseX, mouseY, 0, 0);
            }
            else
            {
                try
                {
                    string[] splitResults = input.Split(',');
                    int xDiff = Convert.ToInt32(splitResults[0]);
                    int yDiff = Convert.ToInt32(splitResults[1]);
                    mouseX -= xDiff;
                    mouseY -= yDiff;

                    changeMouse(xDiff, yDiff);
                   
                    //changeInMouse = true;
                    
                }
                catch (Exception e)
                {
                    connected = false;
                    connecting = false;
                    updateUI("Client disconnected");
                    disconnectBluetooth();
                }
            }
        }

        private void disconnectBluetooth()
        {
            conn.Close();
            conn = null;

            bluetoothListener.Stop();
            bluetoothListener = null;

            bluetoothServerThread.Abort();
        }

        private void changeMouse(int x, int y)
        {
            Func<int> del = delegate()
            {
                this.Cursor = new Cursor(Cursor.Current.Handle);
                Cursor.Position = new Point(mouseX, mouseY);
                return 0;
            };
            Invoke(del);
        }

        private void updateUI(string message)
        {
            Func<int> del = delegate()
            {
                listOutput.Items.Add(message);
                return 0;
            };
            Invoke(del);
        }
    }
}
