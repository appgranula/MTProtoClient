package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 25.07.13.
 */
public class EncryptedData extends CryptedMessage {
    byte[] salt, sessionId, messageId;
    byte[] seqNo,messageDataLength;
    byte[] messageData;
    byte[] encryptedData;
    private byte[] authKey;
    private byte[] msgKey;


//    9E 39 02 7D CF 27 33 57 - auth_key_id == substr(SHA1(auth_key),12,8)
//            46 8E C0 E5 3D 09 BB 0E - msg_key == substr(SHA1(Message),4,16)
//    И дальше сообщение под шифрованием
//    CB 28 87 7F DE AC 0B 1C - salt == substr(new_nonce, 0, 8) XOR substr(server_nonce, 0, 8)
//            0A 00 00 00 00 00 00 00 - session_id == рандомное число (10)
//    00 00 00 00 BF DF EF 51 - message_id == по типу тех же id что и в прежних запросах
//    01 00 00 00 - seg_no == 1
//            0C 00 00 00 - message_data_length == 12 байт
//    EC 77 BE 7A - ping# == 0x7abe77ec
//    D4 1B 13 01 00 00 00 00 - ping_id:long == Какое то либо рандомное число

    public EncryptedData(byte[] messageData, int idPacket) {
        this.messageData = messageData;
        salt = SaltsStorage.getInstance().getSalt().getSalt().getBytes();
        sessionId = SessionManager.getInstance().getSessionId();

        ByteBuffer temp = ByteBuffer.allocate(4);
        temp.order(ByteOrder.LITTLE_ENDIAN);
        temp.putInt(0, SessionManager.getInstance().getMessageId());

        seqNo = temp.array();

        temp = ByteBuffer.allocate(4);
        temp.order(ByteOrder.LITTLE_ENDIAN);
        temp.putInt(0,messageData.length);

        messageDataLength = temp.array();
        messageId = getMessageId(idPacket);

        getPackedData();
        super(authKey,msgKey,encryptedData);
    }

    private byte[] getMessageId(int idPacket) {
        long id = (System.currentTimeMillis() / 1000L) << 32 + 4 * idPacket;
        //id += timeOffset & 0xFFFFFFFF00000000L;
        ByteBuffer temp = ByteBuffer.allocate(8);
        temp.order(ByteOrder.LITTLE_ENDIAN);
        temp.putLong(0,id);
        return temp.array();
    }

    public byte[] getPackedData() {
        int len = TLInteger64.SIZE * 3 + TLInteger.SIZE * 2 + messageData.length;

        ByteBuffer nBuffer = ByteBuffer.allocate(len);
        nBuffer.put(salt);
        nBuffer.put(sessionId);
        nBuffer.put(messageId);
        nBuffer.put(seqNo);
        nBuffer.put(messageDataLength);
        nBuffer.put(messageData);

        authKey = CryptoUtility.getAuthKeyHash(nBuffer.array());
        msgKey = CryptoUtility.getMsgKeyHash(nBuffer.array());

        int ost = (len % 16) > 0 ? 16 : 0;
        int roundedLen = (len / 16) * 16 + ost;

        ByteBuffer buffer = ByteBuffer.allocate(roundedLen);
        buffer.put(nBuffer);

        byte[] encrypted = CryptoUtility.messageEncrypt(authKey,msgKey,buffer.array(), true);
        encryptedData = encrypted;
        return encrypted;
    }
}
