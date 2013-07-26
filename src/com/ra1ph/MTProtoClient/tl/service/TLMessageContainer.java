package com.ra1ph.MTProtoClient.tl.service;

import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.vector.Vector;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ra1ph on 26.07.13.
 */
public class TLMessageContainer extends Vector{
    List<TLMessage> elements;
    static int hashCode = 0x73f1f8dc;

    public TLMessageContainer(List<TLMessage> elements) {
        this.elements = elements;
    }

    public TLMessageContainer() {
    }

    public List getElements(){
        return elements;
    }

    @Override
    public byte[] serialize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLMessageContainer deserialize(byte[] byteData) {
        ArrayList<TLMessage> list = new ArrayList<TLMessage>();

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData,0,4);
        int length = buffer.getInt(0);
        int n = 0;
        for(int i=0;i<length;i++){
            byte[] temp = Arrays.copyOfRange(byteData, n + 4, byteData.length);
            TLMessage tlMessage = new TLMessage();
            tlMessage.deserialize(temp);
            list.add(tlMessage);
            n+=tlMessage.getSize();
        }
        elements = list;
        TLMessageContainer vectorLong = new TLMessageContainer(list);
        return vectorLong;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashCode;
    }

}
