package com.healerjean.proj.client;

import com.healerjean.proj.UserService;
import com.healerjean.proj.server.Server;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.*;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Slf4j
public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 2345;//Thrift server listening port
    private static final int TIMEOUT = 3000;



    @Test
    public void start1(){
        Client client = new Client();
        // client.startClient();
        // client.startTNonblockingClient();
        // client.startTHsHaClient();
        client.startTAsyncClient();
    }



    private void startClient() {
        TTransport transport = null;
        try {
            transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            UserService.Client client = new UserService.Client(protocol);
            transport.open();
            log.info("用户Id为1的用户 :{},[{}]", client.getName(1), client.isExist("HealerJean"));
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }


    public void startTNonblockingClient() {
        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));
            // 协议要和服务端一致
            TProtocol protocol = new TCompactProtocol(transport);
            UserService.Client client = new UserService.Client(protocol);
            transport.open();
            log.info("用户Id为1的用户 :{},[{}]", client.getName(1), client.isExist("HealerJean"));
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }




    public void startTHsHaClient() {
        TTransport transport = null;
        try {
            transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));
            // 协议要和服务端一致
            TProtocol protocol = new TBinaryProtocol(transport);
            UserService.Client client = new UserService.Client(protocol);
            transport.open();
            log.info("用户Id为1的用户 :{},[{}]", client.getName(1), client.isExist("HealerJean"));
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        } finally {
            if (null != transport) {
                transport.close();
            }
        }
    }


    public void startTAsyncClient() {
        try {
            TAsyncClientManager clientManager = new TAsyncClientManager();
            TNonblockingTransport transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);

            TProtocolFactory tprotocol = new TCompactProtocol.Factory();
            UserService.AsyncClient asyncClient = new UserService.AsyncClient(tprotocol, clientManager, transport);
            System.out.println("Client start .....");

            CountDownLatch latch = new CountDownLatch(1);
            AsynCallback callBack = new AsynCallback(latch);
            System.out.println("call method sayHello start ...");
            asyncClient.getName(1, callBack);
            System.out.println("call method sayHello .... end");
            boolean wait = latch.await(30, TimeUnit.SECONDS);
            System.out.println("latch.await =:" + wait);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("startClient end.");
    }

    public class AsynCallback implements AsyncMethodCallback<String> {

        private CountDownLatch latch;

        public AsynCallback(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        public void onComplete(String response) {
            System.out.println("AsynCall result =:" + response);
        }

        @Override
        public void onError(Exception exception) {
            System.out.println("onError :" + exception.getMessage());
            latch.countDown();
        }
    }



}
