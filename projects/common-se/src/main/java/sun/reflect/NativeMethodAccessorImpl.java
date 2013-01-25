// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packfields(3) packimports(7) deadcode fieldsfirst splitstr(64) nonlb lnc radix(10) lradix(10) 
// Source File Name:   NativeMethodAccessorImpl.java

package sun.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

// Referenced classes of package sun.reflect:
//            MethodAccessorImpl, DelegatingMethodAccessorImpl, MethodAccessorGenerator, ReflectionFactory

class NativeMethodAccessorImpl extends MethodAccessorImpl {

            private Method method;
            private DelegatingMethodAccessorImpl parent;
            private int numInvocations;

            NativeMethodAccessorImpl(Method method1) {
/*  21*/        method = method1;
            }

            public Object invoke(Object obj, Object aobj[]) throws IllegalArgumentException, InvocationTargetException {
/*  27*/        if(++numInvocations > ReflectionFactory.inflationThreshold()) {
/*  28*/            MethodAccessorImpl methodaccessorimpl = (MethodAccessorImpl)(new MethodAccessorGenerator()).generateMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), method.getReturnType(), method.getExceptionTypes(), method.getModifiers());
/*  36*/            parent.setDelegate(methodaccessorimpl);
                }
/*  39*/        return invoke0(method, obj, aobj);
            }

            void setParent(DelegatingMethodAccessorImpl delegatingmethodaccessorimpl) {
/*  43*/        parent = delegatingmethodaccessorimpl;
            }

            private static native Object invoke0(Method method1, Object obj, Object aobj[]);
}
