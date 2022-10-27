package kr.ac.sejong.web;

import kr.ac.sejong.tcp.TcpHandler;

public interface TcpHandlerFactory {
    TcpHandler create();
}
