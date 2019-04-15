package com.dobe.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页信息
 * @author zc.ding
 * @since 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pager {
    
    private int currPage = 1;
    private int pageSize = 10;
    private int rows = 0;
    private int pages = 0;
    private List<?> data = new ArrayList<>();


    public void setRows(int rows) {
        int total = rows / pageSize;
        this.pages = rows % pageSize == 0 ? total : ++total;
        this.rows = rows;
    }
    
    public int getStart() {
        int start = (currPage - 1) * pageSize;
        if (start > 0) {
            return --start;
        }
        return start;
    }

    public int getEnd() {
        int end = getStart() + pageSize;
        return end > rows ? rows : end;
    }
}
