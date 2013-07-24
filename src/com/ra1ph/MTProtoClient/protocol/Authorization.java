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
import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.ServerDHData;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;
import com.ra1ph.MTProtoClient.tl.functions.ReqPQ;
import com.ra1ph.MTProtoClient.tl.functions.ResDHok;
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
                int n = 0;

                ReqPQ reqPQ = new ReqPQ(new TLInteger128(new int[]{1, 2, 3, 4}));
                byte[] req = reqPQ.serialize();

                out.write(new byte[]{(byte) 0xEF});
                out.flush();

                Message m2 = new Message();
                m2.setMessageBody(req, nTCP, 0);
                Log.d("myLog", "OUT  " + TLUtility.bytesToHex(m2.getPacketData()));
                out.write(m2.getPacketData());
                out.flush();
                nTCP++;

                n = in.read(buffer);
                Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));
                Message mR = new Message();
                int error = mR.setMessageData(Arrays.copyOfRange(buffer, 0, n));
                ResPQ res = null;
                if (error == 0) {
                    res = (ResPQ) tlUtility.deserialize(mR.getRawMessageData());
                }
                Arrays.fill(buffer, (byte) 0);

                String pqStr = TLUtility.bytesToHex(res.getPq().getByteValue());
                pqStr = pqStr.replaceAll("\\s","");
                BigInteger pq = new BigInteger(pqStr,16);

                BigInteger p = new ShenksFactor().factorize(new BigInteger("1724114033281923457"));
                BigInteger q = pq.divide(p);
                if(p.compareTo(q) > 0){
                    BigInteger tmp = p;
                    p = q;
                    q = tmp;
                }

                TLInteger256 newNonce = new TLInteger256(new int[]{0x311C85DB,0x234AA264,0x0AFC4A76,0xA735CF5B,0x1F0FD68B,0xD17FA181,0xE1229AD8,0x67CC024D});
                TLPQInnerData data = new TLPQInnerData(res.getPq(),new TLString(TLUtility.hexStringToByteArray(p.toString(16))),new TLString(TLUtility.hexStringToByteArray(q.toString(16))),res.getNonce(),res.getServerNonce(),newNonce);
                byte[] dataByte = data.serialize();

                Log.d("myLog", "OUT  " + TLUtility.bytesToHex(dataByte));

                byte[] hash = CryptoUtility.getSHA1hash(dataByte);
                byte[] dataWithHash = CryptoUtility.getDataWithHash(hash,dataByte);
                byte[] rsaEnc = CryptoUtility.getRSAHackdData(dataWithHash);


                ReqDH reqDH = new ReqDH(res.getNonce(),res.getServerNonce(),new TLString(TLUtility.hexStringToByteArray(p.toString(16))),new TLString(TLUtility.hexStringToByteArray(q.toString(16))), (TLLong) res.fingerprints.getElements().get(0),rsaEnc);
                byte[] dataReqDH = reqDH.serialize();

                error = 1;
                Message mResDH = null;

                Message m = new Message();
                m.setMessageBody(dataReqDH, nTCP, 0);
                Log.d("myLog", "OUT  " + TLUtility.bytesToHex(m.getPacketData()));
                out.write(m.getPacketData());
                out.flush();
                nTCP++;

                n = in.read(buffer);
                Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));

                mResDH = new Message();
                error = mResDH.setMessageData(Arrays.copyOfRange(buffer, 0, n));
                Arrays.fill(buffer, (byte) 0);

                TLObject objDH = tlUtility.deserialize(mResDH.getRawMessageData());
                if(objDH instanceof ResDHok){
                    ResDHok resDHok = (ResDHok) objDH;
                    byte[] encAnswer = resDHok.getEncryptedAnswer();

                    byte[] tmpAesKey = CryptoUtility.getTmpAESKey(res.getServerNonce().getBytes(),newNonce.getBytes());
                    byte[] tmpAESiv = CryptoUtility.getTmpAESiv(res.getServerNonce().getBytes(),newNonce.getBytes());

                    byte[] dataDecrypt = CryptoUtility.aesIGEdecrypt(tmpAESiv,tmpAesKey,encAnswer);

                    tlUtility.deserialize(dataDecrypt);
                }


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
            //socket.setKeepAlive(true);
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
