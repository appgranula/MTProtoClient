package com.ra1ph.MTProtoClient.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created by ra1ph on 24.07.13.
 */
public class PacketSimple {

    public byte[] rawData;
    private byte[] packetData;

    public void setRawData(byte[] rawData, int nPacket) {
        this.rawData = rawData;
        processRawData(nPacket);
    }

    private void processRawData(int nPacket) {
        byte[] rounded = roundRawData(4, ByteOrder.BIG_ENDIAN, rawData);
        ByteBuffer buffer;
        if (rounded.length < 504) {
            buffer = ByteBuffer.allocate(rounded.length + 1);
            buffer.put((byte) (rounded.length / 4));
        }else {
            ByteBuffer temp = ByteBuffer.allocate(4);
            temp.putInt(rounded.length / 4);
            temp.put(0, (byte) 0x7E);

            buffer = ByteBuffer.allocate(rounded.length + 4);
            buffer.put(temp);
        }
        buffer.put(rounded);
        packetData = buffer.array();
    }

    public static byte[] roundRawData(int roundCount, ByteOrder order, byte[] rawData) {
        int l = rawData.length;
        int ost = l - (l / roundCount) * roundCount;
        int roundSize = (l / roundCount) * roundCount;
        if (ost > 0) roundSize += roundCount;
        ByteBuffer b = ByteBuffer.allocate(roundSize);
        if (order != null) b.order(order);
        b.put(rawData);
        return b.array();
    }

    public byte[] getPacketData() {
        return packetData;
    }

    public int setPacketData(byte[] packetData) {
        this.packetData = packetData;
        if (packetData[0] != 0x7F) {
            int len = packetData[0];
            this.rawData = Arrays.copyOfRange(packetData, 1, len * 4 + 1);
            if (rawData.length == 4) {
                return ErrorPacket.parseErrorCode(rawData);
            }
        } else {
            this.rawData = Arrays.copyOfRange(packetData, 4, packetData.length);
        }

        return 0;
    }
}
