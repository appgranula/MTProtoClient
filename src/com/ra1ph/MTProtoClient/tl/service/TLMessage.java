package com.ra1ph.MTProtoClient.tl.service;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.util.Arrays;

/**
 * Created by ra1ph on 26.07.13.
 */
public class TLMessage extends TLObject {
    TLInteger seqNo,len;
    TLLong mesId;
    byte[] data;

    public TLMessage(TLInteger seqNo, TLInteger len, TLLong esId, byte[] data) {
        this.seqNo = seqNo;
        this.len = len;
        mesId = esId;
        this.data = data;
    }

    public TLMessage() {
    }

    @Override
    public byte[] serialize() {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLMessage deserialize(byte[] byteData) {

        mesId = new TLLong();
        mesId.deserialize(Arrays.copyOfRange(byteData,0,TLLong.SIZE));

        seqNo = new TLInteger();
        seqNo.deserialize(Arrays.copyOfRange(byteData,TLLong.SIZE,TLLong.SIZE + TLInteger.SIZE));

        len = new TLInteger();
        len.deserialize(Arrays.copyOfRange(byteData,TLLong.SIZE + TLInteger.SIZE, TLLong.SIZE + TLInteger.SIZE * 2));

        data = Arrays.copyOfRange(byteData,TLLong.SIZE + TLInteger.SIZE * 2, TLLong.SIZE + TLInteger.SIZE * 2 + len.getValue());

        TLMessage mes = new TLMessage(seqNo,len,mesId,byteData);

        return mes;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getSize(){
        return TLLong.SIZE + TLInteger.SIZE * 2 + len.getValue();
    }

    public byte[] getData() {
        return data;
    }
}
