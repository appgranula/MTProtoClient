package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ra1ph on 25.07.13.
 */
public class KeyStorage {

    private static KeyStorage instance;
    private static Long currentKey=null;
    private HashMap<Long,byte[]> storage;

    public static KeyStorage getInstance(){
        if(instance == null) instance = new KeyStorage();
        return instance;
    }

    private KeyStorage(){
        storage = new HashMap<>();
    }

    public void addKey(byte[] key){
        byte[] hash = CryptoUtility.getSHA1hash(key);
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(Arrays.copyOfRange(hash,12,20));
        Long link = buffer.getLong(0);
        storage.put(link,key);
    }

    public byte[] getKey(Long link){
        return storage.get(link);
    }

    public void setCurrentKey(Long link){
        currentKey = link;
    }

    public byte[] getCurrentKey(){
        if(currentKey!=null)return storage.get(currentKey);
        currentKey = (Long) storage.keySet().toArray()[0];
        return storage.get(currentKey);
    }

    public Long getCurrentKeyId(){
        return currentKey;
    }


}
