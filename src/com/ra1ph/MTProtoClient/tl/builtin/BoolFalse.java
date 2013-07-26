package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;

/**
 * Created by ra1ph on 26.07.13.
 */
public class BoolFalse extends TLObject {
    boolean value;

    static final int hashConstructor=0xbc799737;

    public static int getHashConstructor() {
        return hashConstructor;
    }

    public BoolFalse(boolean value) {
        this.value = value;
    }

    public BoolFalse() {
    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public BoolFalse deserialize(byte[] byteData) {
            value = false;
        BoolFalse boolTrue = new BoolFalse(false);
        return boolTrue;
    }
}
