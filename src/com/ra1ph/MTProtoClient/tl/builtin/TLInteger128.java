package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 12:49
 * To change this template use File | Settings | File Templates.
 */
public class TLInteger128 extends TLObject {
    public static final int SIZE = 16;

    int[] value;

    public TLInteger128(){
        value = new int[4];
    }

    public TLInteger128(int[] value){
        this.value = value;
    }

    public void setValue(int[] value){
        this.value = value;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value[0]);
        buffer.putInt(4,value[1]);
        buffer.putInt(8,value[2]);
        buffer.putInt(12,value[3]);
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLInteger128 deserialize(byte[] byteData) {
        TLInteger128 object = new TLInteger128();
        ByteBuffer buffer = ByteBuffer.allocate(16);
        int[] result = new int[4];
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(byteData,0,16);
        result[0] = buffer.getInt(0);
        result[1] = buffer.getInt(4);
        result[2] = buffer.getInt(8);
        result[3] = buffer.getInt(12);
        object.setValue(result);
        this.value=result;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
