package org.romanchi;

public class BFactory {
    @Bean
    public B createB(){
        B b = new B();
        b.value = 42;
        return b;
    }
}
