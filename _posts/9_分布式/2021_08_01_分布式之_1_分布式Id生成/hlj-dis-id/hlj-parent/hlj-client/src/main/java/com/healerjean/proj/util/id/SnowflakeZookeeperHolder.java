package com.healerjean.proj.util.id;

import com.healerjean.proj.common.exception.PublicBasicException;
import com.healerjean.proj.util.json.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryUntilElapsed;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SnowflakeZookeeperHolder {

    /**
     * workerId
     */
    private int workerId;
    /**
     * ip
     */
    private String ip;
    /**
     * port
     */
    private String port;
    /**
     * zookeeperConnectionString
     */
    private String zookeeperConnectionString;
    /**
     * listenAddress
     */
    private String listenAddress;
    /**
     * zk_AddressNode
     */
    private String zk_AddressNode;
    /**
     * lastUpdateTime
     */
    private long lastUpdateTime;
    /**
     * PATH_FOREVER
     */
    private static final String PATH_FOREVER = "/public/idgenerate/snowflake/forever";

    /**
     * TEMP_PATH
     */
    private static final String TEMP_PATH = "/home/work/temp";
    /**
     * PROJECT_NAME
     */
    private static final String PROJECT_NAME = "public";
    /**
     * PROP_PATH
     */
    private static final String PROP_PATH = TEMP_PATH + File.separator + PROJECT_NAME + "/conf/{port}/workerID.properties";


    public SnowflakeZookeeperHolder(String ip, String port, String zookeeperConnectionString) {
        this.ip = ip;
        this.port = port;
        this.listenAddress = ip + ":" + port;
        this.zookeeperConnectionString = zookeeperConnectionString;
    }

    public boolean init() {
        CuratorFramework curator = createWithOptions(zookeeperConnectionString, new RetryUntilElapsed(1000, 4), 10000, 6000);
        curator.start();
        try {
            Stat stat = curator.checkExists().forPath(PATH_FOREVER);
            if (stat == null) {
                //不存在根节点,机器第一次启动,创建/snowflake/ip:port-000000000,并上传数据
                zk_AddressNode = createNode(curator);
                //worker id 默认是0
                updateLocalWorkerID(workerId);
                //定时上报本机时间给forever节点
                scheduledUploadData(curator, zk_AddressNode);
            } else {
                Map<String, Integer> nodeMap = new HashMap();
                Map<String, String> realNode = new HashMap();
                //存在根节点,先检查是否有属于自己的根节点
                List<String> keys = curator.getChildren().forPath(PATH_FOREVER);
                for (String key : keys) {
                    String[] nodeKey = key.split("-");
                    realNode.put(nodeKey[0], key);
                    nodeMap.put(nodeKey[0], Integer.parseInt(nodeKey[1]));
                }
                Integer workerid = nodeMap.get(listenAddress);
                if (workerid != null) {
                    //有自己的节点,zk_AddressNode=ip:port
                    zk_AddressNode = PATH_FOREVER + "/" + realNode.get(listenAddress);
                    workerId = workerid;
                    if (!checkInitTimeStamp(curator, zk_AddressNode)) {
                        throw new PublicBasicException(500, "init timestamp check error,forever node timestamp gt this node time");
                    }
                    //准备创建临时节点
                    scheduledUploadData(curator, zk_AddressNode);
                    updateLocalWorkerID(workerId);
                    log.info("[Old NODE]find forever node have this endpoint ip-{} port-{} workid-{} childnode and start SUCCESS", ip, port, workerId);
                } else {
                    //表示新启动的节点,创建持久节点 ,不用check时间
                    String newNode = createNode(curator);
                    zk_AddressNode = newNode;
                    String[] nodeKey = newNode.split("-");
                    workerId = Integer.parseInt(nodeKey[1]);
                    scheduledUploadData(curator, zk_AddressNode);
                    updateLocalWorkerID(workerid);
                    log.info("[New NODE]can not find node on forever node that endpoint ip-{} port-{} workid-{},create own node on forever node and start SUCCESS ", ip, port, workerId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 创建持久顺序节点 ,并把节点数据放入 value
     *
     * @param curator
     * @return
     * @throws Exception
     */
    private String createNode(CuratorFramework curator) throws Exception {
        try {
            return curator.create().creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath(PATH_FOREVER + "/" + listenAddress + "-", timeData().getBytes());
        } catch (Exception e) {
            log.error("创建永久节点失败 {} ", e.getMessage());
            throw e;
        }
    }

    /**
     * 在节点文件系统上缓存一个workid值,zk失效,机器重启时保证能够正常启动
     *
     * @param
     */
    private void updateLocalWorkerID(int workerID) {
        File leafConfFile = new File(PROP_PATH.replace("{port}", port));
        boolean exists = leafConfFile.exists();
        if (exists) {
            try {
                FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID, "UTF-8", false);
                log.info("文件缓存的workerID: {}", workerID);
            } catch (IOException e) {
                log.error("文件缓存获取失败", e);
            }
        } else {
            //不存在文件,父目录页肯定不存在
            try {
                boolean mkdirs = leafConfFile.getParentFile().mkdirs();
                if (mkdirs) {
                    if (leafConfFile.createNewFile()) {
                        FileUtils.writeStringToFile(leafConfFile, "workerID=" + workerID, "UTF-8", false);
                        log.info("文件缓存的workerID: {}", workerID);
                    }
                } else {
                    log.warn("文件缓存父文件创建失败");
                }
            } catch (IOException e) {
                log.warn("文件缓存创建失败", e);
            }
        }
    }

    private void scheduledUploadData(final CuratorFramework curator, final String zk_AddressNode) {

        ScheduledThreadPoolExecutor updateTimeExecutor = new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "schedule-upload-time");
            thread.setDaemon(true);
            return thread;
        });
        updateTimeExecutor.scheduleWithFixedDelay(() -> {
            updateNewData(curator, zk_AddressNode);
        }, 0L, 3L, TimeUnit.SECONDS);
    }

    private boolean checkInitTimeStamp(CuratorFramework curator, String zk_AddressNode) throws Exception {
        byte[] bytes = curator.getData().forPath(zk_AddressNode);
        Endpoint endPoint = JsonUtils.toObject(new String(bytes, "UTF-8"), Endpoint.class);
        //该节点的时间不能小于最后一次上报的时间
        return !(endPoint.getTimestamp() > System.currentTimeMillis());
    }

    private void updateNewData(CuratorFramework curator, String path) {
        try {
            if (System.currentTimeMillis() < lastUpdateTime) {
                return;
            }
            curator.setData().forPath(path, timeData().getBytes());
            lastUpdateTime = System.currentTimeMillis();
        } catch (Exception e) {
            log.info("update init data error path is {} error is {}", path, e);
        }
    }

    /**
     * 构建需要上传的数据
     *
     * @return
     */
    private String timeData() {
        Endpoint endpoint = new Endpoint(ip, System.currentTimeMillis());
        return JsonUtils.toJsonString(endpoint);
    }

    private CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy, int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder().connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }


    public int getWorkerID() {
        return workerId;
    }

    public void setWorkerID(int workerID) {
        this.workerId = workerID;
    }


    /**
     * 上报数据结构
     */
    static class Endpoint {
        private String ip;
        private long timestamp;

        public Endpoint() {
        }

        public Endpoint(String ip, long timestamp) {
            this.ip = ip;
            this.timestamp = timestamp;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }
    }

}
