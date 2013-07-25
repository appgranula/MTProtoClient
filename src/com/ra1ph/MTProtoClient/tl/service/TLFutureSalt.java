package com.ra1ph.MTProtoClient.tl.service;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ra1ph on 25.07.13.
 */
public class TLFutureSalt extends TLObject {
    public static final int SIZE = TLInteger.SIZE * 2 + TLLong.SIZE;
    static int hashConstructor = 0x0949d9dc;

    TLInteger since, until;
    TLLong salt;

    public TLFutureSalt(TLInteger since, TLInteger until, TLLong salt) {
        this.since = since;
        this.until = until;
        this.salt = salt;
    }

    public TLFutureSalt() {
    }

    @Override
    public byte[] serialize() {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLFutureSalt deserialize(byte[] byteData) {
        TLInteger since = new TLInteger();
        since.deserialize(Arrays.copyOfRange(byteData,0,TLInteger.SIZE));

        TLInteger until = new TLInteger();
        until.deserialize(Arrays.copyOfRange(byteData,TLInteger.SIZE, TLInteger.SIZE * 2));

        TLLong salt = new TLLong();
        salt.deserialize(Arrays.copyOfRange(byteData,TLInteger.SIZE * 2, TLInteger.SIZE * 2 + TLLong.SIZE));

        TLFutureSalt futureSalt = new TLFutureSalt(since,until,salt);
        return futureSalt;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashConstructor;
    }

    public TLLong getSalt() {
        return salt;
    }
}
