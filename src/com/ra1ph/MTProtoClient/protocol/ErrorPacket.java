package com.ra1ph.MTProtoClient.protocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 23.07.13.
 */
public class ErrorPacket extends Packet{
    public static int parseErrorCode(byte[] error){
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(error);
        return buffer.getInt(0);
    }
}
