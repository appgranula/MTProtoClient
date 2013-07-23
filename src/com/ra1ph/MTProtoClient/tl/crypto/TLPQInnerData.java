package com.ra1ph.MTProtoClient.tl.crypto;

import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger256;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 21.07.13
 * Time: 23:11
 * To change this template use File | Settings | File Templates.
 */
public class TLPQInnerData extends TLObject {
    static int hashConstructor = 0x83c95aec;

    TLString pq,p,q;
    TLInteger128 nonce;

    public TLString getPq() {
        return pq;
    }

    public TLString getP() {
        return p;
    }

    public TLString getQ() {
        return q;
    }

    public TLInteger128 getNonce() {
        return nonce;
    }

    public TLInteger128 getServerNonce() {
        return serverNonce;
    }

    public TLInteger256 getNewServerNonce() {
        return newServerNonce;
    }

    TLInteger128 serverNonce;
    TLInteger256 newServerNonce;

    public TLPQInnerData() {
    }

    public TLPQInnerData(TLString pq, TLString p, TLString q, TLInteger128 nonce, TLInteger128 serverNonce, TLInteger256 newServerNonce) {
        this.pq = pq;
        this.p = p;
        this.q = q;
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.newServerNonce = newServerNonce;
    }

    @Override
    public byte[] serialize() {
        int pqLen = pq.getRoundedLength();
        int pLen = p.getRoundedLength();
        int qLen = q.getRoundedLength();

        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        ByteBuffer buffer = ByteBuffer.allocate(4 + pq.getRoundedLength() + p.getRoundedLength() + q.getRoundedLength() + TLInteger128.SIZE*2 + TLInteger256.SIZE);
        buffer.order(ByteOrder.BIG_ENDIAN);
        buffer.put(tempHash);
        buffer.put(pq.serialize());
        buffer.put(p.serialize());
        buffer.put(q.serialize());
        buffer.put(nonce.serialize());
        buffer.put(serverNonce.serialize());
        buffer.put(newServerNonce.serialize());
        return buffer.array();  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public TLObject deserialize(byte[] byteData) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public static int getHashConstructor(){
        return hashConstructor;
    }

    public static TLPQInnerData getTestPacket(TLInteger128 nonce, TLInteger128 serverNonce){
        byte[] temp = new byte[]{0x17, (byte) 0xED, 0x48, (byte) 0x94, 0x1A, 0x08, (byte) 0xF9, (byte) 0x81};
        TLString pq = new TLString(temp);
        TLString p = new TLString(new byte[]{0x49, 0x4C, 0x55, 0x3B});
        TLString q = new TLString(new byte[]{0x53, (byte) 0x91, 0x10, 0x73});

        /*TLInteger128 nonce = new TLInteger128(new int[]{1040533890,0x8CCA27E9, 0x66B301A4, 0x8FECE2FC});
        TLInteger128 serverNonce = new TLInteger128(new int[]{0xA5CF4D33,0xF4A11EA8,0x77BA4AA5,0x73907330});*/

        TLInteger256 newNonce = new TLInteger256(new int[]{0x311C85DB,0x234AA264,0x0AFC4A76,0xA735CF5B,0x1F0FD68B,0xD17FA181,0xE1229AD8,0x67CC024D});
        TLPQInnerData data = new TLPQInnerData(pq,p,q,nonce,serverNonce,newNonce);
        return data;
    }

    public static TLPQInnerData getTestPacket(){
        byte[] temp = new byte[]{0x17, (byte) 0xED, 0x48, (byte) 0x94, 0x1A, 0x08, (byte) 0xF9, (byte) 0x81};
        TLString pq = new TLString(temp);
        TLString p = new TLString(new byte[]{0x49, 0x4C, 0x55, 0x3B});
        TLString q = new TLString(new byte[]{0x53, (byte) 0x91, 0x10, 0x73});

        TLInteger128 nonce = new TLInteger128(new int[]{1040533890,0x8CCA27E9, 0x66B301A4, 0x8FECE2FC});
        TLInteger128 serverNonce = new TLInteger128(new int[]{0xA5CF4D33,0xF4A11EA8,0x77BA4AA5,0x73907330});

        TLInteger256 newNonce = new TLInteger256(new int[]{0x311C85DB,0x234AA264,0x0AFC4A76,0xA735CF5B,0x1F0FD68B,0xD17FA181,0xE1229AD8,0x67CC024D});
        TLPQInnerData data = new TLPQInnerData(pq,p,q,nonce,serverNonce,newNonce);
        return data;
    }
}
