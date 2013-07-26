package com.ra1ph.MTProtoClient.tl.functions.service;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.util.Arrays;

/**
 * Created by ra1ph on 26.07.13.
 */
public class NewSessionCreated extends TLObject {
    TLLong firstMsgId, uniqueId, salt;

    static final int hashConstructor=0x9ec20908;

    public static int getHashConstructor() {
        return hashConstructor;
    }


    public NewSessionCreated(TLLong firstMsgId, TLLong uniqueId, TLLong salt) {
        this.firstMsgId = firstMsgId;
        this.uniqueId = uniqueId;
        this.salt = salt;
    }

    public NewSessionCreated() {
    }

    @Override
    public byte[] serialize() {
        return null;
    }

    @Override
    public NewSessionCreated deserialize(byte[] byteData) {

        firstMsgId = new TLLong();
        firstMsgId.deserialize(Arrays.copyOfRange(byteData,0,TLLong.SIZE));

        uniqueId = new TLLong();
        uniqueId.deserialize(Arrays.copyOfRange(byteData,TLLong.SIZE, TLLong.SIZE * 2));

        salt = new TLLong();
        salt.deserialize(Arrays.copyOfRange(byteData,TLLong.SIZE * 2,TLLong.SIZE * 3));

        NewSessionCreated newSessionCreated = new NewSessionCreated(firstMsgId,uniqueId,salt);
        return newSessionCreated;
    }

    public TLLong getFirstMsgId() {
        return firstMsgId;
    }

    public TLLong getUniqueId() {
        return uniqueId;
    }

    public TLLong getSalt() {
        return salt;
    }
}
