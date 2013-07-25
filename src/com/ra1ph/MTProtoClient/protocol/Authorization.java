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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.ra1ph.MTProtoClient.crypto.*;
import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.ClientDHData;
import com.ra1ph.MTProtoClient.tl.crypto.ServerDHData;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;
import com.ra1ph.MTProtoClient.tl.functions.DHGenOk;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;
import com.ra1ph.MTProtoClient.tl.functions.ReqPQ;
import com.ra1ph.MTProtoClient.tl.functions.ResDHok;
import com.ra1ph.MTProtoClient.tl.functions.ResPQ;
import com.ra1ph.MTProtoClient.tl.functions.SetClientDH;
import com.ra1ph.MTProtoClient.tl.functions.service.GetFutureSalts;
import com.ra1ph.MTProtoClient.tl.service.TLFutureSalt;
import com.ra1ph.MTProtoClient.tl.vector.VectorLong;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 18.07.13
 * Time: 17:57
 * To change this template use File | Settings | File Templates.
 */
public class Authorization {
    private static InputStream in;
    private static OutputStream out;



    public Authorization() {
    }

    public static BigInteger getAuthKey() throws IOException {
        TLUtility tlUtility = TLUtility.getInstance();

        byte[] buffer = new byte[1024];
        int n = 0;

        byte[] rand = new byte[TLInteger128.SIZE];
        new Random().nextBytes(rand);
        ReqPQ reqPQ = new ReqPQ(new TLInteger128(rand));
        byte[] req = reqPQ.serialize();

        out.write(new byte[]{(byte) 0xEF});
        out.flush();

        Message m2 = new Message();
        m2.setMessageBody(req, 0, 0);
        Log.d("myLog", "OUT  " + TLUtility.bytesToHex(m2.getPacketData()));
        out.write(m2.getPacketData());
        out.flush();

        n = in.read(buffer);
        Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));
        Message mR = new Message();
        int error = mR.setMessageData(Arrays.copyOfRange(buffer, 0, n));
        ResPQ res = null;

        if(error != 0) return null;

        if (error == 0) {
            res = (ResPQ) tlUtility.deserialize(mR.getRawMessageData());
        }
        Arrays.fill(buffer, (byte) 0);

        String pqStr = TLUtility.bytesToHex(res.getPq().getByteValue());
        pqStr = pqStr.replaceAll("\\s", "");
        BigInteger pq = new BigInteger(pqStr, 16);

        PollardRho.factor(pq);
        BigInteger p = PollardRho.divider;
        BigInteger q = pq.divide(p);
        if (p.compareTo(q) > 0) {
            BigInteger tmp = p;
            p = q;
            q = tmp;
        }

        //TLInteger256 newNonce = new TLInteger256(new int[]{0x311C85DB,0x234AA264,0x0AFC4A76,0xA735CF5B,0x1F0FD68B,0xD17FA181,0xE1229AD8,0x67CC024D});

        rand = new byte[TLInteger256.SIZE];
        new Random().nextBytes(rand);
        TLInteger256 newNonce = new TLInteger256(rand);

        TLPQInnerData data = new TLPQInnerData(res.getPq(), new TLString(TLUtility.hexStringToByteArray(p.toString(16))), new TLString(TLUtility.hexStringToByteArray(q.toString(16))), res.getNonce(), res.getServerNonce(), newNonce);
        byte[] dataByte = data.serialize();

        Log.d("myLog", "OUT  " + TLUtility.bytesToHex(dataByte));

        byte[] hash = CryptoUtility.getSHA1hash(dataByte);
        byte[] dataWithHash = CryptoUtility.getDataWithHash(hash, dataByte);
        byte[] rsaEnc = CryptoUtility.getRSAHackdData(dataWithHash);


        ReqDH reqDH = new ReqDH(res.getNonce(), res.getServerNonce(), new TLString(TLUtility.hexStringToByteArray(p.toString(16))), new TLString(TLUtility.hexStringToByteArray(q.toString(16))), (TLLong) res.fingerprints.getElements().get(0), rsaEnc);
        byte[] dataReqDH = reqDH.serialize();

        error = 1;
        Message mResDH = null;

        Message m = new Message();
        m.setMessageBody(dataReqDH, 0, 0);
        Log.d("myLog", "OUT  " + TLUtility.bytesToHex(m.getPacketData()));
        out.write(m.getPacketData());
        out.flush();

        n = in.read(buffer);
        Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));

        mResDH = new Message();
        error = mResDH.setMessageData(Arrays.copyOfRange(buffer, 0, n));
        if(error != 0) return null;
        Arrays.fill(buffer, (byte) 0);

        TLObject objDH = tlUtility.deserialize(mResDH.getRawMessageData());
        if (objDH instanceof ResDHok) {
            ResDHok resDHok = (ResDHok) objDH;
            byte[] encAnswer = resDHok.getEncryptedAnswer();

            byte[] tmpAesKey = CryptoUtility.getTmpAESKey(res.getServerNonce().getBytes(), newNonce.getBytes());
            byte[] tmpAESiv = CryptoUtility.getTmpAESiv(res.getServerNonce().getBytes(), newNonce.getBytes());

            byte[] dataDecrypt = CryptoUtility.aesIGEdecrypt(tmpAESiv, tmpAesKey, encAnswer);

            byte[] dhdByte = Arrays.copyOfRange(dataDecrypt, 20, dataDecrypt.length);


            ServerDHData dhdData = (ServerDHData) tlUtility.deserialize(dhdByte);


            byte[] bBytes = new byte[256];
            new Random().nextBytes(bBytes);


            BigInteger b = new BigInteger(1,bBytes);

            BigInteger g = BigInteger.valueOf(dhdData.getG().getValue());
            BigInteger dhPrime = new BigInteger(1, dhdData.getDHPrime().getByteValue());

            BigInteger gB = g.modPow(b, dhPrime);

            BigInteger gA = new BigInteger(1, dhdData.getgA().getByteValue());
            BigInteger authKey = gA.modPow(b, dhPrime);

            Log.d("myLog", "Auth key = " + TLUtility.bytesToHex(authKey.toByteArray()));

            ClientDHData clientDHData = new ClientDHData(res.getNonce(), res.getServerNonce(), new TLLong(0), new TLString(gB.toByteArray()));
            byte[] clientDHDbyte = clientDHData.serialize();
            byte[] dhdWithHash = CryptoUtility.getDHdataWithHash(CryptoUtility.getSHA1hash(clientDHDbyte), clientDHDbyte);

            byte[] encryptedData = CryptoUtility.aesIGEencrypt(tmpAESiv, tmpAesKey, dhdWithHash);

            SetClientDH setClientDH = new SetClientDH(res.getNonce(), res.getServerNonce(), new TLString(encryptedData));

            Message setDHmsg = new Message();
            setDHmsg.setMessageBody(setClientDH.serialize(), 0, 0);

            Log.d("myLog", "OUT  " + TLUtility.bytesToHex(clientDHDbyte));

            Log.d("myLog", "OUT  " + TLUtility.bytesToHex(setDHmsg.getPacketData()));
            out.write(setDHmsg.getPacketData());
            out.flush();

            n = in.read(buffer);
            Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));

            mResDH = new Message();
            error = mResDH.setMessageData(Arrays.copyOfRange(buffer, 0, n));

            if(error != 0) return null;

            TLObject obj = tlUtility.deserialize(mResDH.getRawMessageData());
            if(obj instanceof DHGenOk){

                SaltsStorage salts = SaltsStorage.getInstance();
                ByteBuffer saltByte = ByteBuffer.allocate(8);


                saltByte.put(CryptoUtility.xor(Arrays.copyOfRange(newNonce.getBytes(), 0, 8), Arrays.copyOfRange(res.getServerNonce().getBytes(), 0, 8)));
                Log.d("myLog","Salt is " + TLUtility.bytesToHex(saltByte.array()));

                TLFutureSalt salt = new TLFutureSalt(null, null, new TLLong(saltByte.array()));
                salts.addSalt(salt);

                return authKey;
            }
            Arrays.fill(buffer, (byte) 0);

        }
        return null;
    }

    public static String getPQ(String rqLong) {
        PollardRho.factor(new BigInteger(rqLong));
        return PollardRho.divider.toString();
    }



    public static void testAuth() {
        Socket socket = getSocket();
        KeyStorage storage = KeyStorage.getInstance();

        TLUtility tlUtility = TLUtility.getInstance();

        int nTCP = 0;
        if (socket != null) {
            try {
                BigInteger authKey = null;
                while(authKey == null){
                    authKey = getAuthKey();
                    socket.close();
                    socket = getSocket();
                }
                byte[] tmp = authKey.toByteArray();
                if(tmp[0]==0)tmp = Arrays.copyOfRange(tmp,1,tmp.length);
                storage.addKey(tmp);

                getFutureSalts(5);

                Log.d("myLog", authKey.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getFutureSalts(int i) throws IOException {
        TLInteger num = new TLInteger(i);
        GetFutureSalts getFutureSalts = new GetFutureSalts(num);

        Message m = new Message();
        m.setMessageBody(getFutureSalts.serialize(),0,0);

        Log.d("myLog", "OUT  " + TLUtility.bytesToHex(m.getPacketData()));
        out.write(m.getPacketData());
        out.flush();

        byte[] buffer = new byte[1024];

        int n = in.read(buffer);
        Log.d("myLog", "IN   " + TLUtility.bytesToHex(buffer));


    }


    public static void testing() {

        byte[] buffer = new byte[1024];
        int n = 0;

        ReqPQ reqPQ = new ReqPQ(new TLInteger128(TLUtility.hexStringToByteArray("3E0549828CCA27E966B301A48FECE2FC")));

        ArrayList<TLLong> list = new ArrayList<>();
        list.add(new TLLong(-4344800451088585951L));
        VectorLong vLong = new VectorLong(list);
        ResPQ res = new ResPQ(reqPQ.nonce, new TLInteger128(TLUtility.hexStringToByteArray("A5CF4D33F4A11EA877BA4AA573907330")), new TLString(TLUtility.hexStringToByteArray("17ED48941A08F981")), vLong);


        String pqStr = TLUtility.bytesToHex(res.getPq().getByteValue());
        pqStr = pqStr.replaceAll("\\s", "");
        BigInteger pq = new BigInteger(pqStr, 16);

        PollardRho.factor(pq);
        BigInteger p = PollardRho.divider;
        BigInteger q = pq.divide(p);
        if (p.compareTo(q) > 0) {
            BigInteger tmp = p;
            p = q;
            q = tmp;
        }

        TLInteger256 newNonce = new TLInteger256(new int[]{0x311C85DB, 0x234AA264, 0x0AFC4A76, 0xA735CF5B, 0x1F0FD68B, 0xD17FA181, 0xE1229AD8, 0x67CC024D});

        TLPQInnerData data = new TLPQInnerData(res.getPq(), new TLString(TLUtility.hexStringToByteArray(p.toString(16))), new TLString(TLUtility.hexStringToByteArray(q.toString(16))), res.getNonce(), res.getServerNonce(), newNonce);
        byte[] dataByte = data.serialize();

        Log.d("myLog", "OUT  " + TLUtility.bytesToHex(dataByte));

        byte[] hash = CryptoUtility.getSHA1hash(dataByte);
        byte[] dataWithHash = CryptoUtility.getDataWithHash(hash, dataByte);
        byte[] rsaEnc = CryptoUtility.getRSAHackdData(dataWithHash);


        ReqDH reqDH = new ReqDH(res.getNonce(), res.getServerNonce(), new TLString(TLUtility.hexStringToByteArray(p.toString(16))), new TLString(TLUtility.hexStringToByteArray(q.toString(16))), (TLLong) res.fingerprints.getElements().get(0), rsaEnc);
        byte[] dataReqDH = reqDH.serialize();


        ResDHok resDHok = new ResDHok(res.getNonce(), res.getServerNonce(), new TLString(TLUtility.hexStringToByteArray("28A92FE20173B347A8BB324B5FAB2667C9A8BBCE6468D5B509A4CBDDC186240AC912CF7006AF8926DE606A2E74C0493CAA57741E6C82451F54D3E068F5CCC49B4444124B9666FFB405AAB564A3D01E67F6E912867C8D20D9882707DC330B17B4E0DD57CB53BFAAFA9EF5BE76AE6C1B9B6C51E2D6502A47C883095C46C81E3BE25F62427B585488BB3BF239213BF48EB8FE34C9A026CC8413934043974DB03556633038392CECB51F94824E140B98637730A4BE79A8F9DAFA39BAE81E1095849EA4C83467C92A3A17D997817C8A7AC61C3FF414DA37B7D66E949C0AEC858F048224210FCC61F11C3A910B431CCBD104CCCC8DC6D29D4A5D133BE639A4C32BBFF153E63ACA3AC52F2E4709B8AE01844B142C1EE89D075D64F69A399FEB04E656FE3675A6F8F412078F3D0B58DA15311C1A9F8E53B3CD6BB5572C294904B726D0BE337E2E21977DA26DD6E33270251C2CA29DFCC70227F0755F84CFDA9AC4B8DD5F84F1D1EB36BA45CDDC70444D8C213E4BD8F63B8AB95A2D0B4180DC91283DC063ACFB92D6A4E407CDE7C8C69689F77A007441D4A6A8384B666502D9B77FC68B5B43CC607E60A146223E110FCB43BC3C942EF981930CDC4A1D310C0B64D5E55D308D863251AB90502C3E46CC599E886A927CDA963B9EB16CE62603B68529EE98F9F5206419E03FB458EC4BD9454AA8F6BA777573CC54B328895B1DF25EAD9FB4CD5198EE022B2B81F388D281D5E5BC580107CA01A50665C32B552715F335FD76264FAD00DDD5AE45B94832AC79CE7C511D194BC42B70EFA850BB15C2012C5215CABFE97CE66B8D8734D0EE759A638AF013")));
        byte[] encAnswer = resDHok.getEncryptedAnswer();

        byte[] tmpAesKey = CryptoUtility.getTmpAESKey(res.getServerNonce().getBytes(), newNonce.getBytes());
        byte[] tmpAESiv = CryptoUtility.getTmpAESiv(res.getServerNonce().getBytes(), newNonce.getBytes());

        byte[] dataDecrypt = CryptoUtility.aesIGEdecrypt(tmpAESiv, tmpAesKey, encAnswer);

        byte[] dhdByte = Arrays.copyOfRange(dataDecrypt, 20, dataDecrypt.length);


        ServerDHData dhdData = new ServerDHData(res.getNonce(), res.getServerNonce(), new TLInteger(2), new TLInteger(1373993675), new TLString(TLUtility.hexStringToByteArray("C71CAEB9C6B1C9048E6C522F70F13F73 980D40238E3E21C14934D037563D930F 48198A0AA7C14058229493D22530F4DB FA336F6E0AC925139543AED44CCE7C37 20FD51F69458705AC68CD4FE6B6B13AB DC9746512969328454F18FAF8C595F64 2477FE96BB2A941D5BCD1D4AC8CC4988 0708FA9B378E3C4F3A9060BEE67CF9A4 A4A695811051907E162753B56B0F6B41 0DBA74D8A84B2A14B3144E0EF1284754 FD17ED950D5965B4B9DD46582DB1178D 169C6BC465B0D6FF9CA3928FEF5B9AE4 E418FC15E83EBEA0F87FA9FF5EED7005 0DED2849F47BF959D956850CE929851F 0D8115F635B105EE2E4E15D04B2454BF 6F4FADF034B10403119CD8E3B92FCC5B")), new TLString(TLUtility.hexStringToByteArray("262AABA621CC4DF587DC94CF8252258C 0B9337DFB47545A49CDD5C9B8EAE7236 C6CADC40B24E88590F1CC2CC762EBF1C F11DCC0B393CAAD6CEE4EE5848001C73 ACBB1D127E4CB93072AA3D1C8151B6FB 6AA6124B7CD782EAF981BDCFCE9D7A00 E423BD9D194E8AF78EF6501F415522E4 4522281C79D906DDB79C72E9C63D83FB 2A940FF779DFB5F2FD786FB4AD71C9F0 8CF48758E534E9815F634F1E3A80A5E1 C2AF210C5AB762755AD4B2126DFA61A7 7FA9DA967D65DFD0AFB5CDF26C4D4E1A 88B180F4E0D0B45BA1484F95CB2712B5 0BF3F5968D9D55C99C0FB9FB67BFF56D 7D4481B634514FBA3488C4CDA2FC0659 990E8E868B28632875A9AA703BCDCE8F")));

        BigInteger b = new BigInteger(1, TLUtility.hexStringToByteArray("6F620AFA575C9233EB4C014110A7BCAF49464F798A18A0981FEA1E05E8DA67D9681E0FD6DF0EDF0272AE3492451A84502F2EFC0DA18741A5FB80BD82296919A70FAA6D07CBBBCA2037EA7D3E327B61D585ED3373EE0553A91CBD29B01FA9A89D479CA53D57BDE3A76FBD922A923A0A38B922C1D0701F53FF52D7EA9217080163A64901E766EB6A0F20BC391B64B9D1DD2CD13A7D0C946A3A7DF8CEC9E2236446F646C42CFE2B60A2A8D776E56C8D7519B08B88ED0970E10D12A8C9E355D765F2B7BBB7B4CA9360083435523CB0D57D2B106FD14F94B4EEE79D8AC131CA56AD389C84FE279716F8124A543337FB9EA3D988EC5FA63D90A4BA3970E7A39E5C0DE5"));

        BigInteger g = BigInteger.valueOf(dhdData.getG().getValue());
        BigInteger dhPrime = new BigInteger(1, dhdData.getDHPrime().getByteValue());

        BigInteger gB = g.modPow(b, dhPrime);

        BigInteger gA = new BigInteger(1, dhdData.getgA().getByteValue());
        BigInteger authKey = gA.modPow(b, dhPrime);

        Log.d("myLog", "Auth key = " + TLUtility.bytesToHex(authKey.toByteArray()));

        byte[] testEncData = TLUtility.hexStringToByteArray("928A4957D0463B525C1CC48AABAA030A256BE5C746792C84CA4C5A0DF60AC799048D98A38A8480EDCF082214DFC79DCB9EE34E206513E2B3BC1504CFE6C9ADA46BF9A03CA74F192EAF8C278454ADABC795A566615462D31817382984039505F71CB33A41E2527A4B1AC05107872FED8E3ABCEE1518AE965B0ED3AED7F67479155BDA8E4C286B64CDF123EC748CF289B1DB02D1907B562DF462D8582BA6F0A3022DC2D3504D69D1BA48B677E3A830BFAFD67584C8AA24E1344A8904E305F9587C92EF964F0083F50F61EAB4A393EAA33C9270294AEDC7732891D4EA1599F52311D74469D2112F4EDF3F342E93C8E87E812DC3989BAECFE6740A46077524C75093F5A5405736DE8937BB6E42C9A0DCF22CA53227D462BCCC2CFE94B6FE86AB7FBFA395021F66661AF7C0024CA2986CA03F3476905407D1EA9C010B763258DB1AA2CC7826D91334EFC1FDC665B67FE45ED0");
        byte[] decData = CryptoUtility.aesIGEdecrypt(tmpAESiv, tmpAesKey, testEncData);
        Log.d("myLog", "Decrypted data  " + TLUtility.bytesToHex(decData));

        ClientDHData clientDHData = new ClientDHData(res.getNonce(), res.getServerNonce(), new TLLong(0), new TLString(gB.toByteArray()));
        byte[] clientDHDbyte = clientDHData.serialize();

        byte[] randTemp = TLUtility.hexStringToByteArray("71 62 F3 79 97 F8 65 EF 58 A0 0C 76");
        ByteBuffer temp = ByteBuffer.allocate(20 + clientDHDbyte.length + randTemp.length);
        temp.put(CryptoUtility.getSHA1hash(clientDHDbyte));
        temp.put(clientDHDbyte);
        temp.put(randTemp);

        byte[] encryptedData = CryptoUtility.aesIGEencrypt(tmpAESiv, tmpAesKey, temp.array());

        SetClientDH setClientDH = new SetClientDH(res.getNonce(), res.getServerNonce(), new TLString(encryptedData));
        Log.d("myLog", "Client DH  " + TLUtility.bytesToHex(setClientDH.serialize()));

    }

    public static Socket getSocket() {
        InetAddress serverAddr = null;
        try {
            serverAddr = InetAddress.getByName(TCPClient.SERVERIP);
            Log.e("TCP Client", "C: Connecting...");
            //create a socket to make the connection with the server
            Socket socket = new Socket(serverAddr, TCPClient.SERVERPORT);

            in = socket.getInputStream();
            out = socket.getOutputStream();

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
