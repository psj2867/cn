package kr.ac.sejong.http;

public interface HttpHandler{
    HttpResponse handle(HttpRequest request);
}