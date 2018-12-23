package org.romanchi;

import java.io.IOException;

@Scan(packageToScan = "org.romanchi")
public class App {
    public static void main(String[] args) {
        try {
            Application.run(App.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
