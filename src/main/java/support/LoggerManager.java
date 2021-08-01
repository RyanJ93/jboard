package support;

import com.google.gson.JsonObject;
import io.sentry.Sentry;

public class LoggerManager {
    public static void setupSentry(){
        JsonObject config = Config.getConfig();
        if ( config.has("sentry") ){
            JsonObject sentryConfig = config.getAsJsonObject("sentry");
            if ( sentryConfig != null && sentryConfig.has("dsn") ){
                boolean isDebug = sentryConfig.has("debug") && sentryConfig.get("debug").getAsBoolean();
                String sentryDSN = sentryConfig.get("dsn").getAsString();
                Sentry.init(options -> {
                    options.setTracesSampleRate(1.0);
                    options.setDsn(sentryDSN);
                    options.setDebug(isDebug);
                });
            }
        }
    }
}
