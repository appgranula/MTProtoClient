package com.ra1ph.MTProtoClient.tl.functions.service;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by ra1ph on 26.07.13.
 */
public class BadServerSalt extends TLObject{
    TLLong badMsgId,newSalt;
    TLInteger badMsgSq,errorCode;

    static final int hashConstructor=0xedab447b;

    public BadServerSalt(TLLong badMsgId, TLLong newSalt, TLInteger badMsgSq, TLInteger errorCode) {
        this.badMsgId = badMsgId;
        this.newSalt = newSalt;
        this.badMsgSq = badMsgSq;
        this.errorCode = errorCode;
    }

    public BadServerSalt() {
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public BadServerSalt deserialize(byte[] byteData) {

        badMsgId = new TLLong();
        badMsgId.deserialize(Arrays.copyOfRange(byteData,0,TLLong.SIZE));

        badMsgSq = new TLInteger();
        badMsgSq.deserialize(Arrays.copyOfRange(byteData,TLLong.SIZE , TLLong.SIZE + TLInteger.SIZE));

        errorCode = new TLInteger();
        errorCode.deserialize(Arrays.copyOfRange(byteData, TLLong.SIZE + TLInteger.SIZE, TLLong.SIZE + TLInteger.SIZE * 2));

        ByteBuffer temp = ByteBuffer.allocate(8);
        temp.put(Arrays.copyOfRange(byteData,TLLong.SIZE + TLInteger.SIZE * 2,TLLong.SIZE * 2 + TLInteger.SIZE * 2));
        newSalt = new TLLong(temp.array());

        BadServerSalt badServerSalt = new BadServerSalt(badMsgId,newSalt,badMsgSq,errorCode);

        return badServerSalt;
    }

    public TLLong getBadMsgId() {
        return badMsgId;
    }

    public TLLong getNewSalt() {
        return newSalt;
    }

    public TLInteger getBadMsgSq() {
        return badMsgSq;
    }

    public TLInteger getErrorCode() {
        return errorCode;
    }
}
