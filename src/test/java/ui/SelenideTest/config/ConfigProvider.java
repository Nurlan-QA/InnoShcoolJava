package ui.SelenideTest.config;

import java.io.InputStream;
import java.util.Properties;

public class ConfigProvider {

    private static final Properties properties = new Properties();

    static {
        // Статический блок — выполняется один раз при первом обращении к классу.
        // Загружаем config.properties из classpath (src/test/resources)
        try (InputStream input = ConfigProvider.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException("config.properties не найден в classpath!");
            }
            properties.load(input);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при загрузке config.properties", e);
        }
    }

    // --- Геттеры для каждого параметра ---

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }

    public static String getApiUrl() {
        return properties.getProperty("api.url");
    }

    public static long getTimeout() {
        return Long.parseLong(properties.getProperty("timeout"));
    }

    public static String getLogMode() {
        return properties.getProperty("log.mode");
    }

    public static String getAdminLogin() {
        return properties.getProperty("admin.login");
    }

    public static String getAdminPassword() {
        return properties.getProperty("admin.password");
    }

    public static String getProductName() {
        return properties.getProperty("product.name");
    }

    public static String getProductPrice() {
        return properties.getProperty("product.price");
    }

    public static String getProductBigPrice() {
        return properties.getProperty("product.bigPrice");
    }
}
