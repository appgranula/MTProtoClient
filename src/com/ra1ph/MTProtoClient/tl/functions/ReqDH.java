package com.ra1ph.MTProtoClient.tl.functions;

import com.ra1ph.MTProtoClient.crypto.CryptoUtility;
import com.ra1ph.MTProtoClient.tl.TLObject;
import com.ra1ph.MTProtoClient.tl.TLUtility;
import com.ra1ph.MTProtoClient.tl.builtin.TLInteger128;
import com.ra1ph.MTProtoClient.tl.builtin.TLLong;
import com.ra1ph.MTProtoClient.tl.builtin.TLString;
import com.ra1ph.MTProtoClient.tl.crypto.TLPQInnerData;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 23.07.13
 * Time: 15:09
 * To change this template use File | Settings | File Templates.
 */
public class ReqDH extends TLObject{
    static final int hashConstructor=0xd712e4be;
    private static final int ENCRYPTED_DATA_LENGTH = 260;

    TLInteger128 nonce,serverNonce;
    TLString p,q;
    TLLong publicKeyFingerprint;
    byte[] encryptedData;

    public ReqDH(TLInteger128 nonce, TLInteger128 serverNonce, TLString p, TLString q, TLLong publicKeyFingerprint, byte[] encryptedData) {
        this.nonce = nonce;
        this.serverNonce = serverNonce;
        this.p = p;
        this.q = q;
        this.publicKeyFingerprint = publicKeyFingerprint;
        this.encryptedData = encryptedData;
    }

    public ReqDH() {

    }

    @Override
    public byte[] serialize() {
        ByteBuffer tempHash = ByteBuffer.allocate(4);
        tempHash.order(ByteOrder.LITTLE_ENDIAN);
        tempHash.putInt(0,hashConstructor);

        ByteBuffer buffer = ByteBuffer.allocate(4 + TLInteger128.SIZE * 2 + p.getRoundedLength() + q.getRoundedLength() + TLLong.SIZE + ENCRYPTED_DATA_LENGTH);
        buffer.put(tempHash);
        buffer.put(nonce.serialize());
        buffer.put(serverNonce.serialize());
        buffer.put(p.serialize());
        buffer.put(q.serialize());
        buffer.put(publicKeyFingerprint.serialize());

        TLString encryptedStr = new TLString(encryptedData);
        buffer.put(encryptedStr.serialize());
        return buffer.array();
    }

    @Override
    public ReqDH deserialize(byte[] byteData) {
        return null;
    }

    public static int getHashConstructor() {
        return hashConstructor;
    }

    public static ReqDH getTestPacket(TLInteger128 nonce, TLInteger128 serverNonce){
        TLPQInnerData data = TLPQInnerData.getTestPacket(nonce,serverNonce);
        byte[] dataByte = data.serialize();

        byte[] hash = CryptoUtility.getSHA1hash(dataByte);
        byte[] dataWithHash = CryptoUtility.getDataWithHash(hash,dataByte);
        byte[] rsaEnc = CryptoUtility.getRSAHackdData(dataWithHash);

        byte[] FP = TLUtility.hexStringToByteArray("c3b42b026ce86b21");
        TLLong fingerprint = new TLLong(FP);
        ReqDH reqDH = new ReqDH(data.getNonce(),data.getServerNonce(),data.getP(),data.getQ(),fingerprint,rsaEnc);
        return reqDH;
    }

    public static ReqDH getTestPacket(){
        TLPQInnerData data = TLPQInnerData.getTestPacket();
        byte[] dataByte = data.serialize();

        byte[] hash = CryptoUtility.getSHA1hash(dataByte);
        byte[] dataWithHash = CryptoUtility.getDataWithHash(hash,dataByte);
        byte[] rsaEnc = CryptoUtility.getRSAHackdData(dataWithHash);

        byte[] FP = TLUtility.hexStringToByteArray("c3b42b026ce86b21");
        TLLong fingerprint = new TLLong(FP);
        ReqDH reqDH = new ReqDH(data.getNonce(),data.getServerNonce(),data.getP(),data.getQ(),fingerprint,rsaEnc);
        return reqDH;
    }
}
