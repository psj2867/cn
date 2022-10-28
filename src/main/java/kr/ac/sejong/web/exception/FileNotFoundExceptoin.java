package kr.ac.sejong.web.exception;

public class FileNotFoundExceptoin extends WebServerException {
    public FileNotFoundExceptoin(){};
    public FileNotFoundExceptoin(String message){super(message);};
    public FileNotFoundExceptoin(String message, Throwable cause){super(message,cause);};
    public FileNotFoundExceptoin(Throwable cause){super(cause);};

}
