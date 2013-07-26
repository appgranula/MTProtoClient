package com.ra1ph.MTProtoClient.tl;

import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.util.Arrays;

/**
 * Created by ra1ph on 26.07.13.
 */
public class RpcResult extends TLObject{
    TLLong msgId;
    byte[] result;

    static final int hashConstructor=0xf35c6d01;

    public static int getHashConstructor() {
        return hashConstructor;
    }

    public RpcResult() {
    }

    public RpcResult(TLLong sgId, byte[] result) {

        msgId = sgId;
        this.result = result;
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public RpcResult deserialize(byte[] byteData) {
        msgId = new TLLong();
        msgId.deserialize(Arrays.copyOfRange(byteData,0,8));

        result = Arrays.copyOfRange(byteData,8,byteData.length);

        RpcResult res = new RpcResult(msgId,result);
        return res;
    }

    public byte[] getResult() {
        return result;
    }

    public TLLong getSgId() {
        return msgId;
    }
}
