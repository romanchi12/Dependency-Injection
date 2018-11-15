package org.romanchi;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());
    private static String base = "src/main/java/";
    private static ApplicationContext context = new ApplicationContext();
    private static void scanClass(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Annotation[] classAnnotations = clazz.getAnnotations();
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field declaredField:declaredFields){
            Annotation[] fieldAnnotations = declaredField.getAnnotations();
            for(Annotation fieldAnnotation:fieldAnnotations){
                if(fieldAnnotation instanceof Wired){
                    //wired detected
                    declaredField.setAccessible(true);
                    Class classToInject = declaredField.getType();
                    Object object = context.getBean(clazz);
                    Object objectToInject = context.getBean(classToInject);
                    declaredField.set(object, objectToInject); // HERE WE NEED BEAN STORAGE
                }
            }
        }
    }

    private static void scanFile(String fileName, String accumulatedParentDir) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, InvocationTargetException {
        accumulatedParentDir = accumulatedParentDir + "/";
        File file = new File(base + accumulatedParentDir + fileName);
        if(file.isDirectory()){
            String[] filesToScan = file.list();
            for(String fileToScan:filesToScan){
                scanFile(fileToScan, accumulatedParentDir+file.getName());
            }
        }else{
            if(file.getCanonicalPath().endsWith(".java")){
                //scan
                String filePath = accumulatedParentDir + fileName;
                String classToLoad = filePath.substring(0,filePath.lastIndexOf(".")).replace("/",".");
                Class clazz = Class.forName(classToLoad);
                scanClass(clazz);
            }

        }
    }
    public static void run(Class clazz) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        if(clazz.isAnnotationPresent(Scan.class)){
            Scan scan = (Scan) clazz.getAnnotation(Scan.class);
            String packageToScan = scan.packageToScan().replace(".","/");
            scanFile(packageToScan,"org");
        }else{
            logger.info("packageToScan is no present");
        }
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        Application.context = context;
    }
}
