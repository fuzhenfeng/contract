package org.contract.lock;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.contract.common.BytesUtils;
import org.contract.common.InitException;
import org.contract.common.RunException;
import org.contract.common.StringUtils;
import org.contract.config.Config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * zookeeper 分布式锁
 * zookeeper节点保障循序性
 * zookeeper临时节点保障断开删除，不会死锁
 * zookeeper中创建和删除节点只能通过Leader服务器来执行，然后将数据同不到所有的Follower机器上。
 */
public class ZookeeperLock implements DistributedLock, Watcher, AsyncCallback.StringCallback, AsyncCallback.Children2Callback, AsyncCallback.StatCallback {
    private final static Logger log = LogManager.getLogger(ZookeeperLock.class);

    private String address;
    private Integer port;
    private static ZooKeeper zk;
    private CountDownLatch downLatch;
    private String nodeName;
    private String key;

    @Override
    public void init(Config config) throws InitException {
        Map<String, String> map = config.getConfig();
        this.address = map.get("zookeeper.address");
        try {
            this.port = Integer.valueOf(map.get("zookeeper.port"));
        } catch (NumberFormatException e) {
            // ignore
        }
        if (StringUtils.isBlank(this.address) || port == null) {
            throw new InitException("zookeeper parameter(address port) is a must");
        }
    }

    @Override
    public boolean tryLock(String key, String identity, long timeout, TimeUnit unit) {
        downLatch = new CountDownLatch(1);
        this.key = key;
        try {
            zk = new ZooKeeper(address, port, this);
            zk.create("/" + key, BytesUtils.get(identity), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL, this, null);
            downLatch.await();
            return true;
        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean tryUnLock(String key, String identity, long timeout, TimeUnit unit) {
        try {
            zk.delete(nodeName, -1);
            nodeName = null;
            return true;
        } catch (KeeperException | InterruptedException e) {
            log.error(e.getMessage(), e);
            return false;
        } finally {
            try {
                if(zk != null) zk.close();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void close() throws RunException {

    }

    /**
     * Watcher
     * @param event
     */
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                break;
            case NodeCreated:
                break;
            case NodeDeleted:
                //当前节点重新获取锁
                zk.getChildren("/", false, this, null);
                break;
            case NodeDataChanged:
                break;
            case NodeChildrenChanged:
                break;
        }

    }

    /**
     * Children2Callback
     * @param rc       The return code or the result of the call.
     * @param path     The path that we passed to asynchronous calls.
     * @param ctx      Whatever context object that we passed to asynchronous calls.
     * @param children An unordered array of children of the node on given path.
     * @param stat     {@link Stat} object of the node on given path.
     *
     */
    @Override
    public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
        List<String> collect = children.stream().filter(name -> name.contains(key)).collect(Collectors.toList());
        Collections.sort(collect);
        int i = collect.indexOf(nodeName.substring(1));

        if (i == 0) {
            try {
                zk.setData("/", nodeName.getBytes(), -1);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            zk.exists("/" + children.get(i - 1), this, this, null);
        }
    }

    /**
     * StatCallback
     * @param rc   The return code or the result of the call.
     * @param path The path that we passed to asynchronous calls.
     * @param ctx  Whatever context object that we passed to asynchronous calls.
     * @param stat {@link Stat} object of the node on given path.
     *
     */
    @Override
    public void processResult(int rc, String path, Object ctx, Stat stat) {

    }

    /**
     * StringCallback
     * @param rc   The return code or the result of the call.
     * @param path The path that we passed to asynchronous calls.
     * @param ctx  Whatever context object that we passed to asynchronous calls.
     * @param name The name of the znode that was created. On success, <i>name</i>
     *             and <i>path</i> are usually equal, unless a sequential node has
     *             been created.
     *
     */
    @Override
    public void processResult(int rc, String path, Object ctx, String name) {
        if (name != null) {
            nodeName = name;
            zk.getChildren("/", false, this, null);
            downLatch.countDown();
        }
    }
}
