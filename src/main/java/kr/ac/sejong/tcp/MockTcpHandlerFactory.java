package kr.ac.sejong.tcp;

public class MockTcpHandlerFactory implements TcpHandlerFactory {

    @Override
    public TcpHandler create() {
        return new MockTcphandler();
    }
    
}
