/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50529
 Source Host           : localhost
 Source Database       : whois

 Target Server Type    : MySQL
 Target Server Version : 50529
 File Encoding         : utf-8

 Date: 05/14/2013 16:35:25 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `autnum`
-- ----------------------------
DROP TABLE IF EXISTS `autnum`;
CREATE TABLE `autnum` (
  `Handle` varchar(100) NOT NULL DEFAULT ' ',
  `Start_Autnum` int(11) DEFAULT NULL,
  `End_Autnum` int(11) DEFAULT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Description` varchar(500) DEFAULT NULL,
  `Type` varchar(100) DEFAULT NULL,
  `Country` varchar(3) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT ' ',
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `autnum_redirect`
-- ----------------------------
DROP TABLE IF EXISTS `autnum_redirect`;
CREATE TABLE `autnum_redirect` (
  `startNumber` int(11) NOT NULL,
  `endNumber` int(11) NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `redirectURL` varchar(100) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `delegationkeys`
-- ----------------------------
DROP TABLE IF EXISTS `delegationkeys`;
CREATE TABLE `delegationkeys` (
  `Algorithm` varchar(10) DEFAULT NULL,
  `Digest` varchar(50) DEFAULT NULL,
  `Disgest_Type` varchar(10) DEFAULT NULL,
  `key_Tag` varchar(10) DEFAULT NULL,
  `delegationKeysId` varchar(100) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `dnrdomain`
-- ----------------------------
DROP TABLE IF EXISTS `dnrdomain`;
CREATE TABLE `dnrdomain` (
  `Handle` varchar(100) NOT NULL,
  `LdhName` varchar(100) DEFAULT ' ',
  `Status` varchar(100) DEFAULT ' ',
  `Port43` varchar(40) DEFAULT ' ',
  `Lang` varchar(10) DEFAULT NULL,
  `UnicodeName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `dnrentity`
-- ----------------------------
DROP TABLE IF EXISTS `dnrentity`;
CREATE TABLE `dnrentity` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) DEFAULT NULL,
  `Status` varchar(100) DEFAULT NULL,
  `Emails` varchar(100) DEFAULT NULL,
  `Port43` varchar(40) DEFAULT NULL,
  `Roles` varchar(50) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `domain_redirect`
-- ----------------------------
DROP TABLE IF EXISTS `domain_redirect`;
CREATE TABLE `domain_redirect` (
  `redirectType` varchar(100) DEFAULT NULL,
  `redirectURL` varchar(100) DEFAULT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `events`
-- ----------------------------
DROP TABLE IF EXISTS `events`;
CREATE TABLE `events` (
  `EventAction` varchar(100) DEFAULT NULL,
  `EventActor` varchar(100) DEFAULT NULL,
  `EventDate` varchar(100) DEFAULT NULL,
  `eventsId` varchar(50) NOT NULL,
  PRIMARY KEY (`eventsId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ip`
-- ----------------------------
DROP TABLE IF EXISTS `ip`;
CREATE TABLE `ip` (
  `Handle` varchar(100) NOT NULL,
  `StartLowAddress` bigint(40) DEFAULT NULL,
  `IP_Version` varchar(2) DEFAULT NULL,
  `Name` varchar(100) DEFAULT NULL,
  `Description` varchar(500) DEFAULT NULL,
  `Type` varchar(100) DEFAULT NULL,
  `Country` varchar(10) DEFAULT NULL,
  `Parent_Handle` varchar(100) DEFAULT NULL,
  `Status` varchar(100) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  `StartHighAddress` bigint(20) DEFAULT NULL,
  `EndLowAddress` bigint(20) DEFAULT NULL,
  `EndHighAddress` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `ip_redirect`
-- ----------------------------
DROP TABLE IF EXISTS `ip_redirect`;
CREATE TABLE `ip_redirect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `StartHighAddress` bigint(100) DEFAULT NULL,
  `redirectURL` varchar(100) DEFAULT NULL,
  `StartLowAddress` bigint(100) DEFAULT NULL,
  `EndHighAddress` bigint(100) DEFAULT NULL,
  `EndLowAddress` bigint(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `link`
-- ----------------------------
DROP TABLE IF EXISTS `link`;
CREATE TABLE `link` (
  `Value` varchar(150) DEFAULT NULL,
  `Rel` varchar(10) DEFAULT NULL,
  `Href` varchar(100) DEFAULT NULL,
  `linkId` varchar(100) NOT NULL DEFAULT '',
  `hreflang` varchar(50) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `media` varchar(50) DEFAULT NULL,
  `type` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`linkId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_delegationKeys`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_delegationKeys`;
CREATE TABLE `m2m_delegationKeys` (
  `Handle` varchar(100) NOT NULL,
  `delegationKeyId` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_dnrentity`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_dnrentity`;
CREATE TABLE `m2m_dnrentity` (
  `Handle` varchar(100) NOT NULL,
  `dnrentityHandle` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_events`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_events`;
CREATE TABLE `m2m_events` (
  `Handle` varchar(100) NOT NULL,
  `eventsId` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_link`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_link`;
CREATE TABLE `m2m_link` (
  `linkId` varchar(100) NOT NULL,
  `Handle` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_nameserver`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_nameserver`;
CREATE TABLE `m2m_nameserver` (
  `Handle` varchar(100) NOT NULL,
  `nameserverHandle` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_notices`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_notices`;
CREATE TABLE `m2m_notices` (
  `Handle` varchar(100) DEFAULT NULL,
  `noticesId` varchar(100) DEFAULT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_phones`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_phones`;
CREATE TABLE `m2m_phones` (
  `Handle` varchar(100) NOT NULL,
  `phoneId` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_postaladress`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_postaladress`;
CREATE TABLE `m2m_postaladress` (
  `Handle` varchar(100) NOT NULL,
  `postalAddressId` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_registrar`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_registrar`;
CREATE TABLE `m2m_registrar` (
  `Handle` varchar(100) NOT NULL,
  `registrarHandle` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_remarks`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_remarks`;
CREATE TABLE `m2m_remarks` (
  `Handle` varchar(100) NOT NULL,
  `remarksId` varchar(100) NOT NULL,
  `type` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_rirentity`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_rirentity`;
CREATE TABLE `m2m_rirentity` (
  `Handle` varchar(100) NOT NULL,
  `rirentityHandle` varchar(100) NOT NULL,
  `type` varchar(100) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `m2m_variants`
-- ----------------------------
DROP TABLE IF EXISTS `m2m_variants`;
CREATE TABLE `m2m_variants` (
  `Handle` int(100) NOT NULL,
  `variantsId` varchar(100) NOT NULL,
  `type` varchar(100) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `nameserver`
-- ----------------------------
DROP TABLE IF EXISTS `nameserver`;
CREATE TABLE `nameserver` (
  `Handle` varchar(100) NOT NULL,
  `LdhName` varchar(100) DEFAULT NULL,
  `IPV4_Addresses` varchar(100) DEFAULT NULL,
  `Status` varchar(100) DEFAULT NULL,
  `Port43` varchar(40) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  `UnicodeName` varchar(100) DEFAULT NULL,
  `IPV6_Addresses` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `notices`
-- ----------------------------
DROP TABLE IF EXISTS `notices`;
CREATE TABLE `notices` (
  `Title` varchar(100) DEFAULT NULL,
  `Description` varchar(255) DEFAULT NULL,
  `noticesId` varchar(100) NOT NULL,
  PRIMARY KEY (`noticesId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `permissions`
-- ----------------------------
DROP TABLE IF EXISTS `permissions`;
CREATE TABLE `permissions` (
  `tableName` varchar(20) NOT NULL DEFAULT '',
  `columnName` varchar(50) NOT NULL DEFAULT '',
  `anonymous` varchar(10) DEFAULT '1',
  `authenticated` varchar(10) DEFAULT '1',
  `root` varchar(10) DEFAULT '1',
  `columnLength` varchar(10) DEFAULT '255',
  PRIMARY KEY (`tableName`,`columnName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `phones`
-- ----------------------------
DROP TABLE IF EXISTS `phones`;
CREATE TABLE `phones` (
  `Office` varchar(100) DEFAULT NULL,
  `Fax` varchar(100) DEFAULT NULL,
  `Mobile` varchar(100) DEFAULT NULL,
  `phonesId` varchar(100) NOT NULL,
  PRIMARY KEY (`phonesId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `postaladdress`
-- ----------------------------
DROP TABLE IF EXISTS `postaladdress`;
CREATE TABLE `postaladdress` (
  `Street` varchar(100) DEFAULT NULL,
  `Street1` varchar(100) DEFAULT NULL,
  `Street2` varchar(100) DEFAULT NULL,
  `City` varchar(50) DEFAULT NULL,
  `SP` varchar(20) DEFAULT NULL,
  `Postal_Code` varchar(10) DEFAULT NULL,
  `postalAddressId` varchar(100) NOT NULL,
  `Country_Code` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`postalAddressId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `registrar`
-- ----------------------------
DROP TABLE IF EXISTS `registrar`;
CREATE TABLE `registrar` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) DEFAULT NULL,
  `Status` varchar(100) DEFAULT NULL,
  `Emails` varchar(100) DEFAULT NULL,
  `Port43` varchar(40) DEFAULT NULL,
  `Roles` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `remarks`
-- ----------------------------
DROP TABLE IF EXISTS `remarks`;
CREATE TABLE `remarks` (
  `Title` varchar(100) DEFAULT NULL,
  `Description` varchar(100) DEFAULT NULL,
  `remarksId` varchar(100) NOT NULL,
  PRIMARY KEY (`remarksId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `rirdomain`
-- ----------------------------
DROP TABLE IF EXISTS `rirdomain`;
CREATE TABLE `rirdomain` (
  `Handle` varchar(100) NOT NULL,
  `LdhName` varchar(25) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `rirentity`
-- ----------------------------
DROP TABLE IF EXISTS `rirentity`;
CREATE TABLE `rirentity` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) DEFAULT NULL,
  `Roles` varchar(100) DEFAULT NULL,
  `Emails` varchar(100) DEFAULT NULL,
  `Lang` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `variants`
-- ----------------------------
DROP TABLE IF EXISTS `variants`;
CREATE TABLE `variants` (
  `Relation` varchar(100) DEFAULT NULL,
  `Variant_Names` varchar(100) DEFAULT NULL,
  `variantsId` varchar(100) NOT NULL,
  PRIMARY KEY (`variantsId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
