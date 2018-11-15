package org.romanchi;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

@Scan(packageToScan = "romanchi")
public class App {
    public static void main( String[] args ) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException, ParserConfigurationException, SAXException, NoSuchFieldException {
        Application.run(App.class);
        launch();
    }
    public static void launch() throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        ApplicationContext context = Application.getContext();
        A a = context.getBean(A.class);
        a.printB();
    }
}
