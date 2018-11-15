package org.romanchi;

public class C {
    @Wired
    A a;

    public void printAll(){
        a.printB();
    }
}
