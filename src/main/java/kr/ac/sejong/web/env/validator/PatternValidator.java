package kr.ac.sejong.web.env.validator;

import kr.ac.sejong.web.env.EnvValidator;
import kr.ac.sejong.web.exception.ValidatorException;

public class PatternValidator implements EnvValidator{
    private final String pattern;
    private final String type;
    public PatternValidator(String type, String pattern){
        this.type = type;
        this.pattern = pattern;
    }
    @Override
    public void valid(String t) {
        if(!t.matches(pattern))
            throw new ValidatorException( "env(" + type +") - "+ t +  " not matched pattern " + pattern);
    }
    
}
