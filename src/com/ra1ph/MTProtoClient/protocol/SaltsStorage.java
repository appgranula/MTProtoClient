package com.ra1ph.MTProtoClient.protocol;

import com.ra1ph.MTProtoClient.tl.service.TLFutureSalt;

import java.util.ArrayList;

/**
 * Created by ra1ph on 25.07.13.
 */
public class SaltsStorage {

    private static int saltNum = 0;
    private static SaltsStorage instance;
    ArrayList<TLFutureSalt> salts;

    public static SaltsStorage getInstance(){
        if(instance == null) instance = new SaltsStorage();
        return instance;
    }

    private SaltsStorage(){
        salts = new ArrayList<TLFutureSalt>();
    }

    public void addSalt(TLFutureSalt salt){
        salts.add(salt);
    }

    public TLFutureSalt getNextSalt(){
        if(salts.size()>0){
            saltNum++;
            return salts.get(saltNum-1);
        }

        return null;
    }

    public TLFutureSalt getSalt(){
        if(salts.size()>0){
            return salts.get(saltNum);
        }

        return null;
    }

}
