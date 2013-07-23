package com.ra1ph.MTProtoClient.crypto;

import java.math.*;
import java.util.Random;

public class PMinusOne {

  private BigInteger two = BigInteger.valueOf(2);
  private BigInteger one = BigInteger.valueOf(1);
  private BigInteger zero = BigInteger.valueOf(0);

  private int length;
  private BigInteger A;
  private BigInteger D;
  private BigInteger N;
  
  public PMinusOne(BigInteger N, int length) {
    this.N = N;
    this.length = length;
  }

  private BigInteger PrimeMethod() {

    PrimeNumber myPrimeNum = new PrimeNumber(N, length);
    A = myPrimeNum.getNumberA();

    D = A.gcd(N);
    
    if (D.compareTo(two) >= 0) {
      return D;
    }

    D = A.subtract(one).gcd(N);

    if (D.compareTo(one) == 0 || D.compareTo(N) == 0) {
      return zero;
    }
    else {
      return D;
    }
  }

  public BigInteger getNumberD() {
    return this.PrimeMethod();
  }

    class MyRandomClass {

        private int length;
        private BigInteger RandomA = BigInteger.valueOf( -1);
        private BigInteger two = BigInteger.valueOf(2);
        private BigInteger one = BigInteger.valueOf(1);
        private BigInteger N;

        MyRandomClass(int length, BigInteger N) {
            this.length = length;
            this.N = N;

            Random sr = new Random();
            while ( (RandomA.compareTo(two) < 0) ||
                    (RandomA.compareTo(N.subtract(one)) > 0)) {
                RandomA = new BigInteger(length, sr);
            }
        }

        public BigInteger getRandomNum() {
            return this.RandomA;
        }
    }

    class PrimeNumber {

        private BigInteger one = BigInteger.valueOf(1);
        private int[] BaseArray = {2, 3, 5, 7, 11, 13, 17, 19};
        private BigInteger A, tmpA;
        private BigInteger N;
        private BigInteger Q, QpowL;
        private int length, l;

        PrimeNumber(BigInteger N, int length) {
            this.N = N;

            MyRandomClass myRand = new MyRandomClass(length, N);
            A = myRand.getRandomNum();

            for (int j = 0; j < BaseArray.length; j++) {
                Q = BigInteger.valueOf(BaseArray[j]);
                l = (int) (Math.log(N.floatValue()) / Math.log(Q.floatValue()));
                QpowL = Q.pow(l);
                A = A.modPow(QpowL, N);

                if(A.compareTo(one) == 0){
                    break;
                }
                tmpA = A;
            }
        }

        public BigInteger getNumberA() {
            return this.tmpA;
        }
    }
}

/*
public class FactorPollard {
   public static void main(String[] args) {
    
    BigInteger one = BigInteger.valueOf(1);
    boolean idiot;
    int length = 4; // ����� ������������� ���������� �����
       
    BigInteger ResultValD; 
            
      do {
         idiot = false;
         try {
            BigInteger N = new BigInteger(JOptionPane.showInputDialog("Plz, enter no prime BigInteger number more 1")); //  .intValue();
            
            if (N.compareTo(one) <= 0) {
               idiot = true;
               JOptionPane.showMessageDialog (null, "Invalid integer entered!!!");
            } else {
                if (N.isProbablePrime(50)) {
    		  		JOptionPane.showMessageDialog (null, "Sorry, your number is Probable Prime...");
   		      	}else {
                	PMinusOne myMethod = new PMinusOne(N, length);
              		ResultValD = myMethod.getNumberD();
                	JOptionPane.showMessageDialog (null, ResultValD + " divides " + N + ".");
              	}
            } 
         
         } 
         catch (NumberFormatException e) 
         {
         	idiot = true;
         	JOptionPane.showMessageDialog(null,e.toString());
         }
      } while (idiot);
      
      System.exit(0);
   }


}*/
