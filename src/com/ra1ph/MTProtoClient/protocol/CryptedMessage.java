package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

/**
 * Created by ra1ph on 25.07.13.
 */
public class CryptedMessage extends Packet{
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

    public void setEncryptedData(byte[] data){
        packedData = data;
    }
}
