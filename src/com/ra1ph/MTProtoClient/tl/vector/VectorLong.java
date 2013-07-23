package com.ra1ph.MTProtoClient.tl.vector;

import com.ra1ph.MTProtoClient.tl.builtin.TLLong;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public class VectorLong extends Vector{

    List<TLLong> elements;
    static int hashCode = 0xC734A64E;
    static final int SIZE_OF_ELEMENT=8;

    public VectorLong(){
        this.elements = new ArrayList<TLLong>();
    }

    public VectorLong(List<TLLong> elements){
        this.elements = elements;
    }

    public List getElements(){
        return elements;
    }

    @Override
    public byte[] serialize() {
        ByteBuffer buffer = ByteBuffer.allocate(elements.size()*SIZE_OF_ELEMENT + 1);
        buffer.put((byte) elements.size());
        for(int i=0;i<elements.size();i++){
            buffer.put(elements.get(i).serialize());
        }
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public VectorLong deserialize(byte[] byteData) {
        ArrayList<TLLong> list = new ArrayList<TLLong>();
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData,0,4);
        int length = buffer.getInt(0);
        for(int i=0;i<length;i++){
            byte[] temp = Arrays.copyOfRange(byteData,i*SIZE_OF_ELEMENT+4, (i+1)*SIZE_OF_ELEMENT + 4);
            TLLong tlLong = new TLLong();
            tlLong.deserialize(temp);
            list.add(tlLong);
        }
        elements = list;
        VectorLong vectorLong = new VectorLong(list);
        return vectorLong;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashCode;
    }
}
