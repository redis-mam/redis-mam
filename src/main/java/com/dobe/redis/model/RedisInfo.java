package com.dobe.redis.model;

import com.dobe.redis.enums.RedisType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    
    private List<Node> nodeList;


    public void parseNodes() {
        switch (RedisType.parse(this.name)) {
            case SINGLE:
                this.addNode(this.nodes);
                break;
            case CLUSTER:
                Stream.of(this.nodes.split(",")).forEach(this::addNode);
            default: 
        }
    }

    private void addNode(String value) {
        String[] arr = value.split(":");
        this.nodeList.add(new Node(arr[0], Integer.parseInt(arr[1]), pwd));
    }
}
