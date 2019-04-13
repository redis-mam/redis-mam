package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * redis的连接信息
 *
 * @author zc.ding
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisInfo {
    
    private String name = "";
    private String nodes = "";
    private String description = "";
    private String type = "";
    private String pwd;
    
    private List<Node> nodeList = new ArrayList<>();


    public void parseNodes() {
        Stream.of(this.nodes.split(",")).forEach(value -> {
            String[] arr = value.split(":");
            Node node = new Node();
            node.setHost(arr[0]);
            node.setPort(Integer.parseInt(arr[1]));
            node.setPwd(this.pwd);
            this.nodeList.add(node);
        });
    }
}
