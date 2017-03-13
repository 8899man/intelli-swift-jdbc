export const REPORT_AUTH =  {
NONE: 0,
EDIT: 1,
VIEW: 2
}
export const TARGET_STYLE =  {
ICON_STYLE:  {
NONE: 1,
POINT: 2,
ARROW: 3
},
NUM_LEVEL:  {
NORMAL: 1,
TEN_THOUSAND: 2,
MILLION: 3,
YI: 4,
PERCENT: 5
},
FORMAT:  {
NORMAL: 1,
ZERO2POINT: 2,
ONE2POINT: 3,
TWO2POINT: 4
},
}
export const MULTI_PATH_STATUS =  {
NEED_GENERATE_CUBE: 0,
NOT_NEED_GENERATE_CUBE: 1,
}
export const CUSTOM_GROUP =  {
UNGROUP2OTHER:  {
NOTSELECTED: 0,
SELECTED: 1
},
}
export const REPORT_STATUS =  {
APPLYING: 1,
HANGOUT: 2,
NORMAL: 3
}
export const FIELD_ID =  {
HEAD: "81c48028-1401-11e6-a148-3e1d05defe78"
}
export const TREE_LABEL =  {
TREE_LABEL_ITEM_COUNT_NUM: 40
}
export const TREE =  {
TREE_REQ_TYPE:  {
INIT_DATA: 0,
SEARCH_DATA: 1,
SELECTED_DATA: 3,
ADJUST_DATA: 2,
DISPLAY_DATA: 4
},
TREE_ITEM_COUNT_PER_PAGE: 100
}
export const BUSINESS_TABLE_TYPE =  {
NORMAL: 0
}
export const EXPANDER_TYPE =  {
NONE: false,
ALL: true
}
export const SORT =  {
ASC: 0,
DESC: 1,
CUSTOM: 2,
NONE: 3,
NUMBER_ASC: 4,
NUMBER_DESC: 5
}
export const TABLE_PAGE_OPERATOR =  {
ALL_PAGE: -1,
REFRESH: 0,
COLUMN_PRE: 1,
COLUMN_NEXT: 2,
ROW_PRE: 3,
ROW_NEXT: 4,
EXPAND: 5
}
export const TABLE_PAGE =  {
VERTICAL_PRE: 0,
VERTICAL_NEXT: 1,
HORIZON_PRE: 2,
HORIZON_NEXT: 3,
TOTAL_PAGE: 4
}
export const TABLE_WIDGET =  {
GROUP_TYPE: 1,
CROSS_TYPE: 2,
COMPLEX_TYPE: 3
}
export const REGION =  {
DIMENSION1: "10000",
DIMENSION2: "20000",
TARGET1: "30000",
TARGET2: "40000",
TARGET3: "50000"
}
export const EXPORT =  {
EXCEL: 1,
PDF: 2
}
export const TARGET_TYPE =  {
CAL_POSITION:  {
ALL: 0,
INGROUP: 1
},
CAL_VALUE:  {
PERIOD_TYPE:  {
VALUE: 0,
RATE: 1
},
SUMMARY_TYPE:  {
SUM: 0,
MAX: 1,
MIN: 2,
AVG: 3
},
RANK_TPYE:  {
ASC: 0,
DESC: 1
},
SUM_OF_ALL: 0,
PERIOD: 1,
SUM_OF_ABOVE: 2,
RANK: 3
},
CAL:  {
FORMULA: 0,
CONFIGURATION: 1
},
STRING: 1,
NUMBER: 2,
DATE: 3,
COUNTER: 4,
FORMULA: 5,
YEAR_ON_YEAR_RATE: 6,
MONTH_ON_MONTH_RATE: 7,
YEAR_ON_YEAR_VALUE: 8,
MONTH_ON_MONTH_VALUE: 9,
SUM_OF_ABOVE: 10,
SUM_OF_ABOVE_IN_GROUP: 11,
SUM_OF_ALL: 12,
SUM_OF_ALL_IN_GROUP: 13,
RANK: 14,
RANK_IN_GROUP: 15
}
export const DIMENSION_FILTER_DATE =  {
BELONG_VALUE: 98,
NOT_BELONG_VALUE: 99,
IS_NULL: 100,
NOT_NULL: 101,
TOP_N: 102,
BOTTOM_N: 103,
CONTAIN: 104,
NOT_CONTAIN: 105,
BEGIN_WITH: 106,
END_WITH: 107
}
export const FILTER_TYPE =  {
AND: 80,
OR: 81,
FORMULA: 82,
EMPTY_FORMULA: 90,
EMPTY_CONDITION: 91,
NUMBER_SUM: 83,
NUMBER_AVG: 84,
NUMBER_MAX: 85,
NUMBER_MIN: 86,
NUMBER_COUNT: 87,
TREE_FILTER: 88,
COLUMNFILTER: 89,
DIMENSION_TARGET_VALUE_FILTER: 96,
DIMENSION_SELF_FILTER: 97
}
export const FILTER_DATE =  {
BELONG_DATE_RANGE: 64,
BELONG_WIDGET_VALUE: 65,
NOT_BELONG_DATE_RANGE: 66,
NOT_BELONG_WIDGET_VALUE: 67,
MORE_THAN: 68,
LESS_THAN: 69,
EQUAL_TO: 70,
NOT_EQUAL_TO: 71,
IS_NULL: 72,
NOT_NULL: 73,
EARLY_THAN: 74,
LATER_THAN: 75,
CONTAINS: 76,
CONTAINS_DAY: 77,
DAY_EQUAL_TO: 78,
DAY_NOT_EQUAL_TO: 79
}
export const TARGET_FILTER_NUMBER =  {
EQUAL_TO: 48,
NOT_EQUAL_TO: 49,
BELONG_VALUE: 50,
BELONG_USER: 51,
NOT_BELONG_VALUE: 52,
NOT_BELONG_USER: 53,
IS_NULL: 54,
NOT_NULL: 55,
CONTAINS: 56,
NOT_CONTAINS: 57,
LARGE_THAN_CAL_LINE: 58,
LARGE_OR_EQUAL_CAL_LINE: 59,
SMALL_THAN_CAL_LINE: 60,
SMALL_OR_EQUAL_CAL_LINE: 61,
TOP_N: 62,
BOTTOM_N: 63
}
export const TARGET_FILTER_STRING =  {
BELONG_VALUE: 32,
BELONG_USER: 33,
NOT_BELONG_VALUE: 34,
NOT_BELONG_USER: 35,
CONTAIN: 36,
NOT_CONTAIN: 37,
IS_NULL: 38,
NOT_NULL: 39,
BEGIN_WITH: 40,
END_WITH: 41,
NOT_BEGIN_WITH: 42,
NOT_END_WITH: 43,
VAGUE_CONTAIN: 46,
NOT_VAGUE_CONTAIN: 47
}
export const DIMENSION_FILTER_NUMBER =  {
BELONG_VALUE: 16,
BELONG_USER: 17,
NOT_BELONG_VALUE: 18,
NOT_BELONG_USER: 19,
MORE_THAN_AVG: 20,
LESS_THAN_AVG: 21,
IS_NULL: 22,
NOT_NULL: 23,
TOP_N: 24,
BOTTOM_N: 25
}
export const DIMENSION_FILTER_STRING =  {
BELONG_VALUE: 0,
BELONG_USER: 1,
NOT_BELONG_VALUE: 2,
NOT_BELONG_USER: 3,
CONTAIN: 4,
NOT_CONTAIN: 5,
IS_NULL: 6,
NOT_NULL: 7,
BEGIN_WITH: 8,
END_WITH: 9,
TOP_N: 10,
BOTTOM_N: 11,
NOT_BEGIN_WITH: 12,
NOT_END_WITH: 13,
VAGUE_CONTAIN: 14,
NOT_VAGUE_CONTAIN: 15
}
export const GROUP =  {
NO_GROUP: 0,
AUTO_GROUP: 3,
CUSTOM_GROUP: 4,
CUSTOM_NUMBER_GROUP: 5,
Y: 6,
S: 7,
M: 8,
W: 9,
YMD: 10,
YD: 11,
MD: 12,
YMDHMS: 13,
ID_GROUP: 14
}
export const SUMMARY_TYPE =  {
SUM: 0,
MAX: 1,
MIN: 2,
AVG: 3,
COUNT: 4,
APPEND: 5,
RECORD_COUNT: 6
}
export const BI_REPORT =  {
NULL: 0,
SUBMITED: 1,
PUBLISHED: 2
}
VERSION = "4.0.2"
SYSTEM_TIME = "__system_time-3e1d05defe78__"


export const ETL_ADD_COLUMN_TYPE =  {
FORMULA: "formula",
DATE_DIFF: "date_diff",
DATE_YEAR: "date_year",
DATE_SEASON: "date_season",
DATE_MONTH: "date_month",
EXPR_CPP: "expr_same_period",
EXPR_LP: "expr_last_period",
EXPR_CPP_PERCENT: "expr_same_period_percent",
EXPR_LP_PERCENT: "expr_last_period_percent",
EXPR_SUM: "expr_sum",
EXPR_ACC: "expr_acc",
EXPR_RANK: "expr_rank",
GROUP: "group_value",
SINGLE_VALUE: "single_value",
VALUE_CONVERT: "value_convert"
}
export const JSON_KEYS =  {
STATISTIC_ELEMENT: "_src",
FILED_MAX_VALUE: "max",
FIELD_MIN_VALUE: "min",
FILTER_VALUE: "filter_value",
FILTER_CONDITION: "condition",
FILTER_AND_OR: "andor",
FILTER_TYPE: "filter_type",
FIELD_TYPE: "field_type",
FIELD_VALUE: "field_value",
FIELD_NAME: "field_name",
TYPE: "type",
VALUE: "value",
EXPANDER: "expander",
EXPANDER_X: "x",
EXPANDER_Y: "y",
CLICKEDVALUE: "clickedvalue",
SETTTINGS: "settings",
ID: "id",
TABLES: "tables",
TABLE: "table",
FIELDS: "fields",
FIELD: "field",
ETL_TYPE: "etl_type",
ETL_VALUE: "etl_value",
TABLE_TYPE: "table_type",
HAS_NEXT: "hasNext"
}


export const DATA_CONFIG_DESIGN =  {
NO: 0,
YES: 1
}
export const DATA_CONFIG_AUTHORITY =  {
PACKAGE_MANAGER:  {
NODE: "__package_manager_node__",
PAGE: "__package_manager_page__",
SERVER_CONNECTION: "__package_server_connection__",
DATA_CONNECTION: "__package_data_connection__"
},
DATA_CONNECTION:  {
NODE: "__data_connection_node__",
PAGE: "__data_connection_page__"
},
MULTI_PATH_SETTING: "__multi_path_setting__",
PACKAGE_AUTHORITY: "__package_authority__",
FINE_INDEX_UPDATE: "__fine_index_update__"
}
export const GLOBAL_UPDATE_TYPE =  {
PART_UPDATE: "_part_update_",
COMPLETE_UPDATE: "_complete_update_",
META_UPDATE: "_meta_update_"
}
export const CUBE_UPDATE_TYPE =  {
GLOBAL_UPDATE: "__global_update__",
SINGLETABLE_UPDATE: "__singleTable_update__"
}
export const SINGLE_TABLE_UPDATE =  {
TOGETHER: 0,
NEVER: 1
}
export const SINGLE_TABLE_UPDATE_TYPE =  {
ALL: 0,
PART: 1,
NEVER: 2
}
export const UPDATE_FREQUENCY =  {
EVER_DAY: 0,
EVER_SUNDAY: 1,
EVER_MONDAY: 2,
EVER_TUESDAY: 3,
EVER_WEDNESDAY: 4,
EVER_THURSDAY: 5,
EVER_FRIDAY: 6,
EVER_SATURDAY: 7,
EVER_MONTH: 10
}
export const REQ_DATA_TYPE =  {
REQ_GET_ALL_DATA: -1,
REQ_GET_DATA_LENGTH: 0
}
export const TRANS_TYPE =  {
READ_FROM_DB: "db",
READ_FROM_TABLEDATA: "tabledata",
CHOOSE: "choose"
}
export const CONNECTION =  {
ETL_CONNECTION: "__FR_BI_ETL__",
SERVER_CONNECTION: "__FR_BI_SERVER__",
SQL_CONNECTION: "__FR_BI_SQL__",
EXCEL_CONNECTION: "__FR_BI_EXCEL__"
}
export const COLUMN =  {
NUMBER: 32,
STRING: 16,
DATE: 48,
COUNTER: 64,
ROW: 80
}
export const CLASS =  {
INTEGER: 0,
LONG: 1,
DOUBLE: 2,
FLOAT: 3,
DATE: 4,
STRING: 5,
BOOLEAN: 6,
TIMESTAMP: 7,
DECIMAL: 8,
TIME: 9,
BYTE: 10,
ROW: 16
}
SYSTEM_USER_NAME = "__system_user_name__"
LAST_UPDATE_TIME = "__last_update_time__"
CURRENT_UPDATE_TIME = "__current_update_time__"


export const FUNCTION =  {
TEXT: 1,
MATH: 2,
DATE: 3,
ARRAY: 4,
LOGIC: 5,
OTHER: 6
}


