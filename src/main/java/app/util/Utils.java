package app.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Utils {

    private static volatile Utils instance;

    private Utils() {
    }

    public static Utils getInstance(){
        if(instance == null) {
            synchronized (Utils.class) {
                if (instance == null) {
                    instance = new Utils();
                }
            }
        }
        return instance;
    }

    public Properties loadProperties(String path) {
        try (FileInputStream fileInputStream = new FileInputStream(path)) {
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
