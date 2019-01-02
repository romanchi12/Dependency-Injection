package org.romanchi;

import java.io.IOException;

@Scan(packageToScan = "org.romanchi")
public class App {
    public static void main(String[] args) {
        try {
            Application.run(App.class);
            Controller controller = Application.getContext().getBean(UserController.class);
            System.out.println(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
