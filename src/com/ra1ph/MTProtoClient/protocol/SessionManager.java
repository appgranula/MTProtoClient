package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

/**
 * Created by ra1ph on 25.07.13.
 */
public class SessionManager {
    static SessionManager instance;
    static TLInteger64 sessionId;
    static int messageId;

    public static SessionManager getInstance(){
        if(instance==null)instance = new SessionManager();
        return instance;
    }

    private SessionManager(){
        byte[] rand = new byte[TLInteger64.SIZE];
        new Random().nextBytes(rand);
        sessionId = new TLInteger64(rand);
        messageId = 0;
    }

    public byte[] getSessionId() {
        ByteBuffer temp = ByteBuffer.allocate(8);
        temp.order(ByteOrder.LITTLE_ENDIAN);
        temp.put(sessionId.getBytes());
        return temp.array();
    }

    public int getMessageId() {
        return messageId * 2;
    }

    public void incrementMessageId(){
        messageId++;
    }
}
