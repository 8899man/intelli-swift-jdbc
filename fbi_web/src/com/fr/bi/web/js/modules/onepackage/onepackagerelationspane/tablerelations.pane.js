/**
 * @class BI.PackageTableRelationsPane
 * @extend BI.Widget
 * 单个业务包界面所有表关联
 */
BI.PackageTableRelationsPane = BI.inherit(BI.Widget, {
    _defaultConfig: function(){
        return BI.extend(BI.PackageTableRelationsPane.superclass._defaultConfig.apply(this, arguments), {
            baseCls: "bi-package-table-relations-pane"
        })
    },

    _init: function(){
        BI.PackageTableRelationsPane.superclass._init.apply(this, arguments);
        this.model = new BI.PackageTableRelationsPaneModel({
        });
        var self = this;
        this.relationView = BI.createWidget({
            type: "bi.relation_view"
        });
        this.relationView.on(BI.RelationView.EVENT_CHANGE, function(v){
            self.fireEvent(BI.PackageTableRelationsPane.EVENT_CLICK_TABLE, v);
        });
        BI.createWidget({
            type: "bi.vertical",
            element: this.element,
            items: [this.relationView]
        })
    },

    _createItemsByTableIdsAndRelations: function () {
        var self = this;
        var items = [];
        var tableIds = this.model.getTableIds();
        var fieldsMap = this.model.getFieldsMap();
        var relations = this.model.getRelations();
        var connectSet = relations.connectionSet;
        var primKeyMap = relations.primKeyMap;
        var foreignKeyMap = relations.foreignKeyMap;
        var all_fields = this.model.getAllFields();
        var regionHandler = function(){
            self.fireEvent(BI.PackageTableRelationsPane.EVENT_CLICK_TABLE, this.options.value);
        };
        //var allTableSet = [];
        var degrees = getTableIdsDegree();
        var calcDegree = {};
        var distinctTableIds = [];
        BI.each(tableIds, function(idx, tId){
            calcDegree[tId] = 0;
        });
        BI.each(tableIds, function(idx, tId){
            if(BI.contains(distinctTableIds, tId)){
                return;
            }
            distinctTableIds.push(tId);
            var primFields = getFieldsInPrimKeyMap(fieldsMap[tId]);
            var foreFields = getFieldsInForeignMap(fieldsMap[tId]);
            if(BI.isEmptyArray(primFields) && BI.isEmptyArray(foreFields)){
                items.push({
                    primary: {
                        region: tId,
                        regionText: self.model.getTableTranName(tId),
                        regionTitle: self.model.getTableTranName(tId),
                        regionHandler: regionHandler
                    }
                });
            }else{
                items =  BI.concat(items, getViewItemsByTableId(tId, []));
            }
        });
        return items;

        function getTableIdsDegree(){
            var degree = {};
            //因为1对1的关联在foreignKeyMap中取不到，所以弃用
            //BI.each(tableIds, function(idx, tId){
            //    var count = 0;
            //    var foreFields = getFieldsInForeignMap(fieldsMap[tId]);
            //    BI.each(foreFields, function(idx, fieldId){
            //        if(BI.has(foreignKeyMap, fieldId)) {
            //            count++;
            //        }
            //    });
            //    degree[tId] = count;
            //});
            //待优化
            BI.each(connectSet, function(idx, obj){
                var foreignId = obj.foreignKey.field_id;
                var tableId = all_fields[foreignId].table_id;
                if(!BI.has(degree, tableId)){
                    degree[tableId] = 0;
                }
                degree[tableId]++;
            });
            return degree;
        }

        function getViewItemsByTableId(tId, visitSet){
            var rels = getRelationsByPrimaryId(tId);
            var items = [];
            BI.each(rels, function(idx, rel){
                var primaryId = rel.primaryKey.field_id, foreignId = rel.foreignKey.field_id;
                var foreignTableId = all_fields[foreignId].table_id;
                //是未访问过的节点且入度未满
                if(!BI.contains(visitSet, foreignTableId) && calcDegree[foreignTableId] !== degrees[foreignTableId]){
                    //自循环
                    if(all_fields[primaryId].table_id === all_fields[foreignId].table_id){
                        items.push({
                            primary: {
                                region: all_fields[primaryId].table_id,
                                regionText: self.model.getTableTranName(all_fields[primaryId].table_id),
                                regionTitle: self.model.getTableTranName(all_fields[primaryId].table_id),
                                value: primaryId,
                                text: self.model.getFieldTranName(primaryId),
                                title: self.model.getFieldTranName(primaryId),
                                regionHandler: regionHandler
                            },
                            foreign: {
                                region: BI.UUID(),
                                regionText: self.model.getTableTranName(all_fields[foreignId].table_id),
                                regionTitle: self.model.getTableTranName(all_fields[foreignId].table_id),
                                value: foreignId,
                                text: self.model.getFieldTranName(foreignId),
                                title: self.model.getFieldTranName(foreignId)
                            }
                        });
                    }else{
                        items.push({
                            primary: {
                                region: all_fields[primaryId].table_id,
                                regionText: self.model.getTableTranName(all_fields[primaryId].table_id),
                                regionTitle: self.model.getTableTranName(all_fields[primaryId].table_id),
                                value: primaryId,
                                text: self.model.getFieldTranName(primaryId),
                                title: self.model.getFieldTranName(primaryId),
                                regionHandler: regionHandler
                            },
                            foreign: {
                                region: all_fields[foreignId].table_id,
                                regionText: self.model.getTableTranName(all_fields[foreignId].table_id),
                                regionTitle: self.model.getTableTranName(all_fields[foreignId].table_id),
                                value: foreignId,
                                text: self.model.getFieldTranName(foreignId),
                                title: self.model.getFieldTranName(foreignId),
                                regionHandler: regionHandler
                            }
                        });
                    }
                }
                var visittable = BI.concat(visitSet, [tId]);
                if(!BI.contains(visittable, foreignTableId) && calcDegree[foreignTableId] !== degrees[foreignTableId]){
                    calcDegree[foreignTableId]++;
                    distinctTableIds.pushDistinct(foreignTableId);
                    items = BI.concat(items, getViewItemsByTableId(foreignTableId, visittable));
                }
            });
            return items;
        }

        function getFieldsInPrimKeyMap(fieldIds){
            return BI.filter(fieldIds, function(idx, fieldId){
                return BI.has(primKeyMap, fieldId);
            });
        }

        function getFieldsInForeignMap(fieldIds){
            return BI.filter(fieldIds, function(idx, fieldId){
                return BI.has(foreignKeyMap, fieldId);
            });
        }

        function getRelationsByPrimaryId(tId){
            var rel = [];
            var primFields = getFieldsInPrimKeyMap(fieldsMap[tId]);
            BI.each(primFields, function(idx, fieldId){
                rel = BI.concat(rel, primKeyMap[fieldId]);
            });
            return rel;
        }
    },

    populate: function(items){
        var self = this, o = this.options;
        this.model.populate(items);
        this.model.getTableNamesOfAllPackages(function(){
            self.relationView.populate(self._createItemsByTableIdsAndRelations());
        });
    },

    getValue: function(){

    },

    setValue: function(){

    }
});
BI.PackageTableRelationsPane.EVENT_CLICK_TABLE = "EVENT_CLICK_TABLE";
$.shortcut("bi.package_table_relations_pane", BI.PackageTableRelationsPane);