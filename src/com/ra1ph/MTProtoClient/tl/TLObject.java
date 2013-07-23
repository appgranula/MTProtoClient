package com.ra1ph.MTProtoClient.tl;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public abstract class TLObject {

    static String constructor;
    static int hashConstructor;

    public abstract byte[] serialize();
    public abstract TLObject deserialize(byte[] byteData);

}
