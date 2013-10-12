/*
MySQL Data Transfer
Source Host: localhost
Source Database: whois
Target Host: localhost
Target Database: whois
Date: 2013-10-12 15:46:36
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for autnum
-- ----------------------------
CREATE TABLE `autnum` (
  `Handle` varchar(100) NOT NULL default ' ',
  `Start_Autnum` int(11) default NULL,
  `End_Autnum` int(11) default NULL,
  `Name` varchar(100) default NULL,
  `Type` varchar(100) default NULL,
  `Status` varchar(50) default NULL,
  `Country` varchar(3) default NULL,
  `Lang` varchar(10) default ' ',
  `$x$xnic_auttest` varchar(100) default '',
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for autnum_redirect
-- ----------------------------
CREATE TABLE `autnum_redirect` (
  `startNumber` int(11) NOT NULL,
  `endNumber` int(11) NOT NULL,
  `id` int(11) NOT NULL auto_increment,
  `redirectURL` varchar(100) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dnrdomain
-- ----------------------------
CREATE TABLE `dnrdomain` (
  `Handle` varchar(100) NOT NULL,
  `Ldh_Name` varchar(100) default ' ',
  `Status` varchar(100) default ' ',
  `Port43` varchar(40) default ' ',
  `Lang` varchar(10) default NULL,
  `Unicode_Name` varchar(100) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dnrentity
-- ----------------------------
CREATE TABLE `dnrentity` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) default NULL,
  `Status` varchar(100) default NULL,
  `Emails` varchar(100) default NULL,
  `Port43` varchar(40) default NULL,
  `Roles` varchar(50) default NULL,
  `Lang` varchar(10) default NULL,
  `Bday` varchar(100) default NULL,
  `Anniversary` varchar(100) default NULL,
  `Gender` varchar(100) default NULL,
  `Kind` varchar(100) default NULL,
  `Language_Tag_1` varchar(100) default NULL,
  `Pref1` varchar(10) default NULL,
  `Language_Tag_2` varchar(100) default NULL,
  `Pref2` varchar(10) default NULL,
  `Org` varchar(100) default NULL,
  `Title` varchar(100) default NULL,
  `Role` varchar(100) default NULL,
  `Geo` varchar(100) default NULL,
  `Key` varchar(256) default NULL,
  `Tz` varchar(100) default NULL,
  `Url` varchar(256) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for domain_redirect
-- ----------------------------
CREATE TABLE `domain_redirect` (
  `redirectType` varchar(100) default NULL,
  `redirectURL` varchar(100) default NULL,
  `id` int(11) NOT NULL auto_increment,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dsdata
-- ----------------------------
CREATE TABLE `dsdata` (
  `Key_Tag` int(11) default NULL,
  `Algorithm` int(11) default NULL,
  `Digest` varchar(512) default NULL,
  `Digest_Type` int(11) default NULL,
  `DsDataID` varchar(100) NOT NULL,
  PRIMARY KEY  (`DsDataID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for errormessage
-- ----------------------------
CREATE TABLE `errormessage` (
  `Error_Code` varchar(100) default NULL,
  `Title` varchar(256) default NULL,
  `Description` varchar(256) default NULL,
  `Lang` varchar(256) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for events
-- ----------------------------
CREATE TABLE `events` (
  `Event_Action` varchar(100) default NULL,
  `Event_Actor` varchar(100) default NULL,
  `Event_Date` varchar(100) default NULL,
  `eventsId` varchar(50) NOT NULL,
  PRIMARY KEY  (`eventsId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ip
-- ----------------------------
CREATE TABLE `ip` (
  `Handle` varchar(100) NOT NULL,
  `StartLowAddress` bigint(40) default NULL,
  `IP_Version` varchar(2) default NULL,
  `Name` varchar(100) default NULL,
  `Type` varchar(100) default NULL,
  `Country` varchar(10) default NULL,
  `Parent_Handle` varchar(100) default NULL,
  `Status` varchar(100) default NULL,
  `Lang` varchar(10) default NULL,
  `StartHighAddress` bigint(20) default NULL,
  `EndLowAddress` bigint(20) default NULL,
  `EndHighAddress` bigint(20) default NULL,
  `$x$testip` varchar(50) default '',
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for ip_redirect
-- ----------------------------
CREATE TABLE `ip_redirect` (
  `id` int(11) NOT NULL auto_increment,
  `StartHighAddress` bigint(100) default NULL,
  `redirectURL` varchar(100) default NULL,
  `StartLowAddress` bigint(100) default NULL,
  `EndHighAddress` bigint(100) default NULL,
  `EndLowAddress` bigint(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for keydata
-- ----------------------------
CREATE TABLE `keydata` (
  `Flags` varchar(100) default NULL,
  `Protocol` varchar(100) default NULL,
  `Public_Key` varchar(512) default NULL,
  `Algorithm` int(11) default NULL,
  `KeyDataID` varchar(100) NOT NULL,
  PRIMARY KEY  (`KeyDataID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for link
-- ----------------------------
CREATE TABLE `link` (
  `Value` varchar(150) default NULL,
  `Rel` varchar(10) default NULL,
  `Href` varchar(100) default NULL,
  `linkId` varchar(100) NOT NULL default '',
  `hreflang` varchar(50) default NULL,
  `title` varchar(100) default NULL,
  `media` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  PRIMARY KEY  (`linkId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_dnrentity
-- ----------------------------
CREATE TABLE `m2m_dnrentity` (
  `Handle` varchar(100) NOT NULL,
  `dnrentityHandle` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_dsdata
-- ----------------------------
CREATE TABLE `m2m_dsdata` (
  `Handle` varchar(100) default NULL,
  `DsDataID` varchar(100) default NULL,
  `type` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for m2m_events
-- ----------------------------
CREATE TABLE `m2m_events` (
  `Handle` varchar(100) NOT NULL,
  `eventsId` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_keydata
-- ----------------------------
CREATE TABLE `m2m_keydata` (
  `Handle` varchar(100) NOT NULL,
  `KeyDataID` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for m2m_link
-- ----------------------------
CREATE TABLE `m2m_link` (
  `linkId` varchar(100) NOT NULL,
  `Handle` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_nameserver
-- ----------------------------
CREATE TABLE `m2m_nameserver` (
  `Handle` varchar(100) NOT NULL,
  `nameserverHandle` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_notices
-- ----------------------------
CREATE TABLE `m2m_notices` (
  `Handle` varchar(100) default NULL,
  `noticesId` varchar(100) default NULL,
  `type` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_phones
-- ----------------------------
CREATE TABLE `m2m_phones` (
  `Handle` varchar(100) NOT NULL,
  `phoneId` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_postaladress
-- ----------------------------
CREATE TABLE `m2m_postaladress` (
  `Handle` varchar(100) NOT NULL,
  `postalAddressId` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_publicids
-- ----------------------------
CREATE TABLE `m2m_publicids` (
  `Handle` varchar(100) default NULL,
  `identifier` varchar(100) default NULL,
  `type` varchar(100) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for m2m_registrar
-- ----------------------------
CREATE TABLE `m2m_registrar` (
  `Handle` varchar(100) NOT NULL,
  `registrarHandle` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_remarks
-- ----------------------------
CREATE TABLE `m2m_remarks` (
  `Handle` varchar(100) NOT NULL,
  `remarksId` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_rirentity
-- ----------------------------
CREATE TABLE `m2m_rirentity` (
  `Handle` varchar(100) NOT NULL,
  `rirentityHandle` varchar(100) NOT NULL,
  `type` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for m2m_securedns
-- ----------------------------
CREATE TABLE `m2m_securedns` (
  `Handle` varchar(100) NOT NULL,
  `SecureDNSID` varchar(100) NOT NULL,
  `type` varchar(20) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for m2m_variants
-- ----------------------------
CREATE TABLE `m2m_variants` (
  `Handle` varchar(100) NOT NULL,
  `variantsId` varchar(100) NOT NULL,
  `type` varchar(100) default NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for nameserver
-- ----------------------------
CREATE TABLE `nameserver` (
  `Handle` varchar(100) NOT NULL,
  `Ldh_Name` varchar(100) default NULL,
  `IPV4_Addresses` varchar(100) default NULL,
  `Status` varchar(100) default NULL,
  `Port43` varchar(40) default NULL,
  `Lang` varchar(10) default NULL,
  `Unicode_Name` varchar(100) default NULL,
  `IPV6_Addresses` varchar(100) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for notices
-- ----------------------------
CREATE TABLE `notices` (
  `Title` varchar(100) default NULL,
  `Description` varchar(255) default NULL,
  `noticesId` varchar(100) NOT NULL,
  PRIMARY KEY  (`noticesId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for permissions
-- ----------------------------
CREATE TABLE `permissions` (
  `tableName` varchar(20) NOT NULL default '',
  `columnName` varchar(50) NOT NULL default '',
  `anonymous` varchar(10) default '1',
  `authenticated` varchar(10) default '1',
  `root` varchar(10) default '1',
  `columnLength` varchar(10) default '255',
  PRIMARY KEY  (`tableName`,`columnName`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for phones
-- ----------------------------
CREATE TABLE `phones` (
  `Office` varchar(100) default NULL,
  `Fax` varchar(100) default NULL,
  `Mobile` varchar(100) default NULL,
  `phonesId` varchar(100) NOT NULL,
  PRIMARY KEY  (`phonesId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for postaladdress
-- ----------------------------
CREATE TABLE `postaladdress` (
  `Street` varchar(100) default NULL,
  `Street1` varchar(100) default NULL,
  `Street2` varchar(100) default NULL,
  `City` varchar(50) default NULL,
  `SP` varchar(20) default NULL,
  `Postal_Code` varchar(10) default NULL,
  `postalAddressId` varchar(100) NOT NULL,
  `Country` varchar(4) default NULL,
  PRIMARY KEY  (`postalAddressId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for publicids
-- ----------------------------
CREATE TABLE `publicids` (
  `identifier` varchar(256) NOT NULL,
  `type` varchar(256) default NULL,
  PRIMARY KEY  (`identifier`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for registrar
-- ----------------------------
CREATE TABLE `registrar` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) default NULL,
  `Status` varchar(100) default NULL,
  `Emails` varchar(100) default NULL,
  `Port43` varchar(40) default NULL,
  `Roles` varchar(50) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for remarks
-- ----------------------------
CREATE TABLE `remarks` (
  `Title` varchar(100) default NULL,
  `Description` varchar(100) default NULL,
  `remarksId` varchar(100) NOT NULL,
  PRIMARY KEY  (`remarksId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rirdomain
-- ----------------------------
CREATE TABLE `rirdomain` (
  `Handle` varchar(100) NOT NULL,
  `Ldh_Name` varchar(25) default NULL,
  `Lang` varchar(10) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for rirentity
-- ----------------------------
CREATE TABLE `rirentity` (
  `Handle` varchar(100) NOT NULL,
  `Entity_Names` varchar(100) default NULL,
  `Roles` varchar(100) default NULL,
  `Emails` varchar(100) default NULL,
  `Lang` varchar(10) default NULL,
  `Bday` varchar(100) default NULL,
  `Anniversary` varchar(100) default NULL,
  `Gender` varchar(100) default NULL,
  `Kind` varchar(100) default NULL,
  `Language_Tag_1` varchar(100) default NULL,
  `Pref1` varchar(10) default NULL,
  `Language_Tag_2` varchar(100) default NULL,
  `Pref2` varchar(10) default NULL,
  `Org` varchar(100) default NULL,
  `Title` varchar(100) default NULL,
  `Role` varchar(100) default NULL,
  `Geo` varchar(100) default NULL,
  `Key` varchar(256) default NULL,
  `Tz` varchar(100) default NULL,
  `Url` varchar(256) default NULL,
  PRIMARY KEY  (`Handle`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for securedns
-- ----------------------------
CREATE TABLE `securedns` (
  `Zone_Signed` tinyint(4) default NULL,
  `Delegation_Signed` tinyint(4) default NULL,
  `Max_SigLife` int(11) default NULL,
  `secureDNSID` varchar(100) NOT NULL default '',
  PRIMARY KEY  (`secureDNSID`)
) ENGINE=InnoDB DEFAULT CHARSET=gbk;

-- ----------------------------
-- Table structure for variants
-- ----------------------------
CREATE TABLE `variants` (
  `Relation` varchar(100) default NULL,
  `Variant_Names` varchar(100) default NULL,
  `variantsId` varchar(100) NOT NULL,
  `IDNTable` varchar(100) default NULL,
  PRIMARY KEY  (`variantsId`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
