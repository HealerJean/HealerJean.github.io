package com.healerjean.proj.server;

import com.healerjean.proj.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.server.*;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.*;
import org.junit.Test;

@Slf4j
public class Server {

    private static final int SERVER_PORT = 2345;//Thrift server listening port


    @Test
    public void start1(){
        Server server = new Server();
        // server.startTServer();
        // server.startTThreadPoolServer();
        // server.startTNonblockingServer();
        server.startTHsHaServer();
    }



    public void startTServer() {
        log.info("TServer start ....");
        try {
            UserService.Processor processor = new UserService.Processor<UserService.Iface>(new UserServiceImpl());
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);

            TServer.Args tArgs = new TServer.Args(serverTransport);
            tArgs.processor(processor);
            tArgs.protocolFactory(new TBinaryProtocol.Factory());
            tArgs.transportFactory(new TFramedTransport.Factory());

            // 简单的单线程服务模型，一般用于测试
            TServer server = new TSimpleServer(tArgs);
            server.serve();
        } catch (TTransportException e) {
            log.error("Server start error!!!", e);
        }

    }



    public void startTThreadPoolServer() {
        log.info("TThreadPoolServer start ....");
        try {

            UserService.Processor processor = new UserService.Processor<UserService.Iface>(new UserServiceImpl());
            TServerSocket serverTransport = new TServerSocket(SERVER_PORT);
            TThreadPoolServer.Args ttpsArgs = new TThreadPoolServer.Args(serverTransport);
            ttpsArgs.processor(processor);
            ttpsArgs.protocolFactory(new TBinaryProtocol.Factory());

            // 线程池服务模型，使用标准的阻塞式IO，预先创建一组线程处理请求。
            TServer server = new TThreadPoolServer(ttpsArgs);
            server.serve();

        } catch (Exception e) {
            log.error("Server start error!!!", e);
        }
    }

    public void startTNonblockingServer() {
        log.info("TNonblockingServer start ....");
        try {
            UserService.Processor processor = new UserService.Processor<UserService.Iface>(new UserServiceImpl());
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(SERVER_PORT);
            TNonblockingServer.Args tnbArgs = new TNonblockingServer.Args(tnbSocketTransport);
            tnbArgs.processor(processor);
            tnbArgs.transportFactory(new TFramedTransport.Factory());
            tnbArgs.protocolFactory(new TCompactProtocol.Factory());

            // 使用非阻塞式IO，服务端和客户端需要指定TFramedTransport数据传输的方式
            TServer server = new TNonblockingServer(tnbArgs);
            server.serve();

        } catch (Exception e) {
            log.error("Server start error!!!", e);
        }
    }




    public void startTHsHaServer() {
        log.info("THsHaServer start ....");
        try {
            UserService.Processor processor = new UserService.Processor<UserService.Iface>(new UserServiceImpl());
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(SERVER_PORT);
            THsHaServer.Args thhsArgs = new THsHaServer.Args(tnbSocketTransport);
            thhsArgs.processor(processor);
            thhsArgs.transportFactory(new TFramedTransport.Factory());
            thhsArgs.protocolFactory(new TBinaryProtocol.Factory());

            // 半同步半异步的服务模型
            TServer server = new THsHaServer(thhsArgs);
            server.serve();

        } catch (Exception e) {
            log.error("Server start error!!!", e);
        }
    }



}
