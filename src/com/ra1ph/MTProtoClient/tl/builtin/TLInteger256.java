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
public class TLInteger256 extends TLObject {
    public static final int SIZE = 32;

    int[] value;

    public TLInteger256(){
        value = new int[8];
    }

    public TLInteger256(int[] value){
        this.value = value;
    }

    public void setValue(int[] value){
        this.value = value;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(0,value[0]);
        buffer.putInt(4,value[1]);
        buffer.putInt(8,value[2]);
        buffer.putInt(12,value[3]);
        buffer.putInt(16,value[4]);
        buffer.putInt(20,value[5]);
        buffer.putInt(24,value[6]);
        buffer.putInt(28,value[7]);
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLInteger256 deserialize(byte[] byteData) {
        TLInteger256 object = new TLInteger256();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        int[] result = new int[8];
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(byteData,0,32);
        result[0] = buffer.getInt(0);
        result[1] = buffer.getInt(4);
        result[2] = buffer.getInt(8);
        result[3] = buffer.getInt(12);
        result[4] = buffer.getInt(16);
        result[5] = buffer.getInt(20);
        result[6] = buffer.getInt(24);
        result[7] = buffer.getInt(28);
        object.setValue(result);
        this.value=result;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
