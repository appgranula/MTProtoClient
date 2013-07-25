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
    private HashMap<TLInteger64,BigInteger> storage;

    public static KeyStorage getInstance(){
        if(instance == null) instance = new KeyStorage();
        return instance;
    }

    private KeyStorage(){
        storage = new HashMap<>();
    }

    public void addKey(BigInteger key){
        byte[] hash = CryptoUtility.getSHA1hash(key.toByteArray());
        TLInteger64 link = new TLInteger64(Arrays.copyOfRange(hash,0,8));
        storage.put(link,key);
    }

    public BigInteger getKey(TLInteger64 link){
        return storage.get(link);
    }

    public void setCurrentKey(TLInteger64 link){
        currentKey = link;
    }

    public BigInteger getCurrentKey(){
        if(currentKey!=null)return storage.get(currentKey);
        return (BigInteger) storage.keySet().toArray()[0];
    }

    public TLInteger64 getCurrentKeyId(){
        return currentKey;
    }


}
