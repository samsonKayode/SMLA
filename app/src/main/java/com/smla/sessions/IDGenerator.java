package com.smla.sessions;

/**
 * Created by SAMSON KAYODE on 09-Sep-2017.
 */

import java.security.SecureRandom;
import java.math.BigInteger;
import java.util.Random;

public class IDGenerator {

    static SecureRandom random = new SecureRandom();

    static Random rand = new Random();

    public static String nextSessionId()
    {
        return new BigInteger(40, random).toString(20);
    }

    public static int getInv(){

        int n = rand.nextInt(1999999)+107238;

        return n;

    }
}
