package com.ra1ph.MTProtoClient.tl.builtin;

import com.ra1ph.MTProtoClient.tl.TLObject;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 20:38
 * To change this template use File | Settings | File Templates.
 */
public class TLString extends TLObject {
    int len;
    String str;

    public TLString() {

    }

    public TLString(String str) {
        this.str = str;
    }

    public TLString(byte[] str) {
        char[] temp = new char[str.length];
        for(int i=0;i<str.length;i++){
            temp[i]= (char) str[i];
        }

        this.str = new String(temp);
    }

    @Override
    public byte[] serialize() {
        char[] chStr = str.toCharArray();
        byte[] temp = new byte[chStr.length];
        for(int i=0;i<chStr.length;i++){
            temp[i]= (byte) chStr[i];
        }

        if (chStr.length < 254) {
            int length = chStr.length + 1;
            int ost = (length - (length / 4) * 4) > 0 ? 4 : 0;
            int rounded = (length / 4) * 4 + ost;
            ByteBuffer buffer = ByteBuffer.allocate(rounded);
            buffer.put((byte) temp.length);

            buffer.put(temp);
            return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
        } else {
            int length = chStr.length + 4;
            int ost = (length - (length / 4) * 4) > 0 ? 4 : 0;
            int rounded = (length / 4) * 4 + ost;

            int newLength = temp.length;
            ByteBuffer len = ByteBuffer.allocate(4);
            len.putInt(0,newLength);
            len.put(0,(byte)0xFE);

            ByteBuffer buffer = ByteBuffer.allocate(rounded);
            buffer.put(len);
            buffer.put(temp);
            return buffer.array();
        }
    }


    @Override
    public TLString deserialize(byte[] byteData) {
        int length = byteData[0];
        byte[] temp = null;
        TLString tlString;

        if (byteData[0]!= (byte) 0xFE) {
            temp = Arrays.copyOfRange(byteData, 1, length + 1);
        } else {
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(byteData, 1, 3);
            length = buffer.getInt(0);
            temp = Arrays.copyOfRange(byteData, 4, length + 4);

        }

        len = temp.length;
        tlString = new TLString(temp);

        char[] str = new char[temp.length];
        for(int i=0;i<str.length;i++){
            str[i]= (char) temp[i];
        }

        this.str = new String(str);

        return tlString;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getHashConstructor() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public int getRoundedLength() {
        if (str.length() < 254) {
            int length = str.toCharArray().length + 1;
            int ost = (length - (length / 4) * 4) > 0 ? 4 : 0;
            int rounded = (length / 4) * 4 + ost;
            len = rounded;
        } else {
            int length = str.toCharArray().length + 4;
            int ost = (length - (length / 4) * 4) > 0 ? 4 : 0;
            int rounded = (length / 4) * 4 + ost;
            len = rounded;
        }
        return len;
    }

    public byte[] getByteValue(){
        char[] chStr = str.toCharArray();
        byte[] temp = new byte[chStr.length];
        for(int i=0;i<chStr.length;i++){
            temp[i]= (byte) chStr[i];
        }
        return temp;
    }

}
