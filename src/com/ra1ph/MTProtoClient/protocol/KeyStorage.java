package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger64;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ra1ph on 25.07.13.
 */
public class KeyStorage {

    private static KeyStorage instance;
    private static TLInteger64 currentKey=null;
    private HashMap<TLInteger64,byte[]> storage;

    public static KeyStorage getInstance(){
        if(instance == null) instance = new KeyStorage();
        return instance;
    }

    private KeyStorage(){
        storage = new HashMap<>();
    }

    public void addKey(byte[] key){
        byte[] hash = CryptoUtility.getSHA1hash(key);
        TLInteger64 link = new TLInteger64(Arrays.copyOfRange(hash,0,8));
        storage.put(link,key);
    }

    public byte[] getKey(TLInteger64 link){
        return storage.get(link);
    }

    public void setCurrentKey(TLInteger64 link){
        currentKey = link;
    }

    public byte[] getCurrentKey(){
        if(currentKey!=null)return storage.get(currentKey);
        return (byte[]) storage.keySet().toArray()[0];
    }

    public TLInteger64 getCurrentKeyId(){
        return currentKey;
    }


}
