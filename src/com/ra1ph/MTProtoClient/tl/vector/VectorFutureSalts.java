package com.ra1ph.MTProtoClient.tl.vector;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.service.TLFutureSalt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ra1ph on 25.07.13.
 */
public class VectorFutureSalts extends Vector {
    List<TLFutureSalt> elements;


    static int hashCode = 0xae500895;
    static final int SIZE_OF_ELEMENT=TLFutureSalt.SIZE;

    public VectorFutureSalts(){
        this.elements = new ArrayList<TLFutureSalt>();
    }

    public VectorFutureSalts(List<TLFutureSalt> elements){
        this.elements = elements;
    }

    public List getElements(){
        return elements;
    }

    @Override
    public byte[] serialize() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public VectorFutureSalts deserialize(byte[] byteData) {
       /* TLUtility tlUtility =
        ArrayList<TLFutureSalt> list = new ArrayList<TLFutureSalt>();

        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData,0,4);
        int length = buffer.getInt(0);

        for(int i=0;i<length;i++){
            byte[] temp = Arrays.copyOfRange(byteData, i * SIZE_OF_ELEMENT + 4, (i + 1) * SIZE_OF_ELEMENT + 4);
            TLFutureSalt tlLong = new TLFutureSalt();
            .deserialize(temp);
            list.add(tlLong);
        }
        elements = list;
        VectorLong vectorLong = new VectorLong(list);*/
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashCode;
    }
}
