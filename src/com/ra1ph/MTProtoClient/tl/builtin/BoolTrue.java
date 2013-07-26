package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

/**
 * Created by ra1ph on 26.07.13.
 */
public class BoolTrue extends TLObject {
    boolean value;

    static final int hashConstructor=0x997275b5;

    public static int getHashConstructor() {
        return hashConstructor;
    }

    public BoolTrue(boolean value) {
        this.value = value;
    }

    public BoolTrue() {
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public BoolTrue deserialize(byte[] byteData) {
            value = true;
        BoolTrue boolTrue = new BoolTrue(true);
        return boolTrue;
    }
}
