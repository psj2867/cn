package kr.ac.sejong.web.exception;

public class EnvException extends WebServerException{    
    public EnvException(){};
    public EnvException(String message){super(message);};
    public EnvException(String message, Throwable cause){super(message,cause);};
    public EnvException(Throwable cause){super(cause);};
}
