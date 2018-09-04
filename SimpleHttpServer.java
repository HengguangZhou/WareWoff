import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class SimpleHttpServer {
  public static void main(String[] args) throws IOException{
    final ServerSocket server = new ServerSocket(8080);
    System.out.println("Listening for connection on port 8080 ....");
    while (true){
        try (Socket socket = server.accept()){
            String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "Lolololol";
            socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
        }
    }
  }
}
