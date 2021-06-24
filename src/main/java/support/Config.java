package support;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.*;

public class Config {
    private static final String DEFAULT_CONFIG_PATH = "config/config.json";
    private static final String CONFIG_PATH_ENV_VAR_NAME = "JBOARD_CONFIG_PATH";

    private static JsonObject config;

    private static String getConfigPath(){
        String configPath = null;
        if ( new File(Config.DEFAULT_CONFIG_PATH).exists() ){
            configPath = Config.DEFAULT_CONFIG_PATH;
        }else{
            String path = System.getenv(Config.CONFIG_PATH_ENV_VAR_NAME);
            if ( path != null && !path.isEmpty() && new File(path).exists() ){
                configPath = path;
            }
        }
        return configPath;
    }

    public static void loadConfig() throws RuntimeException, IOException {
        String configPath = Config.getConfigPath();
        if ( configPath == null ){
            throw new RuntimeException("No configuration file found.");
        }
        File configFile = new File(configPath);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(configFile));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ( ( line = bufferedReader.readLine() ) != null ){
            stringBuilder.append(line);
        }
        Gson gson = new Gson();
        Config.config = gson.fromJson(stringBuilder.toString(), JsonObject.class);
    }

    public static JsonObject getConfig(){
        return Config.config;
    }
}
