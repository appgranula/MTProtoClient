package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.TLUtility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 18.07.13
 * Time: 16:35
 * To change this template use File | Settings | File Templates.
 */
public class Packet {

    static final int ROUND_COUNT=4;
    private static final int LEGTH_SIZE = 4;
    private static final int NUMBER_SIZE = 4;
    private static final int CRC32_SIZE = 4;
    byte[] rawData,packetData;

    public void setRawData(byte[] rawData, int nPacket){
        this.rawData = rawData;
        processRawData(nPacket);
    }

    private void processRawData(int nPacket){
        byte[] rounded = roundRawData(ROUND_COUNT,null,rawData);
        int length = rounded.length + LEGTH_SIZE + NUMBER_SIZE + CRC32_SIZE;
        ByteBuffer result = ByteBuffer.allocate(length);
        result.put(intToByte(length));
        result.put(intToByte(nPacket));
        result.put(rounded);
        result.put(TLUtility.getCRC32(Arrays.copyOfRange(result.array(), 0, rounded.length + LEGTH_SIZE + NUMBER_SIZE)));
        packetData = result.array();
    }

    public byte[] getPacketData(){
       return packetData;
    }

    public static byte[] roundRawData(int roundCount, ByteOrder order, byte[] rawData) {
        int l = rawData.length;
        int ost = l - (l/roundCount)*roundCount;
        int roundSize = (l/roundCount)*roundCount;
        if(ost>0) roundSize+=roundCount;
        ByteBuffer b = ByteBuffer.allocate(roundSize);
        if(order!=null) b.order(order);
        b.put(rawData);
        return b.array();
    }

    public static byte[] intToByte(int number){
        ByteBuffer b = ByteBuffer.allocate(4);
        b.order(ByteOrder.LITTLE_ENDIAN);
        b.putInt(number);
        byte[] result = b.array();
        return result;
    }

    public int setPacketData(byte[] packetData){
        this.packetData = packetData;
        this.rawData = Arrays.copyOfRange(packetData,LEGTH_SIZE+NUMBER_SIZE, packetData.length - CRC32_SIZE);
        if(rawData.length == 4){
            return ErrorPacket.parseErrorCode(rawData);
        }
        return 0;
    }
}
