package kr.ac.sejong.web.env;

import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Consumer;

import kr.ac.sejong.web.env.validator.PatternValidator;

public enum EnvKeys {

    IP("0.0.0.0",new PatternValidator("IP","[0-9]*\\.[0-9]*\\.[0-9]*\\.[0-9]*")),
    PORT("80", new PatternValidator("PORT","[0-9]*")),
    THR("5",new PatternValidator("THR","[0-9]*")),
    CWD(Paths.get("").toAbsolutePath().normalize().toString(), EnvValidator.Idenfiy),
    FILE(null, EnvValidator.Idenfiy),
    PROGRAM_NAME("cn", EnvValidator.NotNull("PROGRAM_NAME")),
    LOGGER("console", EnvValidator.NotNull("LOGGER")),
    LOGGER_LEVEL("INFO", EnvValidator.NotNull("LOGGER_LEVEL")),
    ;

    private String key;
    private String defualt_value;
    private EnvValidator validator;


    EnvKeys(String defualt_value){        
        this(null, defualt_value);
    }
    EnvKeys(String defualt_value, EnvValidator validator){        
        this(null, defualt_value);
        this.validator = validator;
    }
    EnvKeys(String key, String defualt_value){
        this.key = key;
        this.defualt_value = defualt_value;
    }
    public EnvValidator getValidator() {
        return validator;
    }
    public static Consumer<EnvKeys> getValidator(Map<String,String> setting) {
        return ( keyL) -> keyL.getValidator().valid(setting.get(keyL.getKey()));
    }
    public String getKey(){
        if(this.key != null)
            return this.key;
        this.key = name();
        return this.key;
    }
    
    public String getDefualt_value() {
        return defualt_value;
    }
    
}
