package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 22:20
 * To change this template use File | Settings | File Templates.
 */
public class TLLong extends TLObject{
    public static final int SIZE=8;
    long value;

    public TLLong(){
        value = 0;
    }

    public TLLong(byte[] val){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(val);
        value = buffer.getLong(0);
    }

    public TLLong(long value){
        this.value = value;
    }

    public void setValue(long value){
        this.value = value;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(value);
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLLong deserialize(byte[] byteData) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData);
        long result = buffer.getLong(0);
        TLLong object = new TLLong(result);
        value = result;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] getBytes(){
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putLong(value);
        return buffer.array();
    }
}
