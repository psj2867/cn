package kr.ac.sejong.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import kr.ac.sejong.util.CommonUtil;
import kr.ac.sejong.web.env.ChainMap;
import kr.ac.sejong.web.env.Env;
import kr.ac.sejong.web.env.EnvKeys;
import kr.ac.sejong.web.env.YamlParser;
import kr.ac.sejong.web.exception.WebServerException;

public class WebServer {
    private final Logger logger = Logger.getLogger(WebServer.class.getName());
    // SEVERE (최고치)
    // WARNING
    // INFO
    // CONFIG
    // FINE
    // FINER
    // FINEST (최저치)
    private final ChainMap<String, String> setting
        = new ChainMap<>(Arrays.asList(
        Env.system_env(),
        Env.defualt_env()
        ));
    private Object httpManager = new HashMap<>();
    private Thread tcpServer = new Thread(()->{
        while(true){
            System.out.println("abc");
            try {
                Thread.sleep(1 * 1000);
            } catch (InterruptedException e) {
            }
        }
    });

    // args > file > env > default

    public WebServer() {
        setLogger();
        logger.config("env - read system environment variable");
        this.httpManager = defaultManager();
        Env.valid(this.setting);
        setLogger();
    }

    public WebServer(String[] args) throws WebServerException {
        setLogger();
        logger.config("env - read system environment variable");
        logger.config("env - read args ");
        Map<String, String> args_env = Env.args_env(args);

        addFileSetting(args_env.get(EnvKeys.FILE.getKey()));
        this.setting.addFirst(args_env);
        Env.valid(this.setting);
    }

    private void addFileSetting(String file) throws WebServerException {
        if (CommonUtil.isEmpty(file)){
            this.httpManager = defaultManager();
            return;
        }
        YamlParser file_setting = new YamlParser(file);

        logger.config("env - read file setting " + file );

        this.setting.addFirst(file_setting.getRootEnv());
        this.httpManager = file_setting.getHttpManagement();
    }
    private Object defaultManager(){
        return null;
    }

    private void setLogger(){
        Logger root = Logger.getGlobal();
        root.setLevel(Level.INFO);
        root.addHandler(new ConsoleHandler());
    }


    public void start_server() {
        this.setShutdownHook();
        this.tcpServer.setDaemon(true);
        this.tcpServer.start();

        String ip = this.setting.get(EnvKeys.IP.getKey());
        String port = this.setting.get(EnvKeys.PORT.getKey());
        logger.info("server - server start " + ip + ":" + port );
        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                if(sc.nextLine().equals("quit"))
                    break;
            }            
        }
    }

    public static void printHelp(){
        Env.printHelp();
    }
    public static void printHelp(Exception e){
        if(e.getMessage() != null)
            System.out.println(e.getMessage());
        Env.printHelp();
    }
    
    
    private void setShutdownHook(){
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }
    private final int SHUTDOWN_TIMEOUT_MS = 5 * 1000;
    private void shutdown() {
        logger.info("\nserver - The server is being stopped...");
        this.tcpServer.interrupt();
        try {
            this.tcpServer.join( SHUTDOWN_TIMEOUT_MS );
        } catch (InterruptedException e) {
            logger.warning("time out - shutdown force");
        }
        logger.info("server - stop server");
    }

}
