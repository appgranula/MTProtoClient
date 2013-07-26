package com.ra1ph.MTProtoClient.tl;

import com.ra1ph.MTProtoClient.tl.builtin.BoolFalse;
import com.ra1ph.MTProtoClient.tl.builtin.BoolTrue;
import com.ra1ph.MTProtoClient.tl.crypto.ServerDHData;
import com.ra1ph.MTProtoClient.tl.functions.DHGenOk;
import com.ra1ph.MTProtoClient.tl.functions.ReqDH;
import com.ra1ph.MTProtoClient.tl.functions.ReqPQ;
import com.ra1ph.MTProtoClient.tl.functions.ResDHfail;
import com.ra1ph.MTProtoClient.tl.functions.ResDHok;
import com.ra1ph.MTProtoClient.tl.functions.ResPQ;
import com.ra1ph.MTProtoClient.tl.functions.service.BadServerSalt;
import com.ra1ph.MTProtoClient.tl.functions.service.NewSessionCreated;
import com.ra1ph.MTProtoClient.tl.service.TLMessageContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.zip.CRC32;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 18.07.13
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
public class TLUtility {

    static TLUtility instance;
    HashMap<Integer, Class<TLObject>> objects;

    private void initialize() {
        objects = new HashMap<Integer, Class<TLObject>>();
        ArrayList<Class> obj = new ArrayList<Class>();

        obj.add(ResPQ.class);
        obj.add(ReqPQ.class);
        obj.add(ReqDH.class);
        obj.add(ResDHok.class);
        obj.add(ResDHfail.class);
        obj.add(ServerDHData.class);
        obj.add(DHGenOk.class);
        obj.add(TLMessageContainer.class);
        obj.add(NewSessionCreated.class);
        obj.add(BadServerSalt.class);
        obj.add(RpcResult.class);
        obj.add(BoolTrue.class);
        obj.add(BoolFalse.class);

        for (int i = 0; i < obj.size(); i++) {
            try {
                Method m = obj.get(i).getMethod("getHashConstructor");
                Class o = obj.get(i);
                int hash = (Integer) m.invoke(obj);
                objects.put(hash, o);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (InvocationTargetException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IllegalAccessException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static TLUtility getInstance() {
        if (instance == null) instance = new TLUtility();
        return instance;
    }

    private TLUtility() {
        initialize();
    }

    public static int getCRC32(String str) {
        CRC32 crc = new CRC32();
        crc.update(str.getBytes(), 0, str.length());
        return (int) crc.getValue();
    }

    public static byte[] getCRC32(byte[] str) {
        CRC32 crc = new CRC32();
        crc.update(str, 0, str.length);
        byte[] res = new byte[4];
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(crc.getValue());
        return Arrays.copyOfRange(buffer.array(), 0, 4);
    }

    public static byte[] codeString(String str) {
        int L = str.length();
        byte[] result;
        if (L < 254) {
        }
        return null;
    }

    public static char[] byteToChar(byte[] str) {
        char[] res = new char[str.length];
        for (int i = 0; i < str.length; i++) res[i] = (char) str[i];
        return res;
    }

    public byte[] serialize(TLObject object) {
        return object.serialize();
    }

    public TLObject deserialize(byte[] byteData) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.put(byteData, 0, 4);
        int hash = buffer.getInt(0);
        Class tlClass = objects.get(hash);
        try {
            if (tlClass != null) {
                TLObject result = (TLObject) tlClass.newInstance();
                return result.deserialize(Arrays.copyOfRange(byteData, 4, byteData.length));
            }
        } catch (InstantiationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalAccessException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 3];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            hexChars[j * 3 + 2] = ' ';
        }
        return new String(hexChars);
    }

    public static byte[] hexStringToByteArray(String s) {
        String str = s.replaceAll("\\s", "");
        int len = str.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            if (i / 2 < data.length) {
                data[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4)
                        + Character.digit(str.charAt(i + 1), 16));
            } else {
                break;
            }
        }
        return data;
    }
}
