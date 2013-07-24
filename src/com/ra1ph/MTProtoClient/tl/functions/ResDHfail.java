package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.util.Arrays;

/**
 * Created by ra1ph on 24.07.13.
 */
public class ResDHfail extends TLObject{

    static final int hashConstructor=0x79cb045d;

    TLInteger128 nonce,serverNonce, newNonceHash;

    public ResDHfail(TLInteger128 nonce, TLInteger128 serverNonce, TLInteger128 newNonceHash) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.newNonceHash = newNonceHash;
    }

    public ResDHfail() {
    }

    @Override
    public byte[] serialize() {
        return null;
    }

    @Override
    public ResDHfail deserialize(byte[] byteData) {
        TLInteger128 nonce = new TLInteger128();
        nonce.deserialize(Arrays.copyOfRange(byteData, 0, TLInteger128.SIZE));

        TLInteger128 serverNonce = new TLInteger128();
        nonce.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE,TLInteger128.SIZE * 2));

        TLInteger128 newNonceHash = new TLInteger128();
        newNonceHash.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2, TLInteger128.SIZE * 3));

        ResDHfail resDH = new ResDHfail(nonce,serverNonce,newNonceHash);
        return resDH;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }
}
