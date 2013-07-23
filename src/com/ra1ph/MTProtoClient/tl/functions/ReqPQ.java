package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ReqPQ extends TLObject {

    static int hashCode = 0x60469778;
    public TLInteger128 nonce;

    public ReqPQ(){
        this.nonce = new TLInteger128();
    }

    public ReqPQ(TLInteger128 nonce){
        this.nonce = nonce;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer  = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putInt(hashCode);
        byte[] hashArr = buffer.array();
        byte[] intArr = nonce.serialize();
        byte[] c = new byte[hashArr.length + intArr.length];
        System.arraycopy(hashArr, 0, c, 0, hashArr.length);
        System.arraycopy(intArr, 0, c, hashArr.length, intArr.length);
        return c;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ReqPQ deserialize(byte[] byteData) {
        ReqPQ reqPQ = new ReqPQ(new TLInteger128().deserialize(byteData));
        return reqPQ;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor() {
        return hashCode;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
