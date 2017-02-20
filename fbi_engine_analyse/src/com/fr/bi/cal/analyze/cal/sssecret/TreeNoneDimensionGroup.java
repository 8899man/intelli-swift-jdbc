package com.fr.bi.cal.analyze.cal.sssecret;import com.finebi.cube.api.ICubeDataLoader;import com.finebi.cube.api.ICubeValueEntryGetter;import com.fr.bi.cal.analyze.cal.result.Node;import com.fr.bi.stable.report.result.DimensionCalculator;import com.fr.bi.stable.report.result.LightNode;import com.fr.bi.stable.report.result.TargetCalculator;import java.util.HashMap;import java.util.Map;/** * Created by Hiram on 2015/1/28. */public class TreeNoneDimensionGroup extends NoneDimensionGroup {    private ISingleDimensionGroup cache;    private LightNode lightNode;    public TreeNoneDimensionGroup(LightNode lightNode, ICubeDataLoader loader) {        this.lightNode = lightNode;        this.loader = loader;    }    private Node initRoot(TargetCalculator key) {        Node node = new Node(null, lightNode.getData());        Map<TargetCalculator, Object> map = extractMap();        node.setTargetIndexValueMap(lightNode.getTargetIndexValueMap());        node.setGroupValueIndexMap(lightNode.getGroupValueIndexMap());        node.setSummaryValueMap(map);        node.setGroupValueIndex(node.getIndex4CalByTargetKey(key.createTargetGettingKey()));        return node;    }    @Override    public Node getRoot() {        throw new UnsupportedOperationException();    }    public void setRoot(LightNode root) {        this.lightNode = root;    }    @Override    public LightNode getLightNode() {        return lightNode;    }    public LightNode getTargetGettingKeyRoot() {        return lightNode;    }    private Map<TargetCalculator, Object> extractMap() {        Map summaryValue = lightNode.getSummaryValueMap();        if (summaryValue == null) {            return null;        }        Map<TargetCalculator, Object> map = new HashMap<TargetCalculator, Object>();        for (Object key : summaryValue.keySet()) {            if (key instanceof TargetCalculator) {                map.put((TargetCalculator) key, summaryValue.get(key));            }        }        return map;    }    @Override    public ISingleDimensionGroup createSingleDimensionGroup(DimensionCalculator[] pck, DimensionCalculator ck, Object[] data, int ckIndex, ICubeValueEntryGetter getter, boolean useRealData) {        return getSingleDimensionGroup();    }    public ISingleDimensionGroup getSingleDimensionGroup() {        if (cache == null) {            cache = new TreeSingleDimensionGroup(lightNode);        }        return cache;    }    @Override    public Number getSummaryValue(TargetCalculator key) {        Number value =  lightNode.getSummaryValue(key.createTargetGettingKey());        if (value == null){            return new NodeSummaryCalculator(getLoader()).getNodeSummary(initRoot(key), key);        }        return value;    }}