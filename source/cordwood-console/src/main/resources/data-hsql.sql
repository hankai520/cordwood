--程序数据库数据初始化脚本

--注意：初始数据结果集中由于直接查询数据，因此数据内容直接作为列名，如果数据内容重复，需要指定别名

-- 新建默认管理员账号（默认密码为 123456 SHA1密文）
MERGE INTO PUBLIC.USERS t
  USING ( VALUES
            ('cfg', '系统配置员', '7c4a8d09ca3762af61e59520943dc26494f8941b', 1, 0, '2016-03-15 15:14:21'),
            ('sa', '超级管理员', '7c4a8d09ca3762af61e59520943dc26494f8941b', 1, 1, '2016-03-15 15:14:21'))
  AS vals(c1,c2,c3,c4,c5,C6) ON t.loginId = vals.c1
WHEN NOT MATCHED THEN
    INSERT (LOGINID,NAME,PASSWORD,STATUS,ROLE,CREATETIME) 
    VALUES (vals.c1,vals.c2,vals.c3,vals.c4,vals.c5,vals.c6);