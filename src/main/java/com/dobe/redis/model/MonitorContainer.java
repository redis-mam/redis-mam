package com.dobe.redis.model;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;

/**
 * 监听容器
 *
 * @author zc.ding
 * @since 2019/4/12
 */
public class MonitorContainer {

    private LinkedBlockingQueue<Node> linkedBlockingQueue = new LinkedBlockingQueue<>(6);
    


    public void addNode(Node node) {
        try{
            linkedBlockingQueue.put(node);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public Node getNode(){
        return linkedBlockingQueue.poll();
    }

   
}
