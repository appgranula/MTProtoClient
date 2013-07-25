package com.ra1ph.MTProtoClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.protocol.*;
import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.ClientDHData;
import com.ra1ph.MTProtoClient.tl.crypto.ServerDHData;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;
import com.ra1ph.MTProtoClient.tl.functions.ResDHok;
import com.ra1ph.MTProtoClient.tl.functions.SetClientDH;
import com.ra1ph.MTProtoClient.tl.service.TLFutureSalt;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends Activity {
    TCPClient mTcpClient;
    TLUtility tlUtility;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        /*mTcpClient = new TCPClient();
        mTcpClient.execute();  */
        tlUtility = TLUtility.getInstance();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.


                Authorization.testAuth();

                KeyStorage.getInstance().addKey(TLUtility.hexStringToByteArray("7A 23 65 D2 CF D6 EF F8 02 C2 47 16 D3 0A 5E A0 CB BD 75 19 A4 47 42 F5 8E C8 35 E9 61 9B BC 21 CF 23 AF 6F 93 7F BB 8E F2 36 AE 72 69 FF 82 5B A0 3A 31 F9 52 FA 0B 7F 48 A7 56 47 B2 8E AF 1B E6 F6 D1 54 71 53 3F EA 1C 68 42 02 C8 B7 D4 60 B4 A8 9F BC 3E 9C 83 20 1A 16 65 B3 D5 04 3D B7 65 F5 9B 9B D5 5C 36 DA 0A FF 81 72 A1 7C 88 0C B4 6D 1A E0 8F D1 B2 CD 66 39 4C E3 E5 79 5D 26 55 A8 13 63 89 A8 AA F7 79 48 4A 93 7F 78 77 F3 5F 9C 49 91 F3 A9 25 1D B2 EE 2A 55 52 7B F6 24 53 23 31 C5 C4 01 65 94 FF 4C 3D 55 61 50 D8 E2 3E BF 0F 8D 2F 5D 58 7E 86 A3 C6 F8 4A DD DB DE CA 72 87 90 B4 FD 61 2F 1C 88 CE 63 F9 A4 52 C5 D2 99 26 F0 DB 15 E4 C1 1A 65 8D E1 41 82 45 E3 78 87 E9 7D 68 08 2D B1 42 33 DF 52 C2 51 28 5C 63 EA 78 CA 53 50 45 74 A1 1C 4B 98 C4 FB 88 C9"));

                SaltsStorage salts = SaltsStorage.getInstance();
                ByteBuffer saltByte = ByteBuffer.allocate(8);

                Log.d("myLog","Salt is " + saltByte);

                saltByte.put(TLUtility.hexStringToByteArray("33 0F A5 6A CB 03 26 A1"));
                TLFutureSalt salt = new TLFutureSalt(null, null, new TLLong(saltByte.array()));
                salts.addSalt(salt);

                byte[] pingData = TLUtility.hexStringToByteArray("EC 77 BE 7A 00 11 22 33 44 55 66 77");

                EncryptedData enc = new EncryptedData(pingData,0);


            }
        }).start();



    }
}
