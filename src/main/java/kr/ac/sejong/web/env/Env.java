package kr.ac.sejong.web.env;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kr.ac.sejong.util.CommonUtil;
import kr.ac.sejong.web.exception.EnvException;

public class Env {

    private static void setIfNotNUll(Map<String, String> envs, EnvKeys key, String value){
        if(CommonUtil.isEmpty(value))
            return;
        envs.put(key.getKey(), value);
    }
    public static void printHelp(){
        printHelp(server_options());
    }
    private static void printHelp(Options options){
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(  EnvKeys.PROGRAM_NAME.getKey() + " [ip [port]] [option..] " , options );
    }

    public static void valid(Map<String,String> setting){
        Stream.of(EnvKeys.values())
            .forEach(EnvKeys.getValidator(setting));
    }

    public static Map<String,String> args_env(String[] args) throws EnvException {       
        Map<String, String> envs = new HashMap<>();
        Options options = server_options();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);            
            if(line.hasOption("help")) {
                throw new EnvException();
            }
            setIfNotNUll(envs, EnvKeys.THR, line.getOptionValue("n"));
            setIfNotNUll(envs, EnvKeys.FILE, line.getOptionValue("f"));
            List<String> rest_args = line.getArgList();
            if(rest_args.size() > 0 ){
                setIfNotNUll(envs, EnvKeys.IP, rest_args.get(0));                
            }
            if(rest_args.size() > 1 ){
                setIfNotNUll(envs, EnvKeys.PORT, rest_args.get(1));
            }           
        } catch (ParseException exp) {
            throw new EnvException(exp.getMessage());
        }
        return envs;
    }

    public static Options server_options(){
        Options options = new Options();

        Option thread = new Option("n", "thread-count", true, "server thread count");
        options.addOption(thread);
        Option file = new Option("f", "file", true, "setting file");
        options.addOption(file);
        Option help = new Option("h", "help", false, "print this message");
        options.addOption(help);

        return options;
    }

    public static Map<String,String> defualt_env(){
        return Stream.of(EnvKeys.values())
            .filter( eL -> !CommonUtil.isEmpty(eL.getDefualt_value()) )
            .collect(Collectors.toMap(EnvKeys::getKey,EnvKeys::getDefualt_value));
    }

    public static Map<String,String> system_env(){
        return new CacheMap<>(Env::getSystemEnv);
    }
    public static String getSystemEnv(String k){
        String v = System.getenv(k);
        if(CommonUtil.isEmpty(v))
            return null;
        return v;
    }
}
