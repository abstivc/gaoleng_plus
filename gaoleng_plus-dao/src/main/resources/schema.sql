-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `GL_FILE`;
-- 创建一个表，指定了4个属性：id、年龄、身高、体重。最后指定了id是唯一不能重复的键值
create table gl_support.gl_file
(
    id          varchar(64) not null comment '主键',
    name        varchar(64)                        not null comment '文件名称',
    type        varchar(50)                         not null comment '后缀名',
    size        int                                 not null comment '文件大小',
    path        varchar(64)                        not null comment '文件保存路径',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建日期',
    create_user varchar(50)                         null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    update_user varchar(50)                         null comment '更新人',
    constraint gl_file_id_uindex
        unique (id),
    constraint gl_file_name_uindex
        unique (name)
) DEFAULT CHARSET=utf8
    comment '文件主表';


-- auto-generated definition
create table gl_user
(
    id          varchar(64)                         not null comment '主键',
    user_name        varchar(64)                         not null comment '用户名',
    password        varchar(64)                         not null comment '密码',
    email   varchar(64)  not null comment '邮箱',
    phone   varchar(64)  not null comment '手机号',
    create_time timestamp default CURRENT_TIMESTAMP null comment '创建日期',
    create_user varchar(50)                         null comment '创建人',
    update_time timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '创建时间',
    update_user varchar(50)                         null comment '更新人',
    constraint gl_user_id_uindex
        unique (id)
)
    comment '用户表' charset = utf8;
