package com.ra1ph.MTProtoClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.protocol.Authorization;
import com.ra1ph.MTProtoClient.protocol.Message;
import com.ra1ph.MTProtoClient.protocol.TCPClient;
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

import java.math.BigInteger;
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
            }
        }).start();



    }
}
