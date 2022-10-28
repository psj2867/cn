package kr.ac.sejong.tcp;

import java.net.Socket;

public class MockTcphandler implements TcpHandler {

    @Override
    public void handle(Socket socket) {        
        return;
    }
    
}
