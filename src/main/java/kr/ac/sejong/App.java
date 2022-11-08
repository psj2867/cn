package kr.ac.sejong;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) throws IOException {
        App app = new App();
        app.start();
    }

    public void start() throws IOException {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(8080);
            System.out.println("서버 시작 :8080 ");
            handleServer(ss);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (ss != null)
                ss.close();
        }
    }

    public void handleServer(ServerSocket ss) throws IOException {
        Socket socket = null;
        while (true) {
            try {
                socket = ss.accept();
                String request = getUntilNlNl(socket.getInputStream());
                String path = getPath(request);
                String fileContents = findFile(path);
                String response = "";
                if(fileContents == null) 
                    response = makeHttpResponse("404 Not Found","file not found");
                else
                    response = makeHttpResponse("200 OK", fileContents);                
                send(socket, response);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                if(socket != null)
                    socket.close();
            }
        }
    }

    public String getUntilNlNl(InputStream in) throws IOException {
        // readline 함수 쓰기 위해서
        BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.ISO_8859_1));
        StringBuilder sb = new StringBuilder();

        String line = br.readLine();
        while (!line.isBlank()) {
            sb.append(line).append("\n");
            line = br.readLine();
        }

        return sb.toString();
    }

    private final Pattern pattern = Pattern.compile("^(\\S)+ (\\S+) (\\S+)\n");

    public String getPath(String msg) {
        Matcher matcher = pattern.matcher(msg);
        matcher.find();
        return matcher.group(2);
    }

    private final String DEFAULT_FILE = "index.html";

    public String findFile(String path){
        if(path.equals("/"))
            path = path + DEFAULT_FILE;
        try (
            InputStream is = App.class.getResourceAsStream("/static"+path) ;
            BufferedReader br =new BufferedReader(new InputStreamReader(is)); ) {
            return br.lines().collect(Collectors.joining("\r\n"));
        } catch (Exception e) {
            return null;
        }         
    }

    public String makeHttpResponse(String code, String msg) {
        int length = msg.getBytes().length;
        StringBuilder sb = new StringBuilder();

        sb.append("HTTP/1.0 ").append(code).append("\r\n");
        sb.append("Content-Length: ").append(length).append("\r\n");
        sb.append("Content-Type: text/html").append("\r\n");        
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
