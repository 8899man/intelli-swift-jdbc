//放置用户不可配置的常量

BICst.WIDGET.Widths = {};
BICst.WIDGET.Widths[BICst.WIDGET.TABLE] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.BAR] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.ACCUMULATE_BAR] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.PIE] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.DASHBOARD] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.AXIS] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.MAP] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.DETAIL] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.DONUT] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.BUBBLE] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.SCATTER] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.RADAR] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.CONTENT] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.IMAGE] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.WEB] = 450;

BICst.WIDGET.Widths[BICst.WIDGET.STRING] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.NUMBER] = 300;
BICst.WIDGET.Widths[BICst.WIDGET.DATE] = 320;
BICst.WIDGET.Widths[BICst.WIDGET.YEAR] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.QUARTER] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.MONTH] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.YMD] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.TREE] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.GENERAL_QUERY] = 450;
BICst.WIDGET.Widths[BICst.WIDGET.QUERY] = 250;
BICst.WIDGET.Widths[BICst.WIDGET.RESET] = 250;

BICst.WIDGET.Heights = {};
BICst.WIDGET.Heights[BICst.WIDGET.TABLE] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.BAR] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.ACCUMULATE_BAR] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.PIE] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.DASHBOARD] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.AXIS] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.MAP] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.DETAIL] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.DONUT] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.BUBBLE] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.SCATTER] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.RADAR] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.CONTENT] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.IMAGE] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.WEB] = 250;

BICst.WIDGET.Heights[BICst.WIDGET.STRING] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.NUMBER] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.DATE] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.YEAR] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.QUARTER] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.MONTH] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.YMD] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.TREE] = 110;
BICst.WIDGET.Heights[BICst.WIDGET.GENERAL_QUERY] = 250;
BICst.WIDGET.Heights[BICst.WIDGET.QUERY] = 60;
BICst.WIDGET.Heights[BICst.WIDGET.RESET] = 60;

//布局方式
BICst.DASHBOARD_LAYOUT_ARRAY = [{
    text: BI.i18nText("BI-Adaptive_Layout"), value: BICst.DASHBOARD_LAYOUT_ADAPT
}, {
    text: BI.i18nText("BI-Free_Layout"), value: BICst.DASHBOARD_LAYOUT_FREE
}];

//类型&数据/样式tab
BICst.DETAIL_DATA_STYLE_TAB = [{
    text: BI.i18nText("BI-Type_Data"), value: BICst.DETAIL_TAB_TYPE_DATA
}, {
    text: BI.i18nText("BI-Style"), value: BICst.DETAIL_TAB_STYLE
}];

//业务包字段/复用
BICst.DETAIL_FIELD_REUSE_TAB = [{
    text: BI.i18nText("BI-Package_Field"), value: BICst.DETAIL_PACKAGES_FIELD
}, {
    text: BI.i18nText("BI-Reuse_Field"), value: BICst.DETAIL_DIMENSION_REUSE
}];

//etl pane card names (empty tip, only one tip, pane)
BICst.CONF_ETL_DATA_SET_EMPTY_TIP = "data_set_empty_tip";
BICst.CONF_ETL_DATA_SET_ONLY_ONE_TIP = "data_set_only_one_tip";
BICst.CONF_ETL_DATA_SET_PANE = "data_set_pane";
BICst.CONF_ETL_SET_EMPTY_TIP = "etl_empty_tip";
BICst.CONF_ETL_SET_PANE = "etl_set_pane";