package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.vector.VectorLong;

import java.util.Arrays;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 13:32
 * To change this template use File | Settings | File Templates.
 */
public class ResPQ extends TLObject {

    static int hashCode = 0x05162463;
    public TLInteger128 nonce;
    public TLInteger128 serverNonce;
    public TLString pq;
    public VectorLong fingerprints;

    public ResPQ(){
        this.nonce = new TLInteger128();
    }

    public ResPQ(TLInteger128 nonce,TLInteger128 serverNonce,TLString pq,VectorLong fingerprints){
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.pq = pq;
        this.fingerprints = fingerprints;
    }

    @Override
    public byte[] serialize() {

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ResPQ deserialize(byte[] byteData) {
        TLInteger128 nonce = new TLInteger128();
        nonce.deserialize(byteData);

        TLInteger128 serverNonce = new TLInteger128();
        serverNonce.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE,TLInteger128.SIZE * 2));


        TLString pq = new TLString();
        pq.deserialize(Arrays.copyOfRange(byteData,TLInteger128.SIZE * 2, byteData.length));

        VectorLong fingerprints = new VectorLong();
        fingerprints.deserialize(Arrays.copyOfRange(byteData, TLInteger128.SIZE * 2 + pq.getRoundedLength() + 4, byteData.length));

        ResPQ resPq = new ResPQ(nonce,serverNonce,pq,fingerprints);
        return resPq;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor() {
        return hashCode;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public TLInteger128 getNonce() {
        return nonce;
    }

    public TLInteger128 getServerNonce() {
        return serverNonce;
    }

    public TLString getPq() {
        return pq;
    }
}
