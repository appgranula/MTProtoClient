package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by ra1ph on 25.07.13.
 */
public class CryptedMessage extends PacketSimple{
    private byte[] authKeyId;
    private byte[] msgKey;
    private byte[] encryptedData;
    private byte[] packedData;

    public CryptedMessage(byte[] authKeyId, byte[] sgKey, byte[] encryptedData) {
        this.authKeyId = authKeyId;
        msgKey = sgKey;
        this.encryptedData = encryptedData;
    }

    public CryptedMessage(byte[] encryptedData) {
        this.encryptedData = encryptedData;
    }

    public CryptedMessage() {
    }

    public void setEncryptedData(byte[] data, byte[] authKeyId, byte[] msgKey){
        this.authKeyId = authKeyId;
        this.msgKey = msgKey;
        encryptedData = data;

        ByteBuffer buffer = ByteBuffer.allocate(8 + 16 + data.length);
        buffer.put(authKeyId);
        buffer.put(msgKey);
        buffer.put(encryptedData);
        this.packedData = buffer.array();
        setRawData(packedData,0);
    }


    public int setMessageData(byte[] messageData) {
        int error = setPacketData(messageData);
        if(error == 0){
            byte[] msgData = rawData;
            authKeyId = Arrays.copyOfRange(msgData,0,8);
            msgKey = Arrays.copyOfRange(msgData,8,24);
            encryptedData = Arrays.copyOfRange(msgData,24,msgData.length);
        }
        return error;  //To change body of created methods use File | Settings | File Templates.
    }

    public byte[] getEncryptedData() {
        return encryptedData;
    }

    public byte[] getMsgKey() {
        return msgKey;
    }

    public byte[] getAuthKeyId() {
        return authKeyId;
    }
}
