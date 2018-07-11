package com.fr.swift.source.alloter;

import com.fr.swift.segment.Segment;

/**
 * @author pony
 * @date 2017/10/24
 * 数据源生成cube的接口,同一数据源可以有多种生成方式，或者分配规则，直接按行拆分，按某些列的hash拆分等等
 */
public interface SwiftSourceAlloter {
    /**
     * 按行信息分块
     *
     * @param rowInfo 行信息
     * @return 块信息
     */
    SegmentInfo allot(RowInfo rowInfo);

    AllotRule getAllotRule();

    boolean isFull(Segment segment);
}