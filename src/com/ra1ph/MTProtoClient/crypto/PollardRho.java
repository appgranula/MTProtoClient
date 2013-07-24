package com.ra1ph.MTProtoClient.crypto;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 21.07.13
 * Time: 14:08
 * To change this template use File | Settings | File Templates.
 */

import android.util.Log;

import java.math.BigInteger;
import java.security.SecureRandom;


public class PollardRho {
    public static BigInteger divider;
    private final static BigInteger ZERO = new BigInteger("0");
    private final static BigInteger ONE = new BigInteger("1");
    private final static BigInteger TWO = new BigInteger("2");
    private final static SecureRandom random = new SecureRandom();

    public static BigInteger rho(BigInteger N) {
        BigInteger divisor;
        BigInteger c = new BigInteger(N.bitLength(), random);
        BigInteger x = new BigInteger(N.bitLength(), random);
        BigInteger xx = x;

        // check divisibility by 2
        if (N.mod(TWO).compareTo(ZERO) == 0) return TWO;

        do {
            x = x.multiply(x).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            xx = xx.multiply(xx).mod(N).add(c).mod(N);
            divisor = x.subtract(xx).gcd(N);
        } while ((divisor.compareTo(ONE)) == 0);

        return divisor;
    }

    public static void factor(BigInteger N) {
        if (divider == null) {
            if (N.compareTo(ONE) == 0) return;
            if (N.isProbablePrime(20)) {
                divider = N;
                Log.d("myLog", N.toString());
                return;
            }
            BigInteger divisor = rho(N);
            factor(divisor);
            factor(N.divide(divisor));
        }
    }

}
