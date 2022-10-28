package kr.ac.sejong.http;

public class MockHttpHandler implements HttpHandler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        HttpResponse res = new HttpResponse();
        return res;
    }
    
}
