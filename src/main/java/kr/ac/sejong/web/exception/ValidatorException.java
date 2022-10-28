package kr.ac.sejong.web.exception;

public class ValidatorException extends WebServerException{    
    public ValidatorException(){};
    public ValidatorException(String message){super(message);};
    public ValidatorException(String message, Throwable cause){super(message,cause);};
    public ValidatorException(Throwable cause){super(cause);};
}
