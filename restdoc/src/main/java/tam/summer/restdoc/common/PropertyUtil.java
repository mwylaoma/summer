package tam.summer.restdoc.common;

import javax.servlet.ServletContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by tanqimin on 2015/11/19.
 */
public class PropertyUtil {
    private static Properties properties = null;
    private static String config_file_path = "/WEB-INF/config.properties";

    private PropertyUtil() {
    }

    public static void createInstance(ServletContext servletContext) {
        if (properties == null) {
            properties = new Properties();
            String configPath = servletContext.getRealPath("/") + config_file_path;
            try {
                properties.load(new FileInputStream(configPath));
            } catch (IOException e) {
                throw new RuntimeException("Load properties file [" + configPath + "] fail");
            }
        }
    }

    public static Properties getProperties() {
        if (properties == null)
            throw new RuntimeException("Please call createInstance method to init");
        return properties;
    }

    public static String getProperty(String key){
        if (properties == null)
            throw new RuntimeException("Please call createInstance method to init");
        String value = properties.getProperty(key);
        try {
            return new String(value.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding");
        }
    }
}
