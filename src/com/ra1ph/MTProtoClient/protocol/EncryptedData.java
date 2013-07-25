package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 25.07.13.
 */
public class EncryptedData {
    TLInteger64 salt, sessionId, messageId;
    TLInteger seqNo,messageDataLength;
    byte[] messageData;
    byte[] encryptedData;

    public EncryptedData(byte[] messageData) {
        this.messageData = messageData;
        salt = new TLInteger64(SaltsStorage.getInstance().getSalt().getSalt().getBytes());
        sessionId = SessionManager.getInstance().getSessionId();
        seqNo = new TLInteger(SessionManager.getInstance().getMessageId());
        messageDataLength = new TLInteger(messageData.length);
        messageId = new TLInteger64(getMessageId());
    }

    private byte[] getMessageId() {
        long id = (System.currentTimeMillis() / 1000L) << 32;
        //id += timeOffset & 0xFFFFFFFF00000000L;
        ByteBuffer temp = ByteBuffer.allocate(8);
        temp.putLong(0,id);
        return temp.array();
    }

    public byte[] getPackedData() {
        int len = TLInteger64.SIZE * 3 + TLInteger.SIZE * 2 + messageData.length;

        int ost = (len % 16) > 0 ? 16 : 0;
        int roundedLen = (len / 16) * 16 + ost;

        ByteBuffer buffer = ByteBuffer.allocate(roundedLen);
        buffer.put(salt.serialize());
        buffer.put(sessionId.serialize());
        buffer.put(messageId.serialize());
        buffer.put(seqNo.serialize());
        buffer.put(messageDataLength.serialize());
        buffer.put(messageData);

        

        return mPackedData;
    }
}
