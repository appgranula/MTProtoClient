package com.ra1ph.MTProtoClient.tl.crypto;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ra1ph on 25.07.13.
 */
public class ClientDHData extends TLObject {

    static int hashConstructor = 0x6643b654;

    TLInteger128 nonce,serverNonce;
    TLLong retryId;
    TLString gB;

    public ClientDHData(TLInteger128 nonce, TLInteger128 serverNonce, TLLong retryId, TLString gB) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.retryId = retryId;
        this.gB = gB;
    }

    public ClientDHData() {
    }

    @Override
    public byte[] serialize() {
        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        int gbSize = gB.getRoundedLength();

        ByteBuffer buffer = ByteBuffer.allocate(4 + TLInteger128.SIZE * 2 + TLLong.SIZE + gbSize);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(tempHash);
        buffer.put(nonce.serialize());
        buffer.put(serverNonce.serialize());
        buffer.put(retryId.serialize());
        buffer.put(gB.serialize());

        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ClientDHData deserialize(byte[] byteData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashConstructor;
    }
}
