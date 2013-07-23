package com.ra1ph.MTProtoClient.crypto;

import android.util.Log;
import com.ra1ph.MTProtoClient.tl.TLUtility;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: ra1ph
 * Date: 23.07.13
 * Time: 14:01
 * To change this template use File | Settings | File Templates.
 */
public class CryptoUtility {

    static final int DATA_WITH_HASH_SIZE=255;
    static final String RSA_PUBLIC_SERVER_KEY = "MIIBCgKCAQEAwVACPi9w23mF3tBkdZz+zwrzKOaaQdr01vAbU4E1pvkfj4sqDsm6" +
            "lyDONS789sVoD/xCS9Y0hkkC3gtL1tSfTlgCMOOul9lcixlEKzwKENj1Yz/s7daS" +
            "an9tqw3bfUV/nqgbhGX81v/+7RFAEd+RwFnK7a+XYl9sluzHRyVVaTTveB2GazTw" +
            "Efzk2DWgkBluml8OREmvfraX3bkHZJTKX4EQSjBbbdJ2ZXIsRrYOXfaA+xayEGB+" +
            "8hdlLmAjbCVfaigxX0CDqWeR1yFL9kwd9P0NsZRPsmoqVwMbMu7mStFai6aIhc3n" +
            "Slv8kg9qv1m6XHVQY3PnEw+QQtqSIXklHwIDAQAB";


    public static byte[] getSHA1hash(byte[] dataByte){
        MessageDigest md = null;
        byte[] sha1hash = new byte[20];
        try {
            md = MessageDigest.getInstance("SHA-1");
            md.update(dataByte, 0, dataByte.length);
            sha1hash = md.digest();
            return sha1hash;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static byte[] getDataWithHash(byte[] hash, byte[] data){
        ByteBuffer buffer = ByteBuffer.allocate(DATA_WITH_HASH_SIZE);
        buffer.put(hash);
        buffer.put(data);
        byte[] rand = new byte[DATA_WITH_HASH_SIZE - hash.length - data.length];
        new Random().nextBytes(rand);
        buffer.put(rand);
        return buffer.array();
    }

    public static byte[] getRSAEncryptedData(byte[] dataWithHash){
        BigInteger modulus = new BigInteger("C150023E2F70DB7985DED064759CFECF0AF328E69A41DAF4D6F01B538135A6F91F8F8B2A0EC9BA9720CE352EFCF6C5680FFC424BD634864902DE0B4BD6D49F4E580230E3AE97D95C8B19442B3C0A10D8F5633FECEDD6926A7F6DAB0DDB7D457F9EA81B8465FCD6FFFEED114011DF91C059CAEDAF97625F6C96ECC74725556934EF781D866B34F011FCE4D835A090196E9A5F0E4449AF7EB697DDB9076494CA5F81104A305B6DD27665722C46B60E5DF680FB16B210607EF217652E60236C255F6A28315F4083A96791D7214BF64C1DF4FD0DB1944FB26A2A57031B32EEE64AD15A8BA68885CDE74A5BFC920F6ABF59BA5C75506373E7130F9042DA922179251F", 16);
        BigInteger pubExp = new BigInteger("010001", 16);

        KeyFactory keyFactory = null;
        try {
            keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(modulus, pubExp);
            RSAPublicKey key = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);

            Cipher cipher = Cipher.getInstance("RSA/IGE/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] cipherData = cipher.doFinal(dataWithHash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeyException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (BadPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static byte[] getRSAHackdData(byte[] dataWithHash){
        BigInteger modulus = new BigInteger("C150023E2F70DB7985DED064759CFECF0AF328E69A41DAF4D6F01B538135A6F91F8F8B2A0EC9BA9720CE352EFCF6C5680FFC424BD634864902DE0B4BD6D49F4E580230E3AE97D95C8B19442B3C0A10D8F5633FECEDD6926A7F6DAB0DDB7D457F9EA81B8465FCD6FFFEED114011DF91C059CAEDAF97625F6C96ECC74725556934EF781D866B34F011FCE4D835A090196E9A5F0E4449AF7EB697DDB9076494CA5F81104A305B6DD27665722C46B60E5DF680FB16B210607EF217652E60236C255F6A28315F4083A96791D7214BF64C1DF4FD0DB1944FB26A2A57031B32EEE64AD15A8BA68885CDE74A5BFC920F6ABF59BA5C75506373E7130F9042DA922179251F", 16);
        BigInteger pubExp = new BigInteger("010001", 16);

        BigInteger r = new BigInteger(dataWithHash);

        BigInteger s = r.modPow(pubExp,modulus);
        byte[] temp = s.toByteArray();
        return Arrays.copyOfRange(temp,temp.length - 256,temp.length);
    }
}
