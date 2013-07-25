package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 25.07.13.
 */
public class SetClientDH extends TLObject {
    static final int hashConstructor=0xf5045f1f;

    TLInteger128 nonce,serverNonce;
    TLString encryptedData;

    public SetClientDH(TLInteger128 nonce, TLInteger128 serverNonce, TLString encryptedData) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.encryptedData = encryptedData;
    }

    public SetClientDH() {
    }

    @Override
    public byte[] serialize() {
        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        int dataLen = encryptedData.getRoundedLength();
        ByteBuffer buffer = ByteBuffer.allocate(4 + TLInteger128.SIZE * 2 + dataLen);
        buffer.put(tempHash);
        buffer.put(nonce.serialize());
        buffer.put(serverNonce.serialize());
        buffer.put(encryptedData.serialize());

        return buffer.array();
    }

    @Override
    public ReqDH deserialize(byte[] byteData) {
        return null;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }
}
