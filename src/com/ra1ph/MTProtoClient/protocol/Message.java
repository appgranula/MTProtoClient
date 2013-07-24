package com.ra1ph.MTProtoClient.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 19.07.13
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */
public class Message extends PacketSimple {
    private static final int AUTH_KEY_SIZE = 8;
    private static final int MESSAGE_ID_SIZE = 8;
    private static final int MESSAGE_LENGTH_SIZE = 4;

    private byte[] rawMessageData;
    private byte[] messageData;
    ByteBuffer buffer;
    private long timeOffset = 0;
    private long id = 0;

    private int errorCode = 0;

    public void setMessageBody(byte[] rawData, int nPacket, long authKey) {
        this.rawMessageData = rawData;
        byte[] rounded = roundRawData(4, ByteOrder.BIG_ENDIAN, rawMessageData);
        buffer = ByteBuffer.allocate(rounded.length + AUTH_KEY_SIZE + MESSAGE_ID_SIZE + MESSAGE_LENGTH_SIZE);
        setMessageId();
        setAuthKey(authKey);
        setMessageLength(rounded.length);
        setMessage(rounded);
        setRawData(buffer.array(), nPacket);
        messageData = getPacketData();
    }

    private void setMessage(byte[] rounded) {
        //To change body of created methods use File | Settings | File Templates.
        int offset = 20;
        for (int i = 0; i < rounded.length; i++) {
            buffer.put(offset + i, rounded[i]);
        }
    }

    private void setMessageLength(int length) {
        //To change body of created methods use File | Settings | File Templates.
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.buffer.putInt(16, length);
    }

    private void setAuthKey(long authKey) {
        //To change body of created methods use File | Settings | File Templates.
        this.buffer.putLong(authKey);
    }

    private void setMessageId() {
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        id = (System.currentTimeMillis() / 1000L) << 32;
        id += timeOffset & 0xFFFFFFFF00000000L;
        this.buffer.putLong(8, id);
    }

    public void setTimeOffset(long timeOffset) {
        this.timeOffset = timeOffset;
    }

    private byte[] getMessageData() {
        return messageData;
    }

    public int setMessageData(byte[] messageData) {
        int error = setPacketData(messageData);
        if (error == 0) {
            byte[] msgData = rawData;
            this.messageData = msgData;
            int start = AUTH_KEY_SIZE + MESSAGE_ID_SIZE + MESSAGE_LENGTH_SIZE;
            int end = msgData.length;
            byte[] body = Arrays.copyOfRange(msgData, start, end);
            this.rawMessageData = body;

            byte[] id = Arrays.copyOfRange(msgData, AUTH_KEY_SIZE, AUTH_KEY_SIZE + MESSAGE_ID_SIZE);
            ByteBuffer temp = ByteBuffer.allocate(MESSAGE_ID_SIZE);
            temp.order(ByteOrder.LITTLE_ENDIAN);
            temp.put(id);
            this.id = temp.getLong(0);
        }
        return error;
    }

    public byte[] getRawMessageData() {
        return rawMessageData;
    }

    public long getId() {
        return id;
    }
}
