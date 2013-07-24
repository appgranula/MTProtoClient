package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 24.07.13.
 */
public class TLInteger extends TLObject{

    public static final int SIZE=4;
    int value;

    public TLInteger(){
        value = 0;
    }

    public TLInteger(byte[] val){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(val);
        value = buffer.getInt(0);
    }

    public TLInteger(int value){
        this.value = value;
    }

    public void setValue(int value){
        this.value = value;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(value);
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLInteger deserialize(byte[] byteData) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData);
        int result = buffer.getInt(0);
        TLInteger object = new TLInteger(result);
        value = result;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

}
