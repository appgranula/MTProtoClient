package com.ra1ph.MTProtoClient.tl.functions.service;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 25.07.13.
 */
public class GetFutureSalts extends TLObject {

    static final int hashConstructor=0xb921bd04;

    TLInteger num;

    public GetFutureSalts() {
    }

    public GetFutureSalts(TLInteger num) {

        this.num = num;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        ByteBuffer buffer = ByteBuffer.allocate(4 * TLInteger.SIZE);
        buffer.put(tempHash);
        buffer.put(num.serialize());
        return buffer.array();
    }

    @Override
    public GetFutureSalts deserialize(byte[] byteData) {
        return null;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }

}
