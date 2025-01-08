package edu.qingchenjia.heimacomments.common;

import lombok.Data;

import java.util.List;

@Data
public class ScrollResult {
    private List<?> list;

    private Long minTime;

    private Integer offset;
}
