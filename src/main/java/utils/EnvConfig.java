package utils;

public final class EnvConfig extends PropertiesReader {
    private static volatile EnvConfig instance;

    private EnvConfig() {
        super("env.properties");
    }

    public static EnvConfig getInstance() {
        if (instance == null) {
            instance = new EnvConfig();
        }
        return instance;
    }
}
