package com.ra1ph.MTProtoClient.tl.vector;

import com.ra1ph.MTProtoClient.tl.TLObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: p00rGen
 * Date: 20.07.13
 * Time: 21:33
 * To change this template use File | Settings | File Templates.
 */
public abstract class Vector extends TLObject {
    public abstract List getElements();
}
