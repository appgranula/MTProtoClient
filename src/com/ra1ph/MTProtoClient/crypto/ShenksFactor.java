package com.ra1ph.MTProtoClient.crypto;


import java.math.BigInteger;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 24.07.13
 * Time: 21:36
 * To change this template use File | Settings | File Templates.
 */
public class ShenksFactor {
    BigInteger lP,lQ,lr,P,Q,r,llQ,llP,llr;
    BigInteger d;

    public void firstLoop(BigInteger N){
        while(true){
            P = lr.multiply(lQ).subtract(lP);
            Q = llQ.add((lP.subtract(P)).multiply(lr));
            r = ((P.add(SqRtN(N))).divide(Q));

            if(isPerfectSQRT(Q)){
                d = SqRtN(Q);
                break;
            }

            llP = lP;
            llQ = lQ;
            llr = lr;

            lP = P;
            lQ = Q;
            lr = r;
        }
    }

    public void secondLoop(BigInteger N){
        llP = P.negate();
        llQ = d;
        llr = llP.add(SqRtN(N)).divide(llQ);

        lP = llr.multiply(llQ).subtract(llP);
        lQ = N.subtract(lP.pow(2)).divide(llQ);

        while(true){
            P = lr.multiply(lQ).subtract(lP);
            Q = llQ.add((lP.subtract(P)).multiply(lr));
            r = ((P.add(SqRtN(N))).divide(Q));

            if(P.equals(lP)){
                break;
            }

            llP = lP;
            llQ = lQ;
            llr = lr;

            lP = P;
            lQ = Q;
            lr = r;
        }
    }

    public BigInteger factorize(BigInteger N){
        BigInteger ost = N.mod(BigInteger.valueOf(4));
        if(ost.equals(1))N.multiply(BigInteger.valueOf(2));
        llP = BigInteger.valueOf(0);
        llQ = BigInteger.valueOf(1);
        llr = SqRtN(N);
        lP = llr;
        lQ = N.subtract(llr.pow(2));
        lr = llr.multiply(BigInteger.valueOf(2)).divide(lQ);

        firstLoop(N);
        secondLoop(N);

        return P;
    }
/*
    public static void main(String[] args) {
        int n = 11111, d, D, k = 2, j = 2,ln=1000;
//n - входное число
        BigInteger[] P = new BigInteger[ln], Q = new BigInteger[ln], r = new BigInteger[ln], q = new BigInteger[ln];
        int[] Ps = new int[ln], Qs = new int[ln], rs = new int[ln];
        if ((n - 1) % 4 == 0) {//if n=1(mod 4)
            n *= 2;
        }
//я очень сомневаюсь в этой части
        D = 4 * n;
        q[0] = (int) Math.sqrt(D);
//скорее всего опечатка в методичке
//в скором времени постараюсь разобраться

        P[0] = 0;
        Q[0] = 1;
        r[0] = (int) Math.sqrt(n);
        P[1] = r[0];
        Q[1] = n - (r[0] * r[0]);
        r[1] = (int) (2 * r[0] / Q[1]);
        while (true) {
            P[k] = r[k - 1] * Q[k - 1] - P[k - 1];
            Q[k] = Q[k - 2] + (P[k - 1] - P[k]) * r[k - 1];
            r[k] = ((P[k] + (int) Math.sqrt(n)) / Q[k]);

            if (Math.sqrt(Q[k]) % 1 == 0) {

                d = (int) Math.sqrt(Q[k]);
                break;
            }
            k++;
        }
        Ps[0] = -P[k];
        Qs[0] = d;
        rs[0] = ((Ps[0] + (int) Math.sqrt(n)) / Qs[0]);
        Ps[1] = rs[0] * Qs[0] - Ps[0];
        Qs[1] = (n - Ps[1] * Ps[1]) / Qs[0];

        while (true) {
            Ps[j] = rs[j - 1] * Qs[j - 1] - Ps[j - 1];
            Qs[j] = Qs[j - 2] + (Ps[j - 1] - Ps[j]) * rs[j - 1];
            rs[j] = ((Ps[j] + (int) Math.sqrt(n)) / Qs[j]);
            if (Ps[j] == Ps[j - 1]) {
                System.out.println(Qs[j - 1]);
                break;
            }
            j++;
        }
    }
        */

    public static boolean isPerfectSQRT(BigInteger x){
        BigInteger temp = SqRtN(x);
        return x.equals(temp.pow(2));
    }

    public static BigInteger isqrt(BigInteger x)
    {
        int b=x.bitLength(); // this is the next bit we try
        BigInteger r=BigInteger.ZERO; // r will contain the result
        BigInteger r2=BigInteger.ZERO; // here we maintain r squared
        while(b>=0)
        {
            BigInteger sr2=r2;
            BigInteger sr=r;
            // compute (r+(1<<b))**2, we have r**2 already.
            BigInteger tmp = r2;
            r2.add(tmp.shiftLeft(1+b));
            r2.add(BigInteger.ONE.shiftLeft(b+b));
            r.add(BigInteger.ONE.shiftLeft(b));
            if (r2.compareTo(x)>0)
            {
                r=sr;
                r2=sr2;
            }
            b--;
        }
        return r;
    }

    public static BigInteger SqRtN(BigInteger N)
    {
        BigInteger G = new BigInteger((N.shiftRight((N.bitLength() + 1) / 2)).toString());
        BigInteger LastG = null;
        BigInteger One = new BigInteger("1");
        while (true)
        {
            LastG = G;
            G = N.divide(G).add(G).shiftRight(1);
            int i = G.compareTo(LastG);
            if (i == 0) return G;
            if (i < 0)
            {
                if (LastG.subtract(G).compareTo(One) == 0)
                    if (G.multiply(G).compareTo(N) < 0 && LastG.multiply(LastG).compareTo(N) > 0) return G;
            }
            else
            {
                if (G.subtract(LastG).compareTo(One) == 0)
                    if (LastG.multiply(LastG).compareTo(N) < 0 && G.multiply(G).compareTo(N) > 0) return LastG;
            }
        }
    }

}
