import support.Config;
import support.Database;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class InitServletContextListener implements ServletContextListener {
    public void contextInitialized(ServletContextEvent contextEvent) {
        try{
            Config.loadConfig();
            Database.setup();
        }catch(Exception ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
