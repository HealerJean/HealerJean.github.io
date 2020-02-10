drop table if exists item_adzone_ref;
create table item_adzone_ref
(
    id                  bigint(20) unsigned not null auto_increment comment '主键',
    ref_alimama_info_id bigint(20) unsigned not null default 0 comment '淘宝联盟账号主键',
    ref_adzone_id       bigint(20) unsigned not null default 0 comment '推广位主键',
    ref_item_good_id    bigint(20) unsigned not null default 0 comment '商品主键',
    qr_url              varchar(512)        not null default '' comment '二维码图片url',
    create_user         bigint(20) unsigned not null default 0 comment '创建人',
    create_name         varchar(64)         not null default '' comment '创建人名称',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    update_user         bigint(20) unsigned not null default 0 comment '更新人',
    update_name         varchar(64)         not null default '' comment '更新人名称',
    update_time         datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_item_adzone (ref_alimama_info_id, ref_adzone_id, ref_item_good_id) using btree comment '该淘宝联盟账号下的商品唯一'
) engine = innodb comment = '商品模块-商品渠道对应表';



drop table if exists item_good;
create table item_good
(
    id                  bigint(20) unsigned not null auto_increment comment '主键',
    ref_alimama_info_id bigint(20) unsigned not null comment '淘宝联盟账号主键',
    ref_adzone_id       bigint(20) unsigned not null comment '推广位',
    taokouling          varchar(32)         not null comment '淘口令',
    coupon_click_url    varchar(1024)       not null comment '推广链接/优惠券领取url',
    item_id             bigint(20) unsigned not null comment '商品id',
    item_url            varchar(512)        not null comment '商品链接url',
    coupo_url           varchar(512)        not null default '' comment '优惠券链接url',
    activity_id         varchar(64)         not null default '' comment '券id',
    shop_id             bigint(20) unsigned not null comment '店铺id',
    shop_name           varchar(64)         not null comment '店铺名称',
    re_title            varchar(64)         not null comment '文案标题',
    title               varchar(64)         not null comment '商品标题',
    description         varchar(512)        not null comment '商品描述',
    origin_price        decimal(19, 2)      not null comment '原价',
    now_price           decimal(19, 2)      not null comment '现价',
    coupon_price        decimal(19, 2)      not null comment '券额',
    before_origin_price decimal(19, 2)      not null comment '历史原价',
    before_now_price    decimal(19, 2)      not null comment '历史现价',
    before_coupon_price decimal(19, 2)      not null comment '历史券额',
    pict_url            varchar(512)        not null comment '商品主图url',
    small_imges         varchar(2048)       not null default '' comment '图片url集合',
    volume              bigint(20) unsigned not null comment '销量',
    tmall               tinyint(1)          not null comment '是否天猫',
    remark              varchar(512)        not null default '' comment '备注',
    pub_status          varchar(32)         not null comment '商品发布状态',
    pub_user            bigint(20) unsigned not null default 0 comment '发布人',
    pub_name            varchar(64)         not null default '' comment '发布人名称',
    sort                int(11) unsigned    not null default 0 comment '排序id',
    sort_user           bigint(20) unsigned not null default 0 comment '排序人',
    sort_name           varchar(64)         not null default '' comment '排序人名称',
    qr_url              varchar(512)        not null default '' comment '商品带二维码图片',
    status              varchar(32)         not null default '' comment '商品状态',
    transform           tinyint(1)          not null default 0 comment '是否改变',
    create_user         bigint(20) unsigned not null default 0 comment '创建人',
    create_name         varchar(64)         not null default '' comment '创建人名称',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    update_user         bigint(20) unsigned not null default 0 comment '更新人',
    update_name         varchar(64)         not null default '' comment '更新人名称',
    update_time         datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_alimama_item (ref_alimama_info_id, item_id) using btree comment '该淘宝联盟账号下的商品唯一'
) engine = innodb comment = '商品模块-商品表';



drop table if exists sys_alimama_adzone;
create table sys_alimama_adzone
(
    id                  bigint(20)          not null auto_increment,
    ref_alimama_info_id bigint(20) unsigned not null comment '淘宝联盟阿里妈妈账号主键',
    alimama_adzone      varchar(64)         not null comment '推广位',
    adzone_name         varchar(64)         not null comment '推广位名称',
    adzone_type         varchar(32)         not null comment '推广位类型',
    status              varchar(32)         not null comment '状态',
    create_user         bigint(20) unsigned not null default 0 comment '创建人',
    create_name         varchar(64)         not null default '' comment '创建人名称',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    update_user         bigint(20) unsigned not null default 0 comment '更新人',
    update_name         varchar(64)         not null default '' comment '更新人名称',
    update_time         datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_alimama_adzone (ref_alimama_info_id, alimama_adzone) using btree comment '推广位唯一'
) engine = innodb comment = '系统模块-淘宝联盟推广位表';



drop table if exists sys_alimama_info;
create table sys_alimama_info
(
    id               bigint(20) unsigned not null auto_increment comment '主键',
    ref_adzone_id    bigint(20) unsigned not null default 0 comment '默认推广位',
    taobao_user_id   bigint(20) unsigned not null comment '淘宝用户id',
    taobao_user_name varchar(32)         not null comment '淘宝用户名',
    appkey           varchar(50)         not null default '' comment '淘宝联盟appkey',
    secret           varchar(100)        not null default '' comment '淘宝联盟secret',
    status           varchar(32)         not null default '' comment '商品状态',
    create_user      bigint(20) unsigned not null default 0 comment '创建人',
    create_name      varchar(64)         not null default '' comment '创建人名称',
    create_time      datetime            not null default current_timestamp comment '创建时间',
    update_user      bigint(20) unsigned not null default 0 comment '更新人',
    update_name      varchar(64)         not null default '' comment '更新人名称',
    update_time      datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_taobao_user (taobao_user_id) using btree comment '该淘宝联盟账号唯一'
) engine = innodb comment = '系统模块-淘宝联盟用户信息';

drop table if exists sys_dictionary_data;
create table sys_dictionary_data
(
    id           bigint(20) unsigned not null auto_increment comment '主键',
    data_key     varchar(32)         not null comment '数据类型type_key  表dictionary_type',
    data_value   varchar(64)         not null comment '字典数据 描述',
    ref_type_key varchar(32)         not null comment '数据类型type_key  表dictionary_type',
    sort         int(11) unsigned    not null default 0 comment '排序',
    status       varchar(8)          not null comment '状态',
    create_user  bigint(20) unsigned not null default 0 comment '创建人',
    create_name  varchar(64)         not null default '' comment '创建人名称',
    create_time  datetime            not null default current_timestamp comment '创建时间',
    update_user  bigint(20) unsigned not null default 0 comment '更新人',
    update_name  varchar(64)         not null default '' comment '更新人名称',
    update_time  datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_typekey_datakey (ref_type_key, data_key) using btree
) engine = innodb comment = '系统模块-字典表数据';


drop table if exists sys_dictionary_type;
create table sys_dictionary_type
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    type_key    varchar(32)         not null comment '字典类型',
    type_desc   varchar(64)         not null comment '字典类型 描述',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_typekey (type_key) using btree
) engine = innodb comment = '系统模块-字典表类型';


drop table if exists `sys_district`;
create table `sys_district`
(
    id            bigint(20) unsigned not null auto_increment comment '主键',
    province_code varchar(8)          null     default '' comment '省-编码',
    province_name varchar(64)         null     default '' comment '省-名称',
    city_code     varchar(8)          null     default '' comment '城市-编码',
    city_name     varchar(64)         null     default '' comment '城市-名称',
    district_code varchar(8)          null     default '' comment '区/县-编码',
    district_name varchar(64)         null     default '' comment '区/县-名称',
    status        varchar(8)          null     default '' comment '状态: 10：有效，99：无效',
    update_time   datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (`id`) using btree,
    index `idx_province` (`province_code`) using btree comment '省份-索引',
    index `idx_city` (`city_code`) using btree comment '城市-索引',
    index `idx_district` (`district_code`) using btree comment '地区-索引'
) engine = innodb comment = '系统模块-地区信息表';



drop table if exists sys_email_log;
create table sys_email_log
(
    id            bigint(20) unsigned not null auto_increment comment '主键',
    type          varchar(32)         not null comment '邮件类型 数据字典',
    subject       varchar(64)         not null comment '邮件标题',
    content       text                not null comment '邮件内容',
    send_email    varchar(64)         not null comment '发送邮箱',
    receive_mails varchar(255)        not null comment '接收邮箱',
    status        varchar(8)          not null comment 'success 发送成功 ，error 发送失败 ',
    msg           varchar(255)        not null comment '发送状态信息 发送成功、异常信息',
    create_time   datetime            not null default current_timestamp comment '创建时间',
    update_time   datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '系统模块-邮件记录表';


drop table if exists sys_menu;
create table sys_menu
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    ref_system_code varchar(32)         not null comment '系统code',
    menu_name       varchar(64)         not null comment '菜单名称',
    url             varchar(255)        not null default '' comment '菜单调用地址',
    method          varchar(32)         not null default '' comment '调用方法（get，post，put，delete）',
    pid             bigint(20) unsigned not null default 0 comment '父级id',
    pchain          varchar(255)        not null default '' comment '父链id，“,”分隔',
    description      varchar(255)        not null default '' comment '描述',
    sort            int(11) unsigned    not null default 0 comment '显示排序',
    icon            varchar(255)        not null default '' comment '菜单图标',
    front_key       varchar(32)         not null default '' comment '前端菜单标识（前端菜单唯一标识）',
    is_permission   varchar(2)          not null default '' comment '是否需要权限拦截，10：需要，99：不需要',
    menu_type       varchar(2)          not null default '' comment '菜单类型：0: 后端路径, 1:前端菜单，2:非展示前端菜单',
    status          varchar(8)          not null comment '状态',
    create_user     bigint(20) unsigned not null default 0 comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_user     bigint(20) unsigned not null default 0 comment '更新人',
    update_name     varchar(64)         not null default '' comment '更新人名称',
    update_time     datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '系统模块-菜单表';



drop table if exists sys_role;
create table sys_role
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    role_name       varchar(64)         not null comment '角色名称',
    ref_system_code varchar(32)         not null comment '系统code',
    description      varchar(255)        not null default '' comment '描述',
    status          varchar(8)          not null comment '状态',
    create_user     bigint(20) unsigned not null default 0 comment '创建人',
    create_name     varchar(64)         not null default '' comment '创建人名称',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_user     bigint(20) unsigned not null default 0 comment '更新人',
    update_name     varchar(64)         not null default '' comment '更新人名称',
    update_time     datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '系统模块-角色表';


drop table if exists sys_role_menu_ref;
create table sys_role_menu_ref
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    ref_role_id bigint(20) unsigned not null comment '角色id',
    ref_menu_id bigint(20) unsigned not null comment '菜单id',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    index idx_role_id (ref_role_id) using btree comment '角色id索引'
) engine = innodb comment = '系统模块-角色与菜单关系表';


drop table if exists sys_template;
create table sys_template
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    name        varchar(32)         not null comment '模板名称',
    content     text                not null comment '模板内容',
    type        varchar(32)         not null comment '业务类型（邮箱，合同，短信等） 字典表',
    description varchar(255)        not null default '' comment '模板描述',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '系统模块-模板';


drop table if exists sys_user_info;
create table sys_user_info
(
    id          bigint(20)          not null auto_increment,
    user_name   varchar(64)         not null comment '用户名',
    real_name   varchar(64)         not null comment '真实姓名',
    email       varchar(64)         not null comment '邮箱',
    telephone   varchar(20)         not null comment '手机号',
    password    varchar(128)        not null comment '密码',
    user_type   varchar(32)         not null comment '用户类型',
    status      varchar(32)         not null comment '用户状态',
    salt        varchar(128)        not null comment '密码随机盐',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    unique index uk_username (user_name) using btree comment '用户名唯一',
    unique index uk_email (email) using btree comment '邮箱唯一',
    primary key (id) using btree
) engine = innodb comment = '系统模块-平台用户';



drop table if exists sys_user_alimama_ref;
create table sys_user_alimama_ref
(
    id                  bigint(20) unsigned not null auto_increment comment '主键',
    ref_user_id         bigint(20) unsigned not null comment '用户id',
    ref_alimama_info_id bigint(20) unsigned not null comment '阿里妈妈信息表id',
    manager             tinyint(1)          not null default 0 comment '是否管理人管理该阿里妈妈信息',
    status              varchar(8)          not null comment '状态',
    create_user         bigint(20) unsigned not null default 0 comment '创建人',
    create_name         varchar(64)         not null default '' comment '创建人名称',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    update_user         bigint(20) unsigned not null default 0 comment '更新人',
    update_name         varchar(64)         not null default '' comment '更新人名称',
    update_time         datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_user_id (ref_user_id) using btree comment '用户id索引'
) engine = innodb comment = '系统模块-用户与阿里妈妈对于关系表';



drop table if exists sys_user_role_ref;
create table sys_user_role_ref
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    ref_user_id bigint(20) unsigned not null comment '用户id',
    ref_role_id bigint(20) unsigned not null comment '角色id',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    index idx_user_id (ref_user_id) using btree comment '用户id索引'
) engine = innodb comment = '系统模块-用户与角色关系表';


drop table if exists `sys_domain`;
create table `sys_domain`
(
    id                  bigint(20) unsigned not null auto_increment comment '主键',
    ref_alimama_info_id bigint(20) unsigned not null comment '阿里妈妈信息表id',
    type                varchar(32)         not null default '' comment '域名类型',
    value               varchar(128)        not null comment '域名的值，中间必须有.iku.',
    status              varchar(8)          not null comment '状态',
    create_user         bigint(20) unsigned not null default 0 comment '创建人',
    create_name         varchar(64)         not null default '' comment '创建人名称',
    create_time         datetime            not null default current_timestamp comment '创建时间',
    update_user         bigint(20) unsigned not null default 0 comment '更新人',
    update_name         varchar(64)         not null default '' comment '更新人名称',
    update_time         datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_alimama_domain_name (ref_alimama_info_id, type) using btree,
    unique index uk_domain (value) using btree
) engine = innodb comment = '系统模块-域名管理';



drop table if exists wechat_access_token;
create table wechat_access_token
(
    id             bigint(20) unsigned not null auto_increment comment '主键',
    wechat_buss_id bigint(20)          not null comment '微信公众号主键',
    access_token   varchar(1000)       not null default '' comment '微信公众号accesstoken',
    status         varchar(8)          not null comment '状态',
    create_user    bigint(20) unsigned not null default 0 comment '创建人',
    create_name    varchar(64)         not null default '' comment '创建人名称',
    create_time    datetime            not null default current_timestamp comment '创建时间',
    update_user    bigint(20) unsigned not null default 0 comment '更新人',
    update_name    varchar(64)         not null default '' comment '更新人名称',
    update_time    datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '微信模块-accesstoken';



drop table if exists wechat_business;
create table wechat_business
(
    id          bigint(20) unsigned not null auto_increment comment '主键',
    name        varchar(20)         not null comment '公众号名字',
    wechat_no   varchar(20)         not null comment '微信号',
    type        varchar(32)         not null comment '公众号类型',
    app_id      varchar(64)         not null comment 'appid',
    app_secret  varchar(64)         not null comment 'app_secret ，秘钥',
    token       varchar(50)         not null comment 'token令牌',
    status      varchar(8)          not null comment '状态',
    create_user bigint(20) unsigned not null default 0 comment '创建人',
    create_name varchar(64)         not null default '' comment '创建人名称',
    create_time datetime            not null default current_timestamp comment '创建时间',
    update_user bigint(20) unsigned not null default 0 comment '更新人',
    update_name varchar(64)         not null default '' comment '更新人名称',
    update_time datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '微信模块-微信公众号表';



drop table if exists wechat_menu;
create table wechat_menu
(
    id             bigint(20) unsigned not null auto_increment comment '主键',
    wechat_buss_id bigint(20) unsigned not null,
    url            varchar(500)        not null default '' comment '菜单链接url',
    event_key      varchar(64)         not null default '' comment '菜单事件ey',
    name           varchar(20)         not null default '' comment '菜地名字',
    value          varchar(256)        not null default '' comment '菜单回复内容',
    w_order        int(11) unsigned    not null default 0 comment '菜单从左到右第几位，不是菜单为0 ',
    h_order        int(11) unsigned    not null default 0 comment '菜单从上到下第几位，不是菜单为0 ',
    type           varchar(32)         not null default '' comment '菜单类型 ，Bottom,，URL,Text,Image',
    use_type       varchar(32)         not null default '' comment 'Menu，Other',
    status         varchar(8)          not null comment '状态',
    create_user    bigint(20) unsigned not null default 0 comment '创建人',
    create_name    varchar(64)         not null default '' comment '创建人名称',
    create_time    datetime            not null default current_timestamp comment '创建时间',
    update_user    bigint(20) unsigned not null default 0 comment '更新人',
    update_name    varchar(64)         not null default '' comment '更新人名称',
    update_time    datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree
) engine = innodb comment = '微信模块-菜单';



drop table if exists wechat_online_user;
create table wechat_online_user
(
    id             bigint(20) unsigned not null auto_increment comment '主键',
    wechat_buss_id bigint(20)          not null comment ' 微信公众号主键 ',
    open_id        varchar(128)        not null comment '微信openid唯一',
    content        varchar(1024)       not null comment '消息内容，可能是文本，也可能是url',
    type           varchar(32)         not null comment '用户与公众号交互的类型',
    create_time    datetime            not null default current_timestamp comment '创建时间',
    primary key (id) using btree
) engine = innodb comment = '微信模块----与公众号交互的用户';



drop table if exists wechat_user_info;
create table wechat_user_info
(
    id              bigint(20) unsigned not null auto_increment comment '主键',
    wechat_buss_id  bigint(20)          not null comment ' 微信公众号主键 ',
    open_id         varchar(128)        not null comment '微信openid唯一',
    union_id        varchar(128)        not null default '' comment '只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。',
    nick_name       varchar(64)         not null default '' comment '用户的昵称',
    subscribe       int(11)             not null comment '是否订阅 1 订阅 0 取消订阅',
    sex             int(11)             not null comment '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
    city            varchar(32)         not null default '' comment '用户所在城市',
    country         varchar(32)         not null default '' comment '用户所在国家',
    province        varchar(32)         not null default '' comment '用户所在省份',
    language        varchar(64)         not null default '' comment '用户的语言，简体中文为zh_cn',
    head_img_url    varchar(512)        not null default '' comment '用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像url将失效',
    subscribe_time  datetime            not null default current_timestamp on update current_timestamp(0) comment '用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间',
    remark          varchar(256)        not null default '' comment '公众号运营者对粉丝的备注，公众号运营者可在微信公众平台用户管理界面对粉丝添加备注',
    group_id        int(11) unsigned    not null default 0 comment '用户所在的分组id（兼容旧的用户分组接口）',
    tagid_list      varchar(128)        not null default '' comment '用户被打上的标签id列表',
    subscribe_scene varchar(64)         not null default '' comment '返回用户关注的渠道来源，add_scene_search 公众号搜索，add_scene_account_migration 公众号迁移，add_scene_profile_card 名片分享，add_scene_qr_code 扫描二维码，add_scene_profile_ link 图文页内名称点击，add_scene_profile_item 图文页右上角菜单，add_scene_paid 支付后关注，add_scene_others 其他',
    qr_scene        int(11) unsigned    not null default 0 comment '二维码扫码场景（开发者自定义）',
    qr_scenestr     varchar(64)         not null default '' comment '二维码扫码场景描述（开发者自定义）',
    create_time     datetime            not null default current_timestamp comment '创建时间',
    update_time     datetime            not null default current_timestamp on update current_timestamp(0) comment '更新时间',
    primary key (id) using btree,
    unique index uk_open_id (open_id) using btree
) engine = innodb comment = '微信模块-用户表';




