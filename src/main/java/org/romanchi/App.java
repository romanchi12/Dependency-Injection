package org.romanchi;

import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Scan(packageToScan = "romanchi")
public class App {
    public static void main( String[] args ) {
        Application.run(App.class);
        launch();
    }
    public static void launch()  {
        ApplicationContext context = Application.getContext();
        A a = context.getBean(A.class);
        a.printB();
    }
}
