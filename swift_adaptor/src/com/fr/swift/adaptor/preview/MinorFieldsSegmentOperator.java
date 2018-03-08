package com.fr.swift.adaptor.preview;

import com.fr.swift.cube.io.Types;
import com.fr.swift.cube.io.location.ResourceLocation;
import com.fr.swift.exception.meta.SwiftMetaDataException;
import com.fr.swift.segment.RealTimeSegmentImpl;
import com.fr.swift.segment.Segment;
import com.fr.swift.segment.increase.IncreaseFieldsSegmentOperator;
import com.fr.swift.source.SourceKey;
import com.fr.swift.source.SwiftMetaData;
import com.fr.swift.source.SwiftResultSet;

import java.util.Collections;
import java.util.List;

public class MinorFieldsSegmentOperator extends IncreaseFieldsSegmentOperator {
    public MinorFieldsSegmentOperator(SourceKey sourceKey, SwiftMetaData metaData, List<Segment> segments,
                                      String cubeSourceKey, SwiftResultSet swiftResultSet, List<String> fields) throws SwiftMetaDataException {
        super(sourceKey, metaData, segments, cubeSourceKey, swiftResultSet, fields);
    }

    @Override
    protected Segment createSegment(int order) {
        String path = String.format("/%s/cubes/%s/minor_seg",
                System.getProperty("user.dir"),
                cubeSourceKey);
        Segment seg = new RealTimeSegmentImpl(new ResourceLocation(path, Types.StoreType.MEMORY), metaData);
        MinorSegmentManager.getInstance().putSegment(sourceKey, Collections.singletonList(seg));
        return seg;
    }
}
