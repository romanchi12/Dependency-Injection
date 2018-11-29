package org.romanchi;

public class A {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        A a = (A) o;

        return b != null ? b.equals(a.b) : a.b == null;
    }

    @Override
    public int hashCode() {
        return b != null ? b.hashCode() : 0;
    }

    @Wired
    private B b;

    public void printB(){
        System.out.println(b.toString());
    }
}
