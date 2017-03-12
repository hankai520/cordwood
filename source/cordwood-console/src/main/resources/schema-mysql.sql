-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema cordwood
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema cordwood
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `cordwood` DEFAULT CHARACTER SET utf8 ;
USE `cordwood` ;

-- -----------------------------------------------------
-- Table `cordwood`.`sidebar_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`sidebar_items` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `displayText` VARCHAR(20) NOT NULL COMMENT '显示文本',
  `iconClasses` VARCHAR(30) NULL COMMENT '图标css样式',
  `name` VARCHAR(20) NOT NULL COMMENT '名称',
  `sink` INT NOT NULL COMMENT '排序（值大的靠后）',
  `url` VARCHAR(200) NULL COMMENT '菜单项对应页面的url',
  `parentId` INT NULL COMMENT '父级菜单项',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '边栏菜单项';


-- -----------------------------------------------------
-- Table `cordwood`.`plugins`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`plugins` (
  `name` VARCHAR(45) NOT NULL COMMENT '插件名',
  `description` VARCHAR(800) NULL COMMENT '简介',
  `developer` VARCHAR(100) NULL COMMENT '开发者',
  `isActive` TINYINT(1) NOT NULL COMMENT '是否启用',
  `displayName` VARCHAR(120) NULL COMMENT '显示名',
  `version` VARCHAR(100) NULL COMMENT '版本号',
  `packageId` VARCHAR(100) NOT NULL COMMENT '所属插件包',
  PRIMARY KEY (`name`))
ENGINE = InnoDB
COMMENT = '插件包中的插件信息';


-- -----------------------------------------------------
-- Table `cordwood`.`plugin_packages`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`plugin_packages` (
  `id` VARCHAR(100) NOT NULL COMMENT '唯一标识',
  `fileName` VARCHAR(100) NOT NULL COMMENT '插件包文件名',
  `developer` VARCHAR(100) NOT NULL COMMENT '插件包开发者',
  `description` VARCHAR(200) NOT NULL COMMENT '简介',
  `createTime` DATETIME NOT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `fileName_UNIQUE` (`fileName` ASC))
ENGINE = InnoDB
COMMENT = '安装的插件包信息';


-- -----------------------------------------------------
-- Table `cordwood`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
  `mobile` VARCHAR(20) NULL COMMENT '手机号',
  `name` VARCHAR(45) NOT NULL COMMENT '姓名',
  `aboutMe` VARCHAR(200) NULL COMMENT '个人简介',
  `password` VARCHAR(100) NOT NULL COMMENT '登陆密码（默认123456）',
  `status` TINYINT(1) NOT NULL COMMENT '帐号状态',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `updateTime` DATETIME NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '用户信息';


-- -----------------------------------------------------
-- Table `cordwood`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`roles` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
COMMENT = '角色';


-- -----------------------------------------------------
-- Table `cordwood`.`users_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`users_roles` (
  `userId` INT NOT NULL COMMENT '用户id',
  `roleId` INT NOT NULL COMMENT '角色id',
  PRIMARY KEY (`userId`, `roleId`))
ENGINE = InnoDB
COMMENT = '用户角色';


-- -----------------------------------------------------
-- Table `cordwood`.`sidebar_items_roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`sidebar_items_roles` (
  `sidebarItemId` INT NOT NULL COMMENT '菜单项id',
  `roleId` VARCHAR(45) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`sidebarItemId`, `roleId`))
ENGINE = InnoDB
COMMENT = '角色可见菜单';


-- -----------------------------------------------------
-- Table `cordwood`.`login_credentials`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`login_credentials` (
  `series` VARCHAR(64) NOT NULL COMMENT '序列号',
  `userName` VARCHAR(64) NOT NULL COMMENT '用户名',
  `token` VARCHAR(64) NOT NULL COMMENT '令牌',
  `lastUsed` DATETIME NOT NULL COMMENT '最近一次使用的时间',
  PRIMARY KEY (`series`))
ENGINE = InnoDB
COMMENT = '登陆凭证，用于记住登陆状态';


-- -----------------------------------------------------
-- Table `cordwood`.`plugin_requests`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`plugin_requests` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `pluginName` VARCHAR(45) NOT NULL COMMENT '插件名',
  `appId` INT NULL COMMENT '应用id',
  `clientIp` VARCHAR(200) NOT NULL COMMENT '客户端ip',
  `channel` TINYINT(1) NOT NULL COMMENT '渠道',
  `requestUrl` VARCHAR(400) NOT NULL COMMENT '请求地址',
  `requestMethod` VARCHAR(10) NOT NULL COMMENT '请求http方法',
  `requestDigest` VARCHAR(400) NULL COMMENT '请求信息摘要',
  `requestBytes` BIGINT(10) NOT NULL COMMENT '请求内容字节数',
  `responseBytes` BIGINT(10) NOT NULL COMMENT '响应内容字节数',
  `milliseconds` BIGINT(10) NOT NULL COMMENT '耗时',
  `succeeded` TINYINT(1) NOT NULL COMMENT '是否成功',
  `responseCode` INT NULL COMMENT 'HTTP响应状态码',
  `errors` TEXT NULL COMMENT '错误日志',
  `createTime` DATETIME NOT NULL COMMENT '请求时间',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '插件访问请求';


-- -----------------------------------------------------
-- Table `cordwood`.`apps`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `cordwood`.`apps` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(20) NOT NULL COMMENT '应用名称',
  `introduction` VARCHAR(200) NOT NULL COMMENT '简介',
  `platform` TINYINT(1) NOT NULL COMMENT '运行平台',
  `status` TINYINT(1) NOT NULL COMMENT '状态',
  `appKey` VARCHAR(120) NOT NULL COMMENT '应用标识',
  `secretKey` VARCHAR(120) NOT NULL COMMENT '密钥',
  `createTime` DATETIME NOT NULL COMMENT '创建时间',
  `updateTime` DATETIME NULL COMMENT '最近更新时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `appKey_UNIQUE` (`appKey` ASC))
ENGINE = InnoDB
COMMENT = '应用';


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;