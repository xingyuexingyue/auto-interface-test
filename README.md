采用 TestNG 的结构 自动化测试实践

##### 实现思路

第一步. 使用 .xml 文件管理要执行的用例

第二步. 使用 TestNG 作为执行测试用例的入口

第三步. 每次执行测试用例要先进行参数化替换和前置动作

第四步. 发起请求并获得请求结果

第五步. 对请求结果做检查点校验

##### 项目结构

```
.
└── java
    |
    └── com.sangyu    	
    |   ├── config 配置相关
    |   ├── dto 数据库映射对象
    |   ├── httptest http 类型请求执行
    |   ├── model 数据库操作类
    |   ├── utils 工具类
    |   └── vo 响应映射对象
    └── resources
        ├── log4j2.xml 日志配置文件
        └── user-service.xml 执行测试用例类
```

##### 表结构

用例表

```
create table `http_cases`(
        `id` int unsigned auto_increment, 
         `team_id`int(2) not null comment  '团队id',  
        `is_run` char(2) NOT NULL comment  '是否开启每日CI 0开始 1不开启',  `service_name` int(2) not null comment  '服务名称',  
        `env` char not null comment  '环境',  
        `request_type` char not null comment  '请求类型 1 get 2 post',  
        `path` varchar(100) not null comment  '路径', 
         `parameters` varchar(1000) comment  '参数', 
         `headers` varchar(1000) comment  '请求头',  
        `remark` varchar(500) comment  '备注',  
        `reponse_type` char not null comment  '结果类型', 
        `before_action` varchar(500) comment  '前置动作', 
        `after_test_action` varchar(500) comment  '测试后操作', 
        `after_action` varchar(500) comment  '后置操作',
         `check_error` varchar(500) comment  '异常检查点',
         `check_null` varchar(500) comment  '不为空检查点',
         `check_contain` varchar(500) comment  '包含检查点', 
        `check_uncontain` varchar(500) comment  '不包含检查点', 
        `check_jsonpath` varchar(500) comment  'JsonPath检查点', 
        `create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP comment  '创建时间', 
        `update_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '更新时间' ,
        `is_delete` char(2) NOT NULL  DEFAULT '0' comment  '状态删除 0删除 1未删除 ' , PRIMARY KEY ( `id` ))ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

参数化维护表

```
create table `parameterize`(
        `id` int unsigned auto_increment, 
        `team_id`int(2) not null comment  '团队id', 
        `paramer_name` varchar(100) not null comment '参数名称',
        `remark` varchar(500) comment  '备注',  
        `paramer_type` int(2) not null comment  '参数类型 0 k-v 类型 1 sql 类型 2 测试用例类型',
        `data_name` int(100) comment  '数据库名  sql 类型用', 
         `s_sql` varchar(500) comment  'sql 语句  sql 类型用',
         `field` varchar(50) comment  '从 sql 结果中取用的字段  sql 类型用',
        `get_type` int(2) comment  '结果获取值方式 sql 类型用 0 单个值',
        `value` varchar(50) comment  'k-v 类型用',
        `case_id` int comment  '用例 id 测试用例类型用',
        `is_check` int(4) comment  '是否开启检查点 0开启 1不开启且报错忽略',
        `case_get_type` int(4) not null  comment  '用例取值方式 0全部内容 1jsonpath部分内容 2jsonpath部分内容如果是数组只取第一条3正则表达式',
        `json_path` varchar(100) comment  'JsonPath',
         `regular` varchar(100) comment  '正则表达式',
        PRIMARY KEY ( `id` ))ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

动作维护表

```
create table `action`(
        `id` int unsigned auto_increment, 
        `action_name` varchar(100) not null comment  ' 必填 动作名称', 
         `team_id`int(2) not null comment  '必填 团队id', 
        `remark` varchar(500) comment  '非必填 备注',  
        `action_type` int(2) not null comment  '动作类型 0 SQL类型 1 http请求 2 已有测试用例',
        `data_name` int(100) comment  '数据库名  sql 类型用', 
         `s_sql` varchar(500) comment  'sql 语句  sql 类型用',
         `url` varchar(500) comment  ' url http请求类型用',
        `get_value_type`  int(2) comment  '取值方式 http请求类型用 0 不需要返回值 1 h全部内容 2 JsonPath方式取值 3 正则',
         `pre_key_name` varchar(500) comment  ' 全部内容 ${pre.key}的key名 ',
        `json_path` varchar(500) comment  ' Json_Path方式取值 ',
        `regular`  varchar(500) comment  ' 正则表达式 ',
        `case_id` int comment  '用例 id 已有测试用例用',
        `case_key_name` varchar(500) comment  ' 全部内容 ${case.key}的key名 ',
        `case_json_path` varchar(500) comment  ' Json_Path方式取值 ',
        `deal_with` int(2) comment  '结果处理 0结果去掉首尾[] 1数字数组转字符串数组',
        PRIMARY KEY ( `id` ))ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

数据库维护表

```
CREATE TABLE IF NOT EXISTS `db_manager`(
        `id` INT UNSIGNED AUTO_INCREMENT,
        `db_ip` VARCHAR(100) NOT NULL,
        `db_port` VARCHAR(100) NOT NULL,
        `base_name` VARCHAR(100) NOT NULL,
        `user_name` VARCHAR(100) NOT NULL,
        `password` VARCHAR(100) NOT NULL,
        `db_name` VARCHAR(100) NOT NULL,
        PRIMARY KEY ( `id` )
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;
```

服务维护表

```
CREATE TABLE IF NOT EXISTS `db_manager`(
        `id` INT UNSIGNED AUTO_INCREMENT,
        `service_name` VARCHAR(100) NOT NULL,
        `service_url` VARCHAR(200) NOT NULL,
        PRIMARY KEY ( `id` )
        )ENGINE=InnoDB DEFAULT CHARSET=utf8;
```
  