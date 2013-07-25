package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 25.07.13.
 */
public class TLInteger64 extends TLObject {
    public static final int SIZE = 8;

    int[] value;

    public TLInteger64(byte[] value){
        int[] val = new int[SIZE/4];
        for(int i=0;i<val.length;i++){
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(value,i*4,4);
            val[i] = buffer.getInt(0);
        }
        this.value = val;
    }

    public TLInteger64(){
        value = new int[2];
    }

    public TLInteger64(int[] value){
        this.value = value;
    }

    public void setValue(int[] value){
        this.value = value;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.putInt(value[0]);
        buffer.putInt(4,value[1]);
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLInteger64 deserialize(byte[] byteData) {
        TLInteger64 object = new TLInteger64();
        ByteBuffer buffer = ByteBuffer.allocate(8);
        int[] result = new int[2];
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(byteData,0,8);
        result[0] = buffer.getInt(0);
        result[1] = buffer.getInt(4);
        object.setValue(result);
        this.value=result;
        return object;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public byte[] getBytes() {
        return serialize();
    }

}
