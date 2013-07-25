package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

/**
 * Created by ra1ph on 25.07.13.
 */
public class CryptedMessage extends Packet{
    TLInteger64 authKeyId;
    TLInteger128 msgKey;
    EncryptedData encryptedData;
    byte[] packedData;

    public CryptedMessage(TLInteger64 authKeyId, TLInteger128 sgKey, EncryptedData encryptedData) {
        this.authKeyId = authKeyId;
        msgKey = sgKey;
        this.encryptedData = encryptedData;
    }

    public CryptedMessage(EncryptedData encryptedData) {
        this.encryptedData = encryptedData;
    }

    public void setMessageData(byte[] data){
        EncryptedData encryptedData = new EncryptedData(data);
        packedData = encryptedData.getPackedData();
    }
}
