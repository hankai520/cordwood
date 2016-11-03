-- HSQLDB 数据库程序数据库初始化脚本

-- 由 eclipselink 自动生成的建表脚本

-- 用户表
CREATE TABLE IF NOT EXISTS PUBLIC.USERS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL,
    CREATETIME TIMESTAMP NOT NULL, 
    LOGINID VARCHAR(100) NOT NULL, 
    MOBILE VARCHAR(20), 
    NAME VARCHAR(45) NOT NULL, 
    PASSWORD VARCHAR(100) NOT NULL, 
    ROLE TINYINT, 
    STATUS TINYINT, 
    UPDATETIME TIMESTAMP, 
    PRIMARY KEY (ID),
    UNIQUE (LOGINID),
    UNIQUE (MOBILE)
);

-- 插件表
CREATE TABLE IF NOT EXISTS PUBLIC.PLUGINS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL, 
    CREATETIME DATE NOT NULL, 
    DESCRIPTION VARCHAR(800), 
    ISACTIVE BOOLEAN, 
    NAME VARCHAR(45), 
    UPDATETIME DATE, 
    VERSION VARCHAR(100), 
    PACKAGEID INTEGER NOT NULL, 
    PRIMARY KEY (ID),
    UNIQUE (NAME)
);

-- 插件包表
CREATE TABLE IF NOT EXISTS PUBLIC.PLUGIN_PACKAGES (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL, 
    CHECKSUM VARCHAR(100) NOT NULL, 
    FILENAME VARCHAR(100) NOT NULL, 
    PRIMARY KEY (ID),
    UNIQUE (CHECKSUM)
);

-- 边栏菜单项表
CREATE TABLE IF NOT EXISTS PUBLIC.SIDEBAR_ITEMS (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL, 
    DISPLAYTEXT VARCHAR(255), 
    ICONCLASSES VARCHAR(255), 
    NAME VARCHAR(255), 
    SINK INTEGER, 
    URL VARCHAR(255), 
    PARENTID INTEGER, 
    PRIMARY KEY (ID)
);

-- 角色-权限表
CREATE TABLE IF NOT EXISTS PUBLIC.ROLES_PRIVILEGES (
    PRIVILEGEID INTEGER NOT NULL, 
    ROLEID INTEGER NOT NULL, 
    PRIMARY KEY (PRIVILEGEID, ROLEID)
);

-- 用户-角色表
CREATE TABLE IF NOT EXISTS PUBLIC.USERS_ROLES (
    USERID INTEGER NOT NULL, 
    ROLEID INTEGER NOT NULL, 
    PRIMARY KEY (USERID, ROLEID)
);

-- 权限表
CREATE TABLE IF NOT EXISTS PUBLIC.PRIVILEGES (
    ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 1 INCREMENT BY 1) NOT NULL, 
    NAME VARCHAR(100) NOT NULL, 
    PRIMARY KEY (ID)
);


--
-- Spring session 持久化
--
CREATE TABLE IF NOT EXISTS PUBLIC.SPRING_SESSION (
    SESSION_ID CHAR(36),
    CREATION_TIME BIGINT NOT NULL,
    LAST_ACCESS_TIME BIGINT NOT NULL,
    MAX_INACTIVE_INTERVAL INT NOT NULL,
    PRINCIPAL_NAME VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (SESSION_ID)
);

CREATE INDEX IF NOT EXISTS SPRING_SESSION_IX1 ON SPRING_SESSION (LAST_ACCESS_TIME);

CREATE TABLE IF NOT EXISTS PUBLIC.SPRING_SESSION_ATTRIBUTES (
    SESSION_ID CHAR(36),
    ATTRIBUTE_NAME VARCHAR(200),
    ATTRIBUTE_BYTES LONGVARBINARY,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_ID) REFERENCES SPRING_SESSION(SESSION_ID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS SPRING_SESSION_ATTRIBUTES_IX1 ON SPRING_SESSION_ATTRIBUTES (SESSION_ID);
