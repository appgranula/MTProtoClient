package com.ra1ph.MTProtoClient.tl.functions.contest;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by ra1ph on 26.07.13.
 */
public class SaveDeveloperInfo extends TLObject {
    static final int hashConstructor=0x9a5f6e95;


    TLInteger vkId,age;
    TLString name,phoneNumber,city;

    public SaveDeveloperInfo(TLInteger vkId, TLInteger age, TLString name, TLString phoneNumber, TLString city) {
        this.vkId = vkId;
        this.age = age;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        ByteBuffer buffer = ByteBuffer.allocate(4 + name.getRoundedLength() + phoneNumber.getRoundedLength() + city.getRoundedLength() + TLInteger.SIZE * 2);
        buffer.put(tempHash);
        buffer.put(vkId.serialize());
        buffer.put(name.serialize());
        buffer.put(phoneNumber.serialize());
        buffer.put(age.serialize());
        buffer.put(city.serialize());

        return buffer.array();
    }

    @Override
    public SaveDeveloperInfo deserialize(byte[] byteData) {
        return null;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }
}
