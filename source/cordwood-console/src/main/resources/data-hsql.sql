--程序数据库数据初始化脚本

--注意：初始数据结果集中由于直接查询数据，因此数据内容直接作为列名，如果数据内容重复，需要指定别名

-- 新建默认管理员账号（默认密码为 123456 SHA1密文）
MERGE INTO PUBLIC.USERS t
  USING ( VALUES
            (1, 'sa@sparksoft.com.cn', '超级管理员', '朕主宰一切', '7c4a8d09ca3762af61e59520943dc26494f8941b', 1, 1, '2016-03-15 15:14:21'),
            (2, 'cfg@sparksoft.com.cn', '系统配置员', '小弟负责平台配置工作', '7c4a8d09ca3762af61e59520943dc26494f8941b', 1, 0, '2016-03-15 15:14:21'))
  AS vals(c1,c2,c3,c4,c5,c6,c7,c8) ON t.id = vals.c1
WHEN NOT MATCHED THEN
    INSERT (ID,EMAIL,NAME,ABOUTME,PASSWORD,STATUS,ROLE,CREATETIME) 
    VALUES (vals.c1,trim(vals.c2),trim(vals.c3),trim(vals.c4),trim(vals.c5),vals.c6,vals.c7,trim(vals.c8));

-- 內建的角色
MERGE INTO PUBLIC.ROLES t
  USING ( VALUES (1, 'ROLE_ADMIN'), (2, 'ROLE_CONFIG') )
  AS vals(c1, c2) ON t.NAME = trim(vals.c2)
WHEN NOT MATCHED THEN
    INSERT (ID, NAME) 
    VALUES (vals.c1, trim(vals.c2));
    
-- 内建的菜单项
MERGE INTO PUBLIC.SIDEBAR_ITEMS t
  USING ( VALUES
            (1,'仪表盘','fa fa-tachometer fa-fw','dashboard',1,'/admin/dashboard',null),
            (2,'插件管理','fa fa-plug fa-fw','plugins',2,'/admin/plugins',null),
            (3,'用户管理','fa fa-user-circle-o fa-fw','users',3, '/admin/users',null),
            (4,'权限管理','fa fa-key fa-fw','privileges',4,'/admin/privileges',null))
  AS vals(c1,c2,c3,c4,c5,c6,c7) ON t.URL = trim(vals.c6)
WHEN NOT MATCHED THEN
    INSERT (ID,DISPLAYTEXT,ICONCLASSES,NAME,SINK,URL,PARENTID) 
    VALUES (vals.c1,trim(vals.c2),trim(vals.c3),trim(vals.c4),vals.c5,trim(vals.c6),trim(vals.c7));

-- 向內建用户授权
MERGE INTO PUBLIC.USERS_ROLES t
  USING ( VALUES (1, 1), (2, 2) )
  AS vals(c1,c2) ON t.USERID = vals.c1 and t.ROLEID = vals.c2
WHEN NOT MATCHED THEN
    INSERT (USERID, ROLEID) 
    VALUES (vals.c1,vals.c2);
    
-- 默认角色可见菜单
MERGE INTO PUBLIC.SIDEBAR_ITEMS_ROLES t
  USING ( VALUES (1, 1), (1, 2), (2, 1), (2, 2) )
  AS vals(c1,c2) ON t.SIDEBARITEMID = vals.c1 and t.ROLEID = vals.c2
WHEN NOT MATCHED THEN
    INSERT (SIDEBARITEMID, ROLEID) 
    VALUES (vals.c1,vals.c2);