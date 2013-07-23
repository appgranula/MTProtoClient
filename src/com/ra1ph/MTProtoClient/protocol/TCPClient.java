package com.ra1ph.MTProtoClient.protocol;

import android.os.AsyncTask;
import android.util.Log;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.functions.ReqPQ;
import com.ra1ph.MTProtoClient.tl.functions.ResPQ;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class TCPClient extends AsyncTask<Void, Void, Void> {

    private static final byte SET_SIMPLE_FORMAT = (byte) 0xEF;
    private final TLUtility tlUtility;

    private String serverMessage;
    public static final String SERVERIP = "95.142.192.65"; //your computer IP address
    public static final int SERVERPORT = 80;
    private AtomicBoolean mRun;
    OutputStreamWriter out;
    InputStream in;
    private boolean isSimpleProtocol = false;
    private OutputStream ou;

    public TCPClient() {
        mRun = new AtomicBoolean();
        tlUtility = TLUtility.getInstance();
    }

    public void stopClient() {
        mRun.set(false);
    }

    @Override
    protected Void doInBackground(Void... params) {

        mRun.set(true);

        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            Log.e("TCP Client", "C: Connecting...");
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, SERVERPORT);
            socket.setKeepAlive(true);
            try {
                //send the message to the server
                out = new OutputStreamWriter(socket.getOutputStream());
                ou = socket.getOutputStream();
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");
                //receive the message which the server sends back
                in = socket.getInputStream();
                //in this while the client listens for the messages sent by the server
                configureConntection(out);
                while (mRun.get()) {
                    if (socket.isConnected()) {
                        byte[] buffer = new byte[512];
                        int n = in.read(buffer);
                        Message m = new Message();
                        m.setMessageData(Arrays.copyOfRange(buffer, 0, n));
                        ResPQ res = (ResPQ) tlUtility.deserialize(m.getRawMessageData());
                        Log.d("myLog", TLUtility.bytesToHex(buffer));
                        Log.d("myLog","            ");
                        Log.d("myLog", TLUtility.bytesToHex(m.getRawMessageData()));
                    }
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                //the socket must be closed. It is not possible to reconnect to this socket
                // after it is closed, which means a new socket instance has to be created.
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void configureConntection(OutputStreamWriter out) {

        testRequest();
    }

    private void testRequest() {

        byte[] test = new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1d, 0x36, (byte) 0xe8, 0x51, 0x14, 0x00, 0x00, 0x00, 0x78, (byte) 0x97, 0x46, 0x60, 0x04, (byte) 0x91, 0x61, 0x61, (byte) 0x8e, 0x47, 0x6b, (byte) 0xbd, (byte) 0x82, 0x3b, 0x53, (byte) 0xfb, 0x09, 0x1a, (byte) 0x88, 0x00};
        byte[] test2 = new byte[]{0x34, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x1d, 0x36, (byte) 0xe8, 0x51, 0x14, 0x00, 0x00, 0x00, 0x78, (byte) 0x97, 0x46, 0x60, 0x04, (byte) 0x91, 0x61, 0x61, (byte) 0x8e, 0x47, 0x6b, (byte) 0xbd, (byte) 0x82, 0x3b, 0x53, (byte) 0xfb, 0x09, 0x1a, (byte) 0x88, 0x00, 0x5d, (byte) 0xbc, 0x15, 0x65};
        byte[] test3 = new byte[]{0x78, (byte) 0x97, 0x46, 0x60, 0x3E, 0x05, 0x49, (byte) 0x82, (byte) 0x8C, (byte) 0xCA, 0x27, (byte) 0xE9, 0x66, (byte) 0xB3, 0x01, (byte) 0xA4, (byte) 0x8F, (byte) 0xEC, (byte) 0xE2, (byte) 0xFC};

        try {
            /*ou.write(test2);
            ou.flush();   */

            /*Packet p = new Packet();
            p.setRawData(test,0);
            ou.write(p.getPacketData());
            ou.flush();     */

            ReqPQ reqPQ = new ReqPQ(new TLInteger128(new int[]{1,2,3,4}));
            byte[] req = reqPQ.serialize();
            Message m = new Message();
            m.setMessageBody(req, 0, 0);
            ou.write(m.getPacketData());
            ou.flush();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }
}