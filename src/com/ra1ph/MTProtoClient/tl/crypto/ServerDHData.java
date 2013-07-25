package com.ra1ph.MTProtoClient.tl.crypto;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ra1ph on 24.07.13.
 */
public class ServerDHData extends TLObject {

    static int hashConstructor = 0xb5890dba;

    TLInteger128 nonce,serverNonce;
    TLInteger g, serverTime;
    TLString DHPrime, gA;

    public ServerDHData(TLInteger128 nonce, TLInteger128 serverNonce, TLInteger g, TLInteger serverTime, TLString DHPrime, TLString gA) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.g = g;
        this.serverTime = serverTime;
        this.DHPrime = DHPrime;
        this.gA = gA;
    }

    public ServerDHData() {
    }

    @Override
    public byte[] serialize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ServerDHData deserialize(byte[] byteData) {
        TLInteger128 nonce = new TLInteger128();
        nonce.deserialize(Arrays.copyOfRange(byteData,0,TLInteger128.SIZE));

        TLInteger128 serverNonce = new TLInteger128();
        serverNonce.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE, TLInteger128.SIZE * 2));

        TLInteger g = new TLInteger();
        g.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2, TLInteger128.SIZE * 2 + TLInteger.SIZE));

        TLString dhPrime = new TLString();
        dhPrime.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2 + TLInteger.SIZE, byteData.length));
        int primeLen = dhPrime.getRoundedLength();

        TLString gA = new TLString();
        gA.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2 + TLInteger.SIZE + primeLen, byteData.length));
        int gaLen = gA.getRoundedLength();

        TLInteger serverTime = new TLInteger();
        serverTime.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2 + TLInteger.SIZE + primeLen + gaLen, TLInteger128.SIZE * 2 + TLInteger.SIZE + primeLen + gaLen + TLInteger.SIZE));

        ServerDHData dhData = new ServerDHData(nonce,serverNonce,g,serverTime,dhPrime,gA);
        return dhData;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashConstructor;
    }

    public TLInteger getG() {
        return g;
    }

    public TLInteger getServerTime() {
        return serverTime;
    }

    public TLString getDHPrime() {
        return DHPrime;
    }

    public TLString getgA() {
        return gA;
    }
}
