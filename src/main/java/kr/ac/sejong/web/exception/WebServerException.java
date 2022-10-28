package kr.ac.sejong.web.exception;

public class WebServerException extends RuntimeException{
    public WebServerException(){};
    public WebServerException(String message){super(message);};
    public WebServerException(String message, Throwable cause){super(message,cause);};
    public WebServerException(Throwable cause){super(cause);};
}
