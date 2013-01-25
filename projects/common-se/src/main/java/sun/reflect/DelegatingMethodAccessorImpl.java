// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packfields(3) packimports(7) deadcode fieldsfirst splitstr(64) nonlb lnc radix(10) lradix(10) 
// Source File Name:   DelegatingMethodAccessorImpl.java

package sun.reflect;

import java.lang.reflect.InvocationTargetException;

// Referenced classes of package sun.reflect:
//            MethodAccessorImpl

class DelegatingMethodAccessorImpl extends MethodAccessorImpl {

            private MethodAccessorImpl _flddelegate;

            DelegatingMethodAccessorImpl(MethodAccessorImpl methodaccessorimpl) {
/*  19*/        setDelegate(methodaccessorimpl);
            }

            public Object invoke(Object obj, Object aobj[]) throws IllegalArgumentException, InvocationTargetException {
/*  25*/        return _flddelegate.invoke(obj, aobj);
            }

            void setDelegate(MethodAccessorImpl methodaccessorimpl) {
/*  29*/        _flddelegate = methodaccessorimpl;
            }
}
