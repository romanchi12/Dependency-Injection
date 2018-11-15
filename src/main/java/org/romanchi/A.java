package org.romanchi;

public class A {
    @Wired
    private B b;

    public void printB(){
        System.out.println(b.toString());
    }
}
