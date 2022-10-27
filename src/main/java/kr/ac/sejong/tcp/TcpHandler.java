package kr.ac.sejong.tcp;

import java.net.Socket;

public interface TcpHandler {
    void handle(Socket socket);
}
