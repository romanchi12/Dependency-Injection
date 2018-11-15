package org.romanchi;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Scan(packageToScan = "romanchi")
public class App {
    public static void main( String[] args ) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        Application.run(App.class);
        launch();
    }
    public static void launch() throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ApplicationContext context = Application.getContext();
        C c = (C)context.getBean(C.class);
        c.printAll();
    }
}
