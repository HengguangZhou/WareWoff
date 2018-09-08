import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * 客户端
 * @author ylg
 *
 */
public class Client {
    //客户端用于与服务端通信的socket
    private Socket socket;
    /**
     *初始化客户端相关内容
     */
    public Client(){
        try {
            /**
             * 实例化socket的过程就是连接的过程通常我们要传入两个参数
             * 1:字符串,服务器的IP地址
             * 2:整数,服务器端申请的端口号
             * (serversocket创建时申请的端口号:8088)
             */
            System.out.println("尝试连接");
            //此处的localhost可以改为运行服务端的那台电脑的的ip地址这样就可以连在一起聊天了
            //localhost指的是本机的ip
            socket =new Socket("localhost", 8088);
            System.out.println("连接成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 客户端用于交互的方法
     */
    public void start(){
        try {
            /**
             * 创建一个线程,用于读取服务器发过来的信息
             */
            Runnable hander=new GetMessageFromServerHandler();
            Thread t=new Thread(hander);
            t.start();
            /**
             * 客户端想向服务发送消息,通过socket花去输出流之后写出数据即可
             */
            OutputStream out=socket.getOutputStream();
            /**
             * 向服务器发送字符串,我们可以将字节流转换为缓冲字符流输出PrintWrint
             *
             */
            OutputStreamWriter osw=new OutputStreamWriter(out,"UTF-8");
            /**
             * 发送一个字符串就应当立即写出,所以要自动行刷新
             */
            PrintWriter pw=new PrintWriter(osw,true);
            /**
             * 创建scanner,将控制台输入的字符串通过pw发送给服务器
             */
            String message=null;
            Scanner scanner=new Scanner(System.in);
            while(true){
                message=scanner.nextLine();
                pw.println(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Client client=new Client();
        client.start();
    }
    /**
     * 该线程的作用是让客户端可以读取服务器发送过来的信息
     * @author ylg
     *
     */
    class GetMessageFromServerHandler implements Runnable{
        /**
         * 通过socket获取输入流,在转换为缓冲字符输入流
         * 最后通过循环都读取服务端发送的每一行信息
         */

        public void run() {
            try {
                InputStream in=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(in,"utf-8");
                BufferedReader br=new BufferedReader(isr);
                String message=null;
                while((message=br.readLine())!=null){
                    System.out.println(message);
                }
            } catch (Exception e) {

            }
        }

    }
}