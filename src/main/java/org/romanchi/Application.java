package org.romanchi;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());
    private static String base = "src/main/java/";
    private static ApplicationContext context = new ApplicationContext();
    private static void scanClass(Class clazz) throws IllegalAccessException, InstantiationException, InvocationTargetException, ClassNotFoundException {
        Annotation[] classAnnotations = clazz.getAnnotations();
        Field[] declaredFields = clazz.getDeclaredFields();
        Method[] declaredMethods = clazz.getDeclaredMethods();
        for(Method declaredMethod:declaredMethods){
            Annotation[] methodAnnotations = declaredMethod.getAnnotations();
            for(Annotation methodAnnotation:methodAnnotations){
                if(methodAnnotation instanceof Bean){
                    System.out.println(declaredMethod.getName());
                    declaredMethod.setAccessible(true);
                    Bean beanMethodAnnotation = (Bean) methodAnnotation;
                    Class returnType = declaredMethod.getReturnType();
                    String beanName = returnType.getCanonicalName();
                    Object createdBeanForReturnType = declaredMethod.invoke(clazz.newInstance());
                    System.out.println(((B)createdBeanForReturnType).value);
                    context.setBean(beanName, createdBeanForReturnType);
                }
            }
        }
        for(Field declaredField:declaredFields){
            Annotation[] fieldAnnotations = declaredField.getAnnotations();
            for(Annotation fieldAnnotation:fieldAnnotations){
                if(fieldAnnotation instanceof Wired){
                    //wired detected
                    declaredField.setAccessible(true);
                    Wired wiredFieldAnnotation = (Wired) fieldAnnotation;
                    ScopeType scopeType = wiredFieldAnnotation.scope();
                    System.out.println("scope: " + scopeType);
                    Class classToInject = declaredField.getType();
                    Object object = context.getBean(clazz);
                    Object objectToInject = context.getBean(classToInject, scopeType);
                    declaredField.set(object, objectToInject); // HERE WE NEED BEAN STORAGE
                    declaredField.setAccessible(false);
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
    public static void run(Class clazz)  {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = factory.newDocumentBuilder();
            Document doc = documentBuilder.parse(new File(App.class.getClassLoader().getResource("context.xml").getFile()));
            Element beans = doc.getDocumentElement();
            boolean isAnnotationDriven = Boolean.valueOf(beans.getElementsByTagName("annotation-driven").item(0).getTextContent());
            if(isAnnotationDriven){
                if(clazz.isAnnotationPresent(Scan.class)){
                    Scan scan = (Scan) clazz.getAnnotation(Scan.class);
                    String packageToScan = scan.packageToScan().replace(".","/");
                    scanFile(packageToScan,"org");
                    scanFile(packageToScan, "org");
                }else{
                    logger.info("packageToScan is no present");
                }
            }else{
                String packageBase = beans.getElementsByTagName("package-base").item(0).getTextContent();
                NodeList beanList = doc.getElementsByTagName("bean");
                for(int i = 0; i < beanList.getLength(); i++){
                    Node bean = beanList.item(i);
                    NamedNodeMap attributes = bean.getAttributes();
                    String beanClass = attributes.getNamedItem("class").getTextContent();
                    Class beanClazz = Class.forName(beanClass);
                    Object beanObject = context.getBean(beanClazz);
                    String name = attributes.getNamedItem("name")==null ? null:attributes.getNamedItem("name").getTextContent();

                    if(bean.getChildNodes().getLength() > 0){
                        NodeList childNodes = bean.getChildNodes();
                        for(int k = 0; k < childNodes.getLength(); k++){
                            Node node = childNodes.item(k);
                            if(node.getNodeType() == Element.ELEMENT_NODE){
                                Element propertyElement = (Element) node;
                                String propertyName = node.getAttributes().getNamedItem("name").getTextContent();
                                String value = node.getAttributes().getNamedItem("value") == null ? null : node.getAttributes().getNamedItem("value").getTextContent();
                                if(value == null){
                                    Node innerBean = propertyElement.getElementsByTagName("bean").item(0);
                                    String innerBeanClass = innerBean.getAttributes().getNamedItem("class").getTextContent();
                                    Object beanToinject = context.getBean(innerBeanClass);
                                    Field fieldToInject = beanClazz.getDeclaredField(propertyName);
                                    fieldToInject.setAccessible(true);
                                    fieldToInject.set(beanObject, beanToinject);
                                }
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static void setContext(ApplicationContext context) {
        Application.context = context;
    }
}
