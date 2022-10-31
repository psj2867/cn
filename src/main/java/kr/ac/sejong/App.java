package kr.ac.sejong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 *
 */
public class App
{
    public static  void main( String[] args ){
        App app = new App();
        app.start();
    }
    public void start(  )
    {

        ServerSocket ss = null;
        Socket socket = null;
        try {
            ss = new ServerSocket(8080);
            System.out.println("서버 시작 :8080 ");
            while (true) {
                socket = ss.accept();
                // readline 함수 쓰기 위해서
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                try(in){
                    String msg = getUntilNlNl(in);
                    String response = makeHttpResponse("200 OK", getPath(msg));
                    send(socket, response);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                socket.close();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public String getUntilNlNl(BufferedReader in ) throws IOException {
        StringBuilder sb = new StringBuilder();
        while(true){
            String line = in.readLine();
            sb.append(line).append("\n");
            if(line.isBlank() ){
                return sb.toString();
            }
        }
    }

    private final Pattern pattern = Pattern.compile("^\\S+ (\\S+) \\S+\n");

    public String getPath(String msg){
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
        return matcher.group(1);
    }
    public String makeHttpResponse(String code, String msg){
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.0 ");
        sb.append(code);
        sb.append("\r\n");
        int length = msg.getBytes().length;
        sb.append("Content-length: ").append(length);
        sb.append("\r\n");
        sb.append("\r\n");
        sb.append(msg);
        return sb.toString();
    }
    public void send(Socket socket, String response) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream());
        writer.write(response);
        writer.flush();
    }

}
