package com.ra1ph.MTProtoClient;

import android.app.Activity;
import android.os.Bundle;
import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.protocol.Authorization;
import com.ra1ph.MTProtoClient.protocol.TCPClient;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;

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

        Authorization.testAuth();



    }
}
