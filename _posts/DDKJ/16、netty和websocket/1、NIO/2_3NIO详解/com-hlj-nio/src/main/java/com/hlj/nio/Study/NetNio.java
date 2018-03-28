package com.hlj.nio.Study;

import sun.applet.Main;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Description
 * @Author HealerJean
 * @Date 2018/3/28  下午12:20.
 */
public class NetNio {


    public static void main(String[] args) throws IOException {
        listen();

    }

   static int port = 1111;

    /*
     * 注册事件
     * */
    protected  static  Selector getSelector() throws IOException {
        // 创建Selector对象
        Selector selector = Selector.open();

        // 创建可选择通道，并配置为非阻塞模式
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        // 绑定通道到指定端口
        ServerSocket socket = serverSocketChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        socket.bind(address);

        // 向Selector中注册感兴趣的事件,
        // 即指定我们想要监听accept事件，也就是新的连接发 生时所产生的事件，
        // 对于ServerSocketChannel通道来说，我们唯一可以指定的参数就是OP_ACCEPT。
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        return selector;
    }


    /*
     * 开始监听
     * */
    public static void listen() throws IOException {

        Selector selector = getSelector();
        System.out.println("listen on " + port);
        try {
            while(true) {
                //int select(long timeout)：select()一样，除了最长会阻塞timeout毫秒(参数)
                //int selectNow()： 不会阻塞，不管什么通道就绪都立刻返回，此方法执行非阻塞的选择操作。
                //                  如果自从前一次选择操作后，没有通道变成可选择的，则此方法直接返回零。
                // int select() 该调用会阻塞，阻塞到至少有一个通道在你注册的事件上就绪，方法将返回所发生的事件的数量。

                //select()方法返回的int值表示有多少通道已经就绪。亦即，自上次调用select()方法后有多少通道变成就绪状态。
                // 如果调用select()方法，因为有一个通道变成就绪状态，返回了1，
                // 若再次调用select()方法，如果另一个通道就绪了，它会再次返回1。
                // 如果对第一个就绪的channel没有做任何操作，现在就有两个就绪的通道，
                // 但在每次select()方法调用之间，只有一个通道处于就绪状态。
                int num  = selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = keys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey key = (SelectionKey) keyIterator.next();

                    //Selector对象并不会从自己的selected key集合中自动移除SelectionKey实例。
                    // 我们需要在处理完一个Channel的时候自己去移除。当下一次Channel就绪的时候，
                    // Selector会再次把它添加到selected key集合中。
                    keyIterator.remove();
                    process(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * 根据不同的事件做处理
     * */
    protected static void process(SelectionKey key) throws IOException{
        Selector selector = getSelector();
        // 接收请求，Channel中什么事件或操作已经就绪
        if (key.isAcceptable()) {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel channel = server.accept();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
        }

    }
}
