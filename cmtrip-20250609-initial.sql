/*
 Navicat Premium Data Transfer

 Source Server         : LOCALHOST
 Source Server Type    : MySQL
 Source Server Version : 80041
 Source Host           : localhost:3306
 Source Schema         : cmtrip

 Target Server Type    : MySQL
 Target Server Version : 80041
 File Encoding         : 65001

 Date: 09/06/2025 15:00:00
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ct_action
-- ----------------------------
DROP TABLE IF EXISTS `ct_action`;
CREATE TABLE `ct_action` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'action id',
  `action_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'action name',
  `action_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'action type',
  `action_start_time` datetime DEFAULT NULL COMMENT 'action start time',
  `action_end_time` datetime DEFAULT NULL COMMENT 'action end time',
  `fare` int DEFAULT '0' COMMENT 'action fare',
  `fare_currency` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'action fare currency',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'action description',
  `img_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'action image virtual path',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='action table';

-- ----------------------------
-- Records of ct_action
-- ----------------------------
BEGIN;
INSERT INTO `ct_action` (`id`, `action_name`, `action_type`, `action_start_time`, `action_end_time`, `fare`, `fare_currency`, `description`, `img_path`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'Test Action', 'Rest', '2025-06-01 00:00:00', '2025-06-01 08:00:00', 10000, 'JPY', 'Test Description', NULL, '1', '2025-06-03 12:20:46', '1', '2025-06-01 15:00:00', 'Test Action Remark');
COMMIT;

-- ----------------------------
-- Table structure for ct_action_pass_entity
-- ----------------------------
DROP TABLE IF EXISTS `ct_action_pass_entity`;
CREATE TABLE `ct_action_pass_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `action_id` bigint NOT NULL,
  `pass_entity_id` bigint NOT NULL,
  `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of ct_action_pass_entity
-- ----------------------------
BEGIN;
INSERT INTO `ct_action_pass_entity` (`id`, `action_id`, `pass_entity_id`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, 1, '1', '2025-06-01 15:00:00', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_menu
-- ----------------------------
DROP TABLE IF EXISTS `ct_menu`;
CREATE TABLE `ct_menu` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'menu id',
  `menu_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'menu name',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'menu path',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'menu component name',
  `parent_id` bigint DEFAULT NULL COMMENT 'parent id',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'menu icon',
  `sort_order` int DEFAULT '0' COMMENT 'menu sort order',
  `visible` tinyint(1) DEFAULT '1' COMMENT 'menu visible',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='menu table';

-- ----------------------------
-- Records of ct_menu
-- ----------------------------
BEGIN;
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (1, 'SYSTEM MANAGEMENT', '/system', 'layout', NULL, 'setting', 1, 1, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (2, 'USERS', '/system/user', 'system/user', 1, 'user', 1, 1, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (3, 'ROLES', '/system/role', 'system/role', 1, 'role', 2, 1, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (4, 'PERMISSIONS', '/system/permission', 'system/permission', 1, 'permission', 3, 1, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (5, 'PASS', '/pass', 'pass', NULL, 'pass', 2, 1, '2025-06-01 15:00:00', NULL, '2025-06-01 15:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (6, 'TRIPS', '/trip', 'trip', NULL, 'trip', 3, 1, '2025-05-20 10:00:00', NULL, '2025-06-01 15:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (7, 'ACTIONS', '/action', 'action', NULL, 'action', 4, 1, '2025-05-20 10:00:00', NULL, '2025-06-01 15:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (8, 'PASS ENTITIES', '/pass/pass-entity', 'pass/pass-entity', 5, 'pass-entity', 5, 1, '2025-06-01 15:00:00', NULL, '2025-06-01 15:00:00', NULL, NULL);
INSERT INTO `ct_menu` (`id`, `menu_name`, `path`, `component`, `parent_id`, `icon`, `sort_order`, `visible`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (9, 'PASS', '/pass/pass', 'pass/pass', 5, 'pass', 1, 1, '2025-06-01 15:00:00', NULL, '2025-06-01 15:00:00', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_pass
-- ----------------------------
DROP TABLE IF EXISTS `ct_pass`;
CREATE TABLE `ct_pass` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pass_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
  `issue_company` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `valid_days` int DEFAULT NULL,
  `fare` int DEFAULT '0',
  `fare_currency` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `img_path` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `description` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `is_delete` tinyint(1) DEFAULT '0',
  `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of ct_pass
-- ----------------------------
BEGIN;
INSERT INTO `ct_pass` (`id`, `pass_name`, `issue_company`, `valid_days`, `fare`, `fare_currency`, `img_path`, `description`, `is_delete`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'Test Pass', 'Test Company', 5, 12000, 'JPY', NULL, '', 0, '1', NULL, '1', '2025-06-03 12:59:11', '');
COMMIT;

-- ----------------------------
-- Table structure for ct_pass_entity
-- ----------------------------
DROP TABLE IF EXISTS `ct_pass_entity`;
CREATE TABLE `ct_pass_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `pass_id` bigint DEFAULT NULL,
  `entity_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `pass_start_date` date DEFAULT NULL,
  `pass_end_date` date DEFAULT NULL,
  `description` varchar(1000) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of ct_pass_entity
-- ----------------------------
BEGIN;
INSERT INTO `ct_pass_entity` (`id`, `pass_id`, `entity_name`, `pass_start_date`, `pass_end_date`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, 'Test Entity', '2025-06-01', '2025-06-05', 'Des', '1', NULL, '1', '2025-06-01 15:00:00', 'Rem');
INSERT INTO `ct_pass_entity` (`id`, `pass_id`, `entity_name`, `pass_start_date`, `pass_end_date`, `description`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 1, 'Second Entity', '2025-06-17', '2025-06-21', '', '1', '2025-06-01 15:00:00', NULL, NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for ct_permission
-- ----------------------------
DROP TABLE IF EXISTS `ct_permission`;
CREATE TABLE `ct_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'permission id',
  `permission_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'permission name',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint unsigned DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `permission_name` (`permission_name`),
  KEY `idx_permission_name` (`permission_name`)
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='permission table';

-- ----------------------------
-- Records of ct_permission
-- ----------------------------
BEGIN;
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (1, 'list-actions-by-trip-id', '2025-05-21 15:13:44', NULL, '2025-05-21 15:13:44', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (2, 'add-action', '2025-05-21 15:14:01', NULL, '2025-05-21 15:14:01', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (3, 'edit-action', '2025-05-21 15:14:12', NULL, '2025-05-21 15:14:12', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (4, 'delete-action', '2025-05-21 15:14:20', NULL, '2025-05-21 15:14:20', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (5, 'list-all-permissions', '2025-05-21 15:32:25', NULL, '2025-05-21 15:32:25', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (6, 'get-permission-list-by-role-id', '2025-05-21 15:32:50', NULL, '2025-05-21 15:32:50', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (7, 'add-permission', '2025-05-21 15:33:02', NULL, '2025-05-21 15:33:02', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (8, 'edit-permission', '2025-05-21 15:34:12', NULL, '2025-05-21 15:34:12', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (9, 'delete-permission', '2025-05-21 15:34:31', NULL, '2025-05-21 15:34:31', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (10, 'list-all-roles', '2025-05-21 15:35:23', NULL, '2025-05-21 15:35:23', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (11, 'list-all-roles-besides-superAdmin', '2025-05-21 15:42:09', NULL, '2025-05-21 15:42:09', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (12, 'get-role-list-by-user-id', '2025-05-21 15:43:02', NULL, '2025-05-21 15:43:02', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (13, 'add-role', '2025-05-21 15:43:11', NULL, '2025-05-21 15:43:11', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (14, 'edit-role', '2025-05-21 15:43:16', NULL, '2025-05-21 15:43:16', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (15, 'delete-role', '2025-05-21 15:43:23', NULL, '2025-05-21 15:43:23', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (16, 'update-role-permissions', '2025-05-21 15:43:32', NULL, '2025-05-21 15:43:32', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (17, 'get-role-permissions', '2025-05-21 15:43:44', NULL, '2025-05-21 15:43:44', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (18, 'list-my-trips', '2025-05-21 19:16:38', NULL, '2025-05-21 19:16:38', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (19, 'add-trip', '2025-05-21 19:16:44', NULL, '2025-05-21 19:16:44', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (20, 'edit-trip', '2025-05-21 19:17:50', NULL, '2025-05-21 19:17:50', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (21, 'delete-trip', '2025-05-21 19:18:00', NULL, '2025-05-21 19:18:00', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (22, 'delete-user', '2025-05-21 19:41:18', NULL, '2025-05-21 19:41:18', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (23, 'get-user-info-by-id', '2025-05-21 19:41:52', NULL, '2025-05-21 19:41:52', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (24, 'get-user-list', '2025-05-21 19:42:03', NULL, '2025-05-21 19:42:03', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (25, 'get-user-roles', '2025-05-21 19:42:10', NULL, '2025-05-21 19:42:10', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (26, 'update-user-roles', '2025-05-21 19:42:17', NULL, '2025-05-21 19:42:17', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (27, 'get-user-permissions', '2025-05-21 19:42:25', NULL, '2025-05-21 19:42:25', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (28, 'list-all-passes', '2025-06-03 10:55:34', NULL, '2025-06-03 10:55:34', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (29, 'add-pass', '2025-06-03 10:55:42', NULL, '2025-06-03 10:55:42', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (30, 'edit-pass', '2025-06-03 10:55:49', NULL, '2025-06-03 10:55:49', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (31, 'delete-pass', '2025-06-03 10:55:56', NULL, '2025-06-03 10:55:56', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (32, 'list-my-all-pass-entities', '2025-06-05 17:26:07', NULL, '2025-06-05 17:26:07', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (33, 'list-my-pass-entities-by-pass-id', '2025-06-05 17:26:13', NULL, '2025-06-05 17:26:13', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (34, 'add-pass-entity', '2025-06-05 17:26:19', NULL, '2025-06-05 17:26:19', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (35, 'edit-pass-entity', '2025-06-05 17:26:24', NULL, '2025-06-05 17:26:24', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (36, 'delete-pass-entity', '2025-06-05 17:26:29', NULL, '2025-06-05 17:26:29', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (37, 'bind-action-and-pass-entity', '2025-06-07 19:46:20', NULL, '2025-06-07 19:46:20', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (38, 'release-pass-entity-from-action', '2025-06-07 19:46:32', NULL, '2025-06-07 19:46:32', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (39, 'get-bound-pass-entity-of-action', '2025-06-07 19:46:58', NULL, '2025-06-07 19:46:58', NULL, NULL);
INSERT INTO `ct_permission` (`id`, `permission_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (40, 'get-pass-entity-saved-fare', '2025-06-09 13:17:30', NULL, '2025-06-09 13:17:30', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_role
-- ----------------------------
DROP TABLE IF EXISTS `ct_role`;
CREATE TABLE `ct_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'role id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'role name',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint unsigned DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `role_name` (`role_name`),
  KEY `idx_role_name` (`role_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='role table';

-- ----------------------------
-- Records of ct_role
-- ----------------------------
BEGIN;
INSERT INTO `ct_role` (`id`, `role_name`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (1, 'superAdmin', '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `ct_role_permission`;
CREATE TABLE `ct_role_permission` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` bigint unsigned NOT NULL COMMENT 'role id',
  `permission_id` bigint unsigned NOT NULL COMMENT 'permission id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint unsigned DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_role_permission_permission` (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='role-permission relation table';

-- ----------------------------
-- Records of ct_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (2, 1, 2, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (3, 1, 7, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (4, 1, 13, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (5, 1, 19, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (6, 1, 4, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (7, 1, 9, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (8, 1, 15, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (9, 1, 21, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (10, 1, 22, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (11, 1, 3, '2025-05-22 12:16:38', 1, '2025-05-22 12:16:38', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (12, 1, 8, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (13, 1, 14, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (14, 1, 20, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (15, 1, 6, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (16, 1, 12, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (17, 1, 17, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (18, 1, 23, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (19, 1, 24, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (20, 1, 27, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (21, 1, 25, '2025-05-22 12:16:46', 1, '2025-05-22 12:16:46', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (22, 1, 1, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (23, 1, 5, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (24, 1, 10, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (25, 1, 11, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (26, 1, 18, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (27, 1, 16, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
INSERT INTO `ct_role_permission` (`id`, `role_id`, `permission_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (28, 1, 26, '2025-05-22 12:16:54', 1, '2025-05-22 12:16:53', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_trip
-- ----------------------------
DROP TABLE IF EXISTS `ct_trip`;
CREATE TABLE `ct_trip` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'trip id',
  `trip_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'trip name',
  `trip_start_date` date DEFAULT NULL COMMENT 'trip start date',
  `trip_end_date` date DEFAULT NULL COMMENT 'trip end date',
  `trip_from` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'trip departure point',
  `trip_to` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'trip destination',
  `trip_budget` int DEFAULT NULL COMMENT 'trip budget',
  `trip_budget_currency` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'trip budget currency',
  `img_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT 'trip image virtual path',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='trip table';

-- ----------------------------
-- Records of ct_trip
-- ----------------------------
BEGIN;
INSERT INTO `ct_trip` (`id`, `trip_name`, `trip_start_date`, `trip_end_date`, `trip_from`, `trip_to`, `trip_budget`, `trip_budget_currency`, `img_path`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 'Test Trip', '2025-06-01', '2025-06-30', 'Tokyo', 'Osaka', 10000, 'JPY', NULL, '1', '2025-06-03 12:07:21', NULL, NULL, 'Test Remark');
COMMIT;

-- ----------------------------
-- Table structure for ct_trip_action
-- ----------------------------
DROP TABLE IF EXISTS `ct_trip_action`;
CREATE TABLE `ct_trip_action` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `trip_id` bigint DEFAULT NULL COMMENT 'trip id',
  `action_id` bigint DEFAULT NULL COMMENT 'action id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='trip-action relation table';

-- ----------------------------
-- Records of ct_trip_action
-- ----------------------------
BEGIN;
INSERT INTO `ct_trip_action` (`id`, `trip_id`, `action_id`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, 1, '1', '2025-06-03 12:20:46', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_user
-- ----------------------------
DROP TABLE IF EXISTS `ct_user`;
CREATE TABLE `ct_user` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'user id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'user name',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'password',
  `salt` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'salt',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'email',
  `phone_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'telephone number',
  `enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'enabled',
  `account_non_expired` tinyint(1) DEFAULT '1' COMMENT 'expired',
  `credentials_non_expired` tinyint(1) DEFAULT '1' COMMENT 'credentials expired',
  `account_non_locked` tinyint(1) DEFAULT '1' COMMENT 'account locked',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint unsigned DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`user_name`),
  KEY `idx_username` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='user table';

-- ----------------------------
-- Records of ct_user
-- ----------------------------
BEGIN;
INSERT INTO `ct_user` (`id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `enabled`, `account_non_expired`, `credentials_non_expired`, `account_non_locked`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (1, 'superAdmin', '652174d24e9b4279c9400e3add03c0c8899b596bc5b963a58c82e441d501a270', 'a806b390-e09d-450e-a9fa-9369275ac26a', 'superAdmin@cmtrip.com', NULL, 1, 0, 0, 0, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
INSERT INTO `ct_user` (`id`, `user_name`, `password`, `salt`, `email`, `phone_number`, `enabled`, `account_non_expired`, `credentials_non_expired`, `account_non_locked`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (2, 'bizAccount', '3402fd04d4000bb09b1867e612ceb4240e9a20758a8490ff531fbf6b9d6afa68', 'a43df9ad-cc8a-4449-8c57-738215c0491b', 'bizaccount@cmtrip.com', NULL, 1, 0, 0, 0, '2025-05-21 10:36:21', NULL, '2025-05-21 10:36:21', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_user_pass_entity
-- ----------------------------
DROP TABLE IF EXISTS `ct_user_pass_entity`;
CREATE TABLE `ct_user_pass_entity` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint DEFAULT NULL,
  `pass_entity_id` bigint DEFAULT NULL,
  `create_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Records of ct_user_pass_entity
-- ----------------------------
BEGIN;
INSERT INTO `ct_user_pass_entity` (`id`, `user_id`, `pass_entity_id`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, 1, '1', NULL, NULL, NULL, NULL);
INSERT INTO `ct_user_pass_entity` (`id`, `user_id`, `pass_entity_id`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2, 1, 2, '1', '2025-06-08 14:40:31', NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_user_role
-- ----------------------------
DROP TABLE IF EXISTS `ct_user_role`;
CREATE TABLE `ct_user_role` (
  `id` bigint unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint unsigned NOT NULL COMMENT 'user id',
  `role_id` bigint unsigned NOT NULL COMMENT 'role id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `create_by` bigint unsigned DEFAULT NULL,
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `update_by` bigint unsigned DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `fk_user_role_role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='user-role relation table';

-- ----------------------------
-- Records of ct_user_role
-- ----------------------------
BEGIN;
INSERT INTO `ct_user_role` (`id`, `user_id`, `role_id`, `create_time`, `create_by`, `update_time`, `update_by`, `remark`) VALUES (1, 1, 1, '2025-05-20 10:00:00', NULL, '2025-05-20 10:00:00', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for ct_user_trip
-- ----------------------------
DROP TABLE IF EXISTS `ct_user_trip`;
CREATE TABLE `ct_user_trip` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` bigint NOT NULL COMMENT 'user id',
  `trip_id` bigint NOT NULL COMMENT 'trip id',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='user-trip relation table';

-- ----------------------------
-- Records of ct_user_trip
-- ----------------------------
BEGIN;
INSERT INTO `ct_user_trip` (`id`, `user_id`, `trip_id`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (1, 1, 1, '1', '2025-06-03 12:07:21', NULL, NULL, NULL);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
