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
        private const int MOUSEEVENTF_WHEEL = 0x800;

        BluetoothListener bluetoothListener;
        BluetoothClient conn;
        Stream mStream;

        Thread bluetoothServerThread;

        bool connected;

        int mouseX;
        int mouseY;
        

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            connected = false;
            this.Cursor = new Cursor(Cursor.Current.Handle);
            mouseX = Cursor.Position.X;
            mouseY = Cursor.Position.Y;
        }

        private void btnStart_Click(object sender, EventArgs e)
        {
            updateUI("Timer start broadcast");
            connectAsServer();
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

            //Stream mStream = conn.GetStream();
            mStream = conn.GetStream();

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
                    updateUI("Client disconnected");
                    disconnectBluetooth();
                }
            }
        }

        private void handleBluetoothInput(string input)
        {
            //updateUI(input);
            if(input.Contains("scrollUp"))
            {
                mouse_event(MOUSEEVENTF_WHEEL, 0, 0, 10, 0);
            }
            else if(input.Contains("scrollDown"))
            {
                mouse_event(MOUSEEVENTF_WHEEL, 0, 0, -10, 0);
            }
            else if (input.Contains("Lclick"))
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
                }
                catch (Exception e)
                {
                    
                }
            }
        }

        private void disconnectBluetooth()
        {
            connected = false;
            updateUI("Client disconnected");

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
                listOutput.SelectedIndex = listOutput.Items.Count - 1;
                listOutput.SelectedIndex = -1;
                return 0;
            };
            Invoke(del);
        }
    }
}
