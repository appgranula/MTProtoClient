package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ra1ph on 24.07.13.
 */
public class ResDHok extends TLObject{

    static final int hashConstructor=0xd0e8075c;

    TLInteger128 nonce,serverNonce;
    TLString encryptedAnswer;

    public ResDHok(TLInteger128 nonce, TLInteger128 serverNonce, TLString encryptedAnswer) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.encryptedAnswer = encryptedAnswer;
    }

    public ResDHok() {
    }

    @Override
    public byte[] serialize() {
        return null;
    }

    @Override
    public ResDHok deserialize(byte[] byteData) {
        TLInteger128 nonce = new TLInteger128();
        nonce.deserialize(Arrays.copyOfRange(byteData,0,TLInteger128.SIZE));

        TLInteger128 serverNonce = new TLInteger128();
        serverNonce.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE,TLInteger128.SIZE * 2));

        TLString encryptedAnswer = new TLString();
        encryptedAnswer.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2, byteData.length));

        ResDHok resDH = new ResDHok(nonce,serverNonce,encryptedAnswer);
        return resDH;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }

    public byte[] getEncryptedAnswer() {
        return encryptedAnswer.getByteValue();
    }
}
