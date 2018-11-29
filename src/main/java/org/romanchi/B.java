package org.romanchi;

import java.util.Random;

public class B {
    int value = new Random().nextInt();

    @Override
    public String toString(){
        return "Hello i am BI, value = " + value;
    }
}
