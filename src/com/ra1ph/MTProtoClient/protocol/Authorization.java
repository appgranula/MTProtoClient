package com.ra1ph.MTProtoClient.protocol;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import com.ra1ph.MTProtoClient.crypto.*;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;
import com.ra1ph.MTProtoClient.tl.functions.ReqPQ;
import com.ra1ph.MTProtoClient.tl.functions.ResPQ;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 18.07.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class Authorization {

    private final OutputStreamWriter out;

    public Authorization(OutputStreamWriter out) {
        this.out = out;
    }

    public void getAuthKey(int i) {

    }

    public static String getPQ(String rqLong) {
        PollardRho.factor(new BigInteger(rqLong));
        return PollardRho.divider.toString();
    }

    public static void testAuth() {
        Socket socket = getSocket();

        TLUtility tlUtility = TLUtility.getInstance();

        int nTCP = 0;
        if (socket != null) {
            try {
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                byte[] buffer = new byte[1024];
                int n=0;



                /*String test = "00 00 00 00 00 00 00 00 00 00 00 00 C9 7A E5 51 40 01 00 00 BE E4 12 D7 3E 05 49 82 8C CA 27 E9 66 B3 01 A4 8F EC E2 FC A5 CF 4D 33 F4 A1 1E A8 77 BA 4A A5 73 90 73 30 04 49 4C 55 3B 00 00 00 04 53 91 10 73 00 00 00 21 6B E8 6C 02 2B B4 C3 FE 00 01 00 7B B0 10 0A 52 31 61 90 4D 9C 69 FA 04 BC 60 DE CF C5 DD 74 B9 99 95 C7 68 EB 60 D8 71 6E 21 09 BA F2 D4 60 1D AB 6B 09 61 0D C1 10 67 BB 89 02 1E 09 47 1F CF A5 2D BD 0F 23 20 4A D8 CA 8B 01 2B F4 0A 11 2F 44 69 5A B6 C2 66 95 53 86 11 4E F5 21 1E 63 72 22 7A DB D3 49 95 D3 E0 E5 FF 02 EC 63 A4 3F 99 26 87 89 62 F7 C5 70 E6 A6 E7 8B F8 36 6A F9 17 A5 27 26 75 C4 60 64 BE 62 E3 E2 02 EF A8 B1 AD FB 1C 32 A8 98 C2 98 7B E2 7B 5F 31 D5 7C 9B B9 63 AB CB 73 4B 16 F6 52 CE DB 42 93 CB B7 C8 78 A3 A3 FF AC 9D BE A9 DF 7C 67 BC 9E 95 08 E1 11 C7 8F C4 6E 05 7F 5C 65 AD E3 81 D9 1F EE 43 0A 6B 57 6A 99 BD F8 55 1F DB 1B E2 B5 70 69 B1 A4 57 30 61 8F 27 42 7E 8A 04 72 0B 49 71 EF 4A 92 15 98 3D 68 F2 83 0C 3E AA 6E 40 38 55 62 F9 70 D3 8A 05 C9 F1 24 6D C3 34 38 E6";
                byte[] arr = TLUtility.hexStringToByteArray(test);
                Packet p = new Packet();
                p.setRawData(arr,0);*/


                ReqPQ reqPQ = new ReqPQ(new TLInteger128(new int[]{1,2,3,4}));
                byte[] req = reqPQ.serialize();
                Message m2 = new Message();
                m2.setMessageBody(req, nTCP, 0);
                out.write(m2.getPacketData());
                out.flush();
                nTCP++;

                out.write(m2.getPacketData());
                out.flush();

                n = in.read(buffer);
                Log.d("myLog", TLUtility.bytesToHex(buffer));
                Message mR = new Message();
                int error = mR.setMessageData(Arrays.copyOfRange(buffer, 0, n));
                ResPQ res = null;
                if(error == 0) {
                    res = (ResPQ) tlUtility.deserialize(mR.getRawMessageData());
                }
                Arrays.fill(buffer, (byte) 0);

                ReqDH reqDH = ReqDH.getTestPacket();
                byte[] data = reqPQ.serialize();

                Message m = new Message();
                m.setMessageBody(data, nTCP, 0);
                out.write(m.getPacketData());
                out.flush();
                nTCP++;

                n = in.read(buffer);
                Log.d("myLog", TLUtility.bytesToHex(buffer));
                mR = new Message();
                error = mR.setMessageData(Arrays.copyOfRange(buffer, 0, n));
                Arrays.fill(buffer, (byte) 0);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Socket getSocket() {
        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName(TCPClient.SERVERIP);
            Log.e("TCP Client", "C: Connecting...");
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, TCPClient.SERVERPORT);
            socket.setKeepAlive(true);
            return socket;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
