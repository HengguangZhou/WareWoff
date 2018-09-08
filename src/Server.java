import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 服务端
 * @author ylg
 *
 */
public class Server {
    /**
     * 用于与客户端连接的ServerSOocket
     */
    private ServerSocket server;
    /**
     * 存放所有客户端的输入流,用于广播信息
     */
    private List<PrintWriter> allOut;
    /**
     * 线程池,用于控制服务端线程数量,并重用线程
     */
    private ExecutorService threadPool;
    /**
     * 构造方法,用于初始化服务器相关内容
     *
     */
    public Server(){
        try {
            //初始化ServerSocket
            /**
             * 初始化时要求我们传入一个整数,这个整数表示端口号,客户端就是
             * 通过这个端口号连接到服务端的
             */
            server=new ServerSocket(8088);
            /**
             * 初始化存放所有客户端输出流的家集合
             */
            allOut =new ArrayList<PrintWriter>();
            //初始化线程池
            threadPool=Executors.newFixedThreadPool(50);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 将给定的输出流存入共享集合中
     * @param out
     */
    private synchronized void addOut(PrintWriter out){
        allOut.add(out);
    }
    /**
     * 从共享集合中删除给定的删除流
     * @param out
     */
    private synchronized void removeOut(PrintWriter out){
        allOut.remove(out);
    }

    //还要遍历方法,并且三个操作集合的方法互斥
    /**
     * 遍历所有的输出流将给定的字符串发送给所有客户端
     * @param message 服务器接收到的消息
     */
    private synchronized void sendMsgToAllClient(String message){
        for(PrintWriter pw:allOut){
            pw.println(message);
        }
    }
    /**
     * 服务端开始工作的方法
     */
    public void start(){
        try {
            /**
             * socket accept()
             * 该方法是一个阻塞方法,用于等待客户端的连接
             * 一旦一个客户端连接上,该方法就会返回与该客户端通信socket
             */
            System.out.println("等待客户端的连接...");
            /**
             * 死循环的目的是一直监听不同客户端的连接
             */
            while(true){
                Socket socket=server.accept();
                System.out.println("一个客户端连接上了...");
                /**
                 * 当一个客户端连接后,启动一个线程,将该客户端的socket传入,
                 * 是该线程与客户端通信
                 */
                Runnable clientHandler=new ClientHandler(socket);
//              Thread t=new Thread(clientHandler);
//              t.start();
                threadPool.execute(clientHandler);
            }


        } catch (Exception e) {
        }finally {

        }
    }
    public static void main(String[] args) {
        Server server =new Server();
        server.start();
    }
    /**
     * 该线程的作用是与给定的客户端Socket进行通信
     * @author ylg
     *
     */
    class ClientHandler implements Runnable{
        /**
         * 当前线程用于交流的指定客户端的Socket
         */
        private Socket socket;
        /**
         * 创建线程体时将交互的Socket传入
         * @param socket
         */
        public ClientHandler(Socket socket){
            this.socket=socket;
        }
        /**
         * 定义在try外面是因为finally中要引用
         */
        PrintWriter pw=null;
        public void run(){
            try {
                /**
                 * 通过socket获取输出流,用于将信息发送给客户端
                 */
                OutputStream out=socket.getOutputStream();
                OutputStreamWriter osw=new OutputStreamWriter(out, "utf-8");
                pw=new PrintWriter(osw,true);
                /**
                 * 将该客户端的输出流存入共享集合
                 */
                addOut(pw);
                /**
                 * 通过连接上的客户端的socket获取输入流来读取客户端发送过来的信息
                 */
                InputStream in=socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(in,"UTF-8");
                /**
                 * 包装为缓冲流字符输入流,可以按行读取字符串
                 */
                BufferedReader br=new BufferedReader(isr);
                String message=null;
                while((message=br.readLine())!=null){
                    //将当前的发送的消息广播给所有客户端
                    sendMsgToAllClient(message);
                /*  //System.out.println("客户端说: "+message); 
                    //将读取到的信息发送给客户端
                    pw.println(message);*/
                    //在服务端上显示
                    System.out.println(message);
                }
            } catch (Exception e) {
            }finally{
                /**
                 * linux客户端若断开连接,服务端会读取到null
                 * windows的客户端断开连接,服务端会抛出异常
                 * 所以finally是我们最后处理的最佳地点
                 */
                System.out.println("客户端下线");
                /**
                 * 当客户端断开后,将其输出流从共享集合中删除
                 */
                removeOut(pw);
                /**
                 * 输出在线人数
                 */
                System.out.println("当前在线人数"+allOut.size());
                /**
                 * 不同分别关闭输入流与输出流
                 * 关闭socket即可,因为这两个流都是从socket获取的,就好比打电话
                 * 我们最终挂断电话就自然断开了麦克风和听筒一样
                 */
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
