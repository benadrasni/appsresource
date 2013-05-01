CREATE SCHEMA IF NOT EXISTS appsresource
DEFAULT CHARACTER SET = 'utf8';

USE appsresource;

CREATE USER 'appsres' IDENTIFIED BY 'appsres';

GRANT SELECT ON appsresource.* TO 'appsres';
GRANT INSERT ON appsresource.* TO 'appsres';
GRANT UPDATE ON appsresource.* TO 'appsres';
GRANT DELETE ON appsresource.* TO 'appsres';
GRANT EXECUTE ON PROCEDURE appsresource.clean_multivalues TO 'appsres';
GRANT EXECUTE ON PROCEDURE appsresource.share_values TO 'appsres';
GRANT EXECUTE ON PROCEDURE appsresource.log_delete TO 'appsres';
GRANT EXECUTE ON PROCEDURE appsresource.get_object_attributes TO 'appsres';

/*
Table structure for users
*/

CREATE TABLE `appsresource`.`users` (
  `user_id` INT(11) NOT NULL auto_increment,
  `user_email` VARCHAR(100) NOT NULL,
  `user_flags` INT NOT NULL default 0,
  `user_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`user_id`),
  KEY `i_user_email` (`user_email`)
);

/*
Table structure for languages
*/

CREATE TABLE `appsresource`.`languages` (
  `lang_id` SMALLINT NOT NULL,
  `lang_code` VARCHAR(2) NOT NULL,
  `lang_name` VARCHAR(50) NOT NULL,
  PRIMARY KEY  (`lang_id`)
);

/*
Table structure for locales
*/

CREATE TABLE `appsresource`.`locales` (
  `loc_id` SMALLINT NOT NULL,
  `loc_code` VARCHAR(5) NOT NULL,
  `loc_name` VARCHAR(50) NOT NULL,
  `loc_lang_id` SMALLINT NOT NULL,
  PRIMARY KEY  (`loc_id`),
  FOREIGN KEY (loc_lang_id) REFERENCES languages(lang_id)
);

/*
Table structure for object types
*/

CREATE TABLE `appsresource`.`object_types` (
  `ot_id` INT(11) NOT NULL auto_increment,
  `ot_ot_id` INT(11) default NULL,
  `ot_code` VARCHAR(10) NOT NULL,
  `ot_name` VARCHAR(255) NOT NULL,
  `ot_desc` VARCHAR(255) default NULL,
  `ot_user_id` INT(11) NOT NULL,
  `ot_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ot_id`),
  KEY `I_OT_CODE` (`ot_code`),
  FOREIGN KEY (ot_ot_id) REFERENCES object_types(ot_id),
  FOREIGN KEY (ot_user_id) REFERENCES users(user_id)
);

/*
Table structure for value types
*/

CREATE TABLE `appsresource`.`value_types` (
  `vt_id` INT(11) NOT NULL auto_increment,
  `vt_code` VARCHAR(10) NOT NULL,
  `vt_name` VARCHAR(255) NOT NULL,
  `vt_desc` VARCHAR(255) default NULL,
  `vt_type` VARCHAR(50) NOT NULL,
  `vt_user_id` INT(11) NOT NULL,
  `vt_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`vt_id`),
  KEY `I_VT_CODE` (`vt_code`),
  FOREIGN KEY (vt_user_id) REFERENCES users(user_id)
);


/*
Table structure for units
*/

CREATE TABLE `appsresource`.`units` (
  `unit_id` INT(11) NOT NULL auto_increment,
  `unit_code` VARCHAR(10) NOT NULL,
  `unit_name` VARCHAR(255) NOT NULL,
  `unit_desc` VARCHAR(255) default NULL,
  `unit_symbol` VARCHAR(20) NOT NULL,
  `unit_category` INT NOT NULL,
  `unit_conversion` FLOAT NOT NULL default 1,
  `unit_user_id` INT(11) NOT NULL,
  `unit_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`unit_id`),
  KEY `I_UNIT_CODE` (`unit_code`),
  FOREIGN KEY (unit_user_id) REFERENCES users(user_id)
);

/*
Table structure for object filterAttributes
*/

CREATE TABLE `appsresource`.`object_attributes` (
  `oa_id` INT(11) NOT NULL auto_increment,
  `oa_vt_id` INT(11) NOT NULL,
  `oa_ot_id` INT(11) NOT NULL,
  `oa_code` VARCHAR(10) NOT NULL,
  `oa_name` VARCHAR(255) NOT NULL,
  `oa_desc` VARCHAR(255) default NULL,
  `oa_unit_id` INT(11) default NULL,
  `oa_shared1` INT default NULL,
  `oa_shared2` INT default NULL,
  `oa_shared3` INT default NULL,
  `oa_shared4` INT default NULL,
  `oa_shared5` INT default NULL,
  `oa_user_id` INT(11) NOT NULL,
  `oa_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`oa_id`),
  KEY `I_OA_CODE` (`oa_code`),
  FOREIGN KEY (oa_user_id) REFERENCES users(user_id),
  FOREIGN KEY (oa_vt_id) REFERENCES value_types(vt_id),
  FOREIGN KEY (oa_ot_id) REFERENCES object_types(ot_id),
  FOREIGN KEY (oa_unit_id) REFERENCES units(unit_id)
);

/*
Table structure for object relations
*/

CREATE TABLE `appsresource`.`object_relations` (
  `or_id` INT(11) NOT NULL auto_increment,
  `or_ot1_id` INT(11) NOT NULL,
  `or_ot2_id` INT(11) NOT NULL,
  `or_code` VARCHAR(10) NOT NULL,
  `or_name` VARCHAR(255) NOT NULL,
  `or_desc` VARCHAR(255) default NULL,
  `or_type` INT(11) NOT NULL,
  `or_or_id` INT(11) default NULL,
  `or_user_id` INT(11) NOT NULL,
  `or_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`or_id`),
  KEY `I_OR_CODE` (`or_code`),
  FOREIGN KEY (or_user_id) REFERENCES users(user_id),
  FOREIGN KEY (or_ot1_id) REFERENCES object_types(ot_id),
  FOREIGN KEY (or_ot2_id) REFERENCES object_types(ot_id),
  FOREIGN KEY (or_or_id) REFERENCES object_relations(or_id)
);

/*
Table structure for templates
*/

CREATE TABLE `appsresource`.`templates` (
  `t_id` INT(11) NOT NULL auto_increment,
  `t_ot_id` INT(11) default NULL,
  `t_code` VARCHAR(10) NOT NULL,
  `t_name` VARCHAR(255) NOT NULL,
  `t_desc` VARCHAR(255) default NULL,
  `t_oa_id` INT(11) default NULL,
  `t_user_id` INT(11) NOT NULL,
  `t_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`t_id`),
  KEY `I_T_CODE` (`t_code`),
  FOREIGN KEY (t_user_id) REFERENCES users(user_id),
  FOREIGN KEY (t_oa_id) REFERENCES object_attributes(oa_id),
  FOREIGN KEY (t_ot_id) REFERENCES object_types(ot_id)
);

/*
Table structure for template relations
*/

CREATE TABLE `appsresource`.`template_relations` (
  `tr_id` INT(11) NOT NULL auto_increment,
  `tr_t1_id` INT(11) NOT NULL,
  `tr_t2_id` INT(11) NOT NULL,
  `tr_or_id` INT(11) NOT NULL,
  `tr_code` VARCHAR(10) NOT NULL,
  `tr_name` VARCHAR(255) NOT NULL,
  `tr_desc` VARCHAR(255) default NULL,
  `tr_rank` INT NOT NULL,
  `tr_subrank` INT NOT NULL,
  `tr_flags` INT NOT NULL,
  `tr_user_id` INT(11) NOT NULL,
  `tr_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tr_id`),
  KEY `I_TR_CODE` (`tr_code`),
  FOREIGN KEY (tr_user_id) REFERENCES users(user_id),
  FOREIGN KEY (tr_or_id) REFERENCES object_relations(or_id),
  FOREIGN KEY (tr_t1_id) REFERENCES templates(t_id),
  FOREIGN KEY (tr_t2_id) REFERENCES templates(t_id)
);

/*
Table structure for template groups
*/

CREATE TABLE `appsresource`.`template_groups` (
  `tg_id` INT(11) NOT NULL auto_increment,
  `tg_t_id` INT(11) NOT NULL,
  `tg_code` VARCHAR(10) NOT NULL,
  `tg_name` VARCHAR(255) NOT NULL,
  `tg_desc` VARCHAR(255) default NULL,
  `tg_rank` INT NOT NULL,
  `tg_subrank` INT NOT NULL,
  `tg_flags` INT NOT NULL,
  `tg_label_top` INT default NULL,
  `tg_label_left` INT default NULL,
  `tg_label_width` INT default NULL,
  `tg_label_align` VARCHAR(10) default NULL,
  `tg_label_width_unit` VARCHAR(2) default NULL,
  `tg_user_id` INT(11) NOT NULL,
  `tg_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tg_id`),
  KEY `I_TG_CODE` (`tg_code`),
  FOREIGN KEY (tg_user_id) REFERENCES users(user_id),
  FOREIGN KEY (tg_t_id) REFERENCES templates(t_id)
);

/*
Table structure for template filterAttributes
*/

CREATE TABLE `appsresource`.`template_attributes` (
  `ta_id` INT(11) NOT NULL auto_increment,
  `ta_tg_id` INT(11) NOT NULL,
  `ta_oa_id` INT(11) default NULL,
  `ta_code` VARCHAR(10) NOT NULL,
  `ta_name` VARCHAR(255) NOT NULL,
  `ta_desc` VARCHAR(255) default NULL,
  `ta_flags` INT NOT NULL,
  `ta_style` INT NOT NULL,
  `ta_tabindex` INT default 0,
  `ta_length` INT NOT NULL,
  `ta_default` VARCHAR(255),
  `ta_label_top` INT default NULL,
  `ta_label_left` INT default NULL,
  `ta_label_width` INT default NULL,
  `ta_label_align` VARCHAR(10) default NULL,
  `ta_label_width_unit` VARCHAR(2) default NULL,
  `ta_top` INT default NULL,
  `ta_left` INT default NULL,
  `ta_width` INT default NULL,
  `ta_width_unit` VARCHAR(2) default NULL,
  `ta_align` VARCHAR(10) default NULL,
  `ta_unit` VARCHAR(2) default NULL,
  `ta_unit_top` INT default NULL,
  `ta_unit_left` INT default NULL,
  `ta_unit_width` INT default NULL,
  `ta_unit_align` VARCHAR(10) default NULL,
  `ta_unit_width_unit` VARCHAR(2) default NULL,
  `ta_shared1` INT default NULL,
  `ta_shared2` INT default NULL,
  `ta_shared3` INT default NULL,
  `ta_shared4` INT default NULL,
  `ta_shared5` INT default NULL,
  `ta_user_id` INT(11) NOT NULL,
  `ta_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ta_id`),
  KEY `I_TA_CODE` (`ta_code`),
  FOREIGN KEY (ta_user_id) REFERENCES users(user_id),
  FOREIGN KEY (ta_tg_id) REFERENCES template_groups(tg_id),
  FOREIGN KEY (ta_oa_id) REFERENCES object_attributes(oa_id)
);

/*
Table structure for template trees
*/

CREATE TABLE `appsresource`.`template_trees` (
  `tt_id` INT(11) NOT NULL auto_increment,
  `tt_t_id` INT(11) NOT NULL,
  `tt_code` VARCHAR(10) NOT NULL,
  `tt_name` VARCHAR(255) NOT NULL,
  `tt_desc` VARCHAR(255) default NULL,
  `tt_flags` INT NOT NULL,
  `tt_rank` INT NOT NULL,
  `tt_user_id` INT(11) NOT NULL,
  `tt_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tt_id`),
  KEY `I_TT_CODE` (`tt_code`),
  FOREIGN KEY (tt_user_id) REFERENCES users(user_id),
  FOREIGN KEY (tt_t_id) REFERENCES templates(t_id)
);

/*
Table structure for template tree items
*/

CREATE TABLE `appsresource`.`template_tree_items` (
  `tti_id` INT(11) NOT NULL auto_increment,
  `tti_tt_id` INT(11) NOT NULL,
  `tti_ta_id` INT(11) NOT NULL,
  `tti_rank` INT NOT NULL,
  PRIMARY KEY  (`tti_id`),
  FOREIGN KEY (tti_ta_id) REFERENCES template_attributes(ta_id),
  FOREIGN KEY (tti_tt_id) REFERENCES template_trees(tt_id)
);

/*
Table structure for template lists
*/

CREATE TABLE `appsresource`.`template_lists` (
  `tl_id` INT(11) NOT NULL auto_increment,
  `tl_t_id` INT(11) NOT NULL,
  `tl_code` VARCHAR(10) NOT NULL,
  `tl_name` VARCHAR(255) NOT NULL,
  `tl_desc` VARCHAR(255) default NULL,
  `tl_flags` INT NOT NULL,
  `tl_rank` INT NOT NULL,
  `tl_user_id` INT(11) NOT NULL,
  `tl_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`tl_id`),
  KEY `I_TT_CODE` (`tl_code`),
  FOREIGN KEY (tl_user_id) REFERENCES users(user_id),
  FOREIGN KEY (tl_t_id) REFERENCES templates(t_id)
);

/*
Table structure for template list items
*/

CREATE TABLE `appsresource`.`template_list_items` (
  `tli_id` INT(11) NOT NULL auto_increment,
  `tli_tl_id` INT(11) NOT NULL,
  `tli_ta_id` INT(11) NOT NULL,
  `tli_rank` INT NOT NULL,
  PRIMARY KEY  (`tli_id`),
  FOREIGN KEY (tli_ta_id) REFERENCES template_attributes(ta_id),
  FOREIGN KEY (tli_tl_id) REFERENCES template_lists(tl_id)
);

/*
Table structure for applications
*/

CREATE TABLE `appsresource`.`applications` (
  `app_id` INT(11) NOT NULL auto_increment,
  `app_code` VARCHAR(10) NOT NULL,
  `app_name` VARCHAR(255) NOT NULL,
  `app_desc` TEXT default NULL,
  `app_category` VARCHAR(100) default NULL,
  `app_flags` INT NOT NULL,
  `app_user_id` INT(11) NOT NULL,
  `app_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`app_id`),
  KEY `I_APP_CODE` (`app_code`),
  FOREIGN KEY (app_user_id) REFERENCES users(user_id)
);

/*
Table structure for application_templates
*/

CREATE TABLE `appsresource`.`application_templates` (
  `appt_app_id` INT(11) NOT NULL,
  `appt_t_id` INT(11) NOT NULL,
  `appt_flags` INT NOT NULL,
  `appt_parent_menu` INT(11) default NULL,
  `appt_rank` INT(11) default 0,
  `appt_user_id` INT(11) NOT NULL,
  `appt_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`appt_app_id`, `appt_t_id`),
  FOREIGN KEY (appt_app_id) REFERENCES applications(app_id),
  FOREIGN KEY (appt_t_id) REFERENCES templates(t_id),
  FOREIGN KEY (appt_parent_menu) REFERENCES templates(t_id),
  FOREIGN KEY (appt_user_id) REFERENCES users(user_id)
);

/*
Table structure for application_users
*/

CREATE TABLE `appsresource`.`application_users` (
  `appu_id` INT(11) NOT NULL auto_increment,
  `appu_app_id` INT(11) NOT NULL,
  `appu_appuser_id` INT(11) NOT NULL,
  `appu_flags` INT NOT NULL,
  `appu_top` INT(11),
  `appu_left` INT(11),
  `appu_width` INT(11),
  `appu_height` INT(11),
  `appu_user_id` INT(11) NOT NULL,
  `appu_subscribedat` DATETIME NOT NULL,
  `appu_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`appu_id`),
  FOREIGN KEY (appu_app_id) REFERENCES applications(app_id),
  FOREIGN KEY (appu_appuser_id) REFERENCES users(user_id),
  FOREIGN KEY (appu_user_id) REFERENCES users(user_id)
);

/*
Table structure for objects
*/

CREATE TABLE `appsresource`.`aobjects` (
  `ao_id` INT(11) NOT NULL auto_increment,
  `ao_ot_id` INT(11) NOT NULL,
  `ao_user_id` INT(11) NOT NULL,
  `ao_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ao_id`),
  FOREIGN KEY (ao_ot_id) REFERENCES object_types(ot_id),
  FOREIGN KEY (ao_user_id) REFERENCES users(user_id)
);

/*
Table structure for parents
*/

CREATE TABLE `appsresource`.`aobject_parents` (
  `pa_ao_id` INT(11) NOT NULL,
  `pa_ot_id` INT(11) NOT NULL,
  `pa_level` INT(11) NOT NULL,
  PRIMARY KEY  (`pa_ao_id`, `pa_ot_id`),
  FOREIGN KEY (pa_ao_id) REFERENCES aobjects(ao_id) ON DELETE CASCADE,
  FOREIGN KEY (pa_ot_id) REFERENCES object_types(ot_id)
);

/*
Table structure for values
*/


CREATE TABLE `appsresource`.`avalues` (
  `av_id` INT(11) NOT NULL auto_increment,
  `av_ao_id` INT(11) NOT NULL,
  `av_oa_id` INT(11) NOT NULL,
  `av_rank` INT(11) NOT NULL,
  `av_user_id` INT(11) NOT NULL,
  `av_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`av_id`),
  KEY `av_ao_oa` (`av_ao_id`,`av_oa_id`),
  FOREIGN KEY (av_ao_id) REFERENCES aobjects(ao_id) ON DELETE CASCADE,
  FOREIGN KEY (av_oa_id) REFERENCES object_attributes(oa_id),
  FOREIGN KEY (av_user_id) REFERENCES users(user_id)
);

/*
Table structure for string values
*/

CREATE TABLE `appsresource`.`avalues_char` (
  `avc_av_id` INT(11) NOT NULL,
  `avc_lang_id` SMALLINT NOT NULL DEFAULT 0,
  `avc_value` VARCHAR(255) NOT NULL,
  PRIMARY KEY  (`avc_av_id`, `avc_lang_id`),
  KEY `avalues_char_value` (`avc_value`),
  FOREIGN KEY (avc_av_id) REFERENCES avalues(av_id) ON DELETE CASCADE,
  FOREIGN KEY (avc_lang_id) REFERENCES languages(lang_id)
);

/*
Table structure for date values
*/

CREATE TABLE `appsresource`.`avalues_date` (
  `avd_av_id` INT(11) NOT NULL,
  `avd_value` DATETIME NOT NULL,
  PRIMARY KEY  (`avd_av_id`),
  FOREIGN KEY (avd_av_id) REFERENCES avalues(av_id) ON DELETE CASCADE
);

/*
Table structure for numeric values
*/

CREATE TABLE `appsresource`.`avalues_number` (
  `avn_av_id` INT(11) NOT NULL,
  `avn_value` DOUBLE NOT NULL,
  PRIMARY KEY  (`avn_av_id`),
  FOREIGN KEY (avn_av_id) REFERENCES avalues(av_id) ON DELETE CASCADE
);

/*
Table structure for reference values
*/

CREATE TABLE `appsresource`.`avalues_ref` (
  `avr_av_id` INT(11) NOT NULL,
  `avr_value` INT(11) NOT NULL,
  PRIMARY KEY  (`avr_av_id`),
  FOREIGN KEY (avr_av_id) REFERENCES avalues(av_id) ON DELETE CASCADE,
  FOREIGN KEY (avr_value) REFERENCES aobjects(ao_id) ON DELETE CASCADE
);

/*
Table structure for text values
*/

CREATE TABLE `appsresource`.`avalues_text` (
  `avt_av_id` INT(11) NOT NULL,
  `avt_lang_id` SMALLINT NOT NULL DEFAULT 0,
  `avt_value` TEXT NOT NULL,
  PRIMARY KEY  (`avt_av_id`, `avt_lang_id`),
  FOREIGN KEY (avt_av_id) REFERENCES avalues(av_id) ON DELETE CASCADE,
  FOREIGN KEY (avt_lang_id) REFERENCES languages(lang_id)
);

/*
Table structure for relations
*/

CREATE TABLE `appsresource`.`relations` (
  `ar_id` INT(11) NOT NULL auto_increment,
  `ar_ao_id1` INT(11) NOT NULL,
  `ar_ao_id2` INT(11) NOT NULL,
  `ar_or_id` INT(11) NOT NULL,
  `ar_rank` INT(11) NOT NULL DEFAULT 0,
  `ar_user_id` INT(11) NOT NULL,
  `ar_lastupdateat` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`ar_id`),
  FOREIGN KEY (ar_ao_id1) REFERENCES aobjects(ao_id) ON DELETE CASCADE,
  FOREIGN KEY (ar_ao_id2) REFERENCES aobjects(ao_id) ON DELETE CASCADE,
  FOREIGN KEY (ar_or_id) REFERENCES object_relations(or_id),
  FOREIGN KEY (ar_user_id) REFERENCES users(user_id)
);


/*
Table structure for import_headers
*/

CREATE TABLE `appsresource`.`import_headers` (
  `ih_id` INT(11) NOT NULL auto_increment,
  `ih_app_id` INT(11) NOT NULL,
  `ih_t_id` INT(11) NOT NULL,
  `ih_filename` VARCHAR(255) NOT NULL,
  `ih_user_id` INT(11) NOT NULL,
  PRIMARY KEY  (`ih_id`),
  FOREIGN KEY (ih_app_id) REFERENCES applications(app_id),
  FOREIGN KEY (ih_t_id) REFERENCES templates(t_id),
  FOREIGN KEY (ih_user_id) REFERENCES users(user_id)
);

/*
Table structure for import_rows
*/

CREATE TABLE `appsresource`.`import_rows` (
  `ir_id` INT(11) NOT NULL auto_increment,
  `ir_ih_id` INT(11) NOT NULL,
  `ir_number` INT(11) NOT NULL,
  `ir_column0` TEXT default NULL,
  `ir_column1` TEXT default NULL,
  `ir_column2` TEXT default NULL,
  `ir_column3` TEXT default NULL,
  `ir_column4` TEXT default NULL,
  `ir_column5` TEXT default NULL,
  `ir_column6` TEXT default NULL,
  `ir_column7` TEXT default NULL,
  `ir_column8` TEXT default NULL,
  `ir_column9` TEXT default NULL,
  `ir_column10` TEXT default NULL,
  `ir_column11` TEXT default NULL,
  `ir_column12` TEXT default NULL,
  `ir_column13` TEXT default NULL,
  `ir_column14` TEXT default NULL,
  `ir_column15` TEXT default NULL,
  `ir_column16` TEXT default NULL,
  `ir_column17` TEXT default NULL,
  `ir_column18` TEXT default NULL,
  `ir_column19` TEXT default NULL,
  `ir_column20` TEXT default NULL,
  `ir_column21` TEXT default NULL,
  `ir_column22` TEXT default NULL,
  `ir_column23` TEXT default NULL,
  `ir_column24` TEXT default NULL,
  `ir_column25` TEXT default NULL,
  `ir_column26` TEXT default NULL,
  `ir_column27` TEXT default NULL,
  `ir_column28` TEXT default NULL,
  `ir_column29` TEXT default NULL,
  `ir_column30` TEXT default NULL,
  `ir_column31` TEXT default NULL,
  `ir_column32` TEXT default NULL,
  `ir_column33` TEXT default NULL,
  `ir_column34` TEXT default NULL,
  `ir_column35` TEXT default NULL,
  `ir_column36` TEXT default NULL,
  `ir_column37` TEXT default NULL,
  `ir_column38` TEXT default NULL,
  `ir_column39` TEXT default NULL,
  `ir_column40` TEXT default NULL,
  `ir_column41` TEXT default NULL,
  `ir_column42` TEXT default NULL,
  `ir_column43` TEXT default NULL,
  `ir_column44` TEXT default NULL,
  `ir_column45` TEXT default NULL,
  `ir_column46` TEXT default NULL,
  `ir_column47` TEXT default NULL,
  `ir_column48` TEXT default NULL,
  `ir_column49` TEXT default NULL,
  `ir_column50` TEXT default NULL,
  `ir_column51` TEXT default NULL,
  `ir_column52` TEXT default NULL,
  `ir_column53` TEXT default NULL,
  `ir_column54` TEXT default NULL,
  `ir_column55` TEXT default NULL,
  `ir_column56` TEXT default NULL,
  `ir_column57` TEXT default NULL,
  `ir_column58` TEXT default NULL,
  `ir_column59` TEXT default NULL,
  `ir_column60` TEXT default NULL,
  `ir_column61` TEXT default NULL,
  `ir_column62` TEXT default NULL,
  `ir_column63` TEXT default NULL,
  `ir_column64` TEXT default NULL,
  `ir_column65` TEXT default NULL,
  `ir_column66` TEXT default NULL,
  `ir_column67` TEXT default NULL,
  `ir_column68` TEXT default NULL,
  `ir_column69` TEXT default NULL,
  `ir_column70` TEXT default NULL,
  `ir_column71` TEXT default NULL,
  `ir_column72` TEXT default NULL,
  `ir_column73` TEXT default NULL,
  `ir_column74` TEXT default NULL,
  `ir_column75` TEXT default NULL,
  `ir_column76` TEXT default NULL,
  `ir_column77` TEXT default NULL,
  `ir_column78` TEXT default NULL,
  `ir_column79` TEXT default NULL,
  `ir_column80` TEXT default NULL,
  `ir_column81` TEXT default NULL,
  `ir_column82` TEXT default NULL,
  `ir_column83` TEXT default NULL,
  `ir_column84` TEXT default NULL,
  `ir_column85` TEXT default NULL,
  `ir_column86` TEXT default NULL,
  `ir_column87` TEXT default NULL,
  `ir_column88` TEXT default NULL,
  `ir_column89` TEXT default NULL,
  `ir_column90` TEXT default NULL,
  `ir_column91` TEXT default NULL,
  `ir_column92` TEXT default NULL,
  `ir_column93` TEXT default NULL,
  `ir_column94` TEXT default NULL,
  `ir_column95` TEXT default NULL,
  `ir_column96` TEXT default NULL,
  `ir_column97` TEXT default NULL,
  `ir_column98` TEXT default NULL,
  `ir_column99` TEXT default NULL,
  PRIMARY KEY  (`ir_id`),
  FOREIGN KEY (ir_ih_id) REFERENCES import_headers(ih_id)
);

/*
Table structure for logs
*/

CREATE TABLE `appsresource`.`logs` (
  `log_id` INT(11) NOT NULL auto_increment,
  `log_type_id` INT(11) NOT NULL,
  `log_ao_id` INT(11) NOT NULL,
  `log_type` SMALLINT NOT NULL,
  `log_value` VARCHAR(255) default NULL,
  `log_lang_id` SMALLINT DEFAULT NULL,
  `log_user_id` INT(11) NOT NULL,
  `log_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY  (`log_id`),
  KEY `logs_time` (`log_ao_id`,`log_time`)
);


/*
 * triggers
 */

delimiter |

CREATE TRIGGER `appsresource`.`insert_parents` AFTER INSERT ON aobjects
  FOR EACH ROW BEGIN
    DECLARE ot INT;
    DECLARE level INT;
    
    SET ot = NEW.ao_ot_id;
    SET level = 0;
    
    REPEAT
      INSERT INTO aobject_parents (pa_ao_id, pa_ot_id, pa_level) 
      VALUES (NEW.ao_id, ot, level);
      
      SET level = level + 1;
      SET ot = (SELECT ot_ot_id FROM object_types WHERE ot_id = ot); 
       
    UNTIL ot is null END REPEAT;
  END;
|

CREATE TRIGGER `appsresource`.`log_bd_aobject` BEFORE DELETE ON `aobjects`
FOR EACH ROW
BEGIN
  DECLARE oa INT;
  DECLARE user_id INT;
  DECLARE lastupdate TIMESTAMP;
  DECLARE value_number VARCHAR(255);
  DECLARE value_char VARCHAR(255);
  DECLARE lang INT;
  DECLARE value_date VARCHAR(255);
  DECLARE value_ref VARCHAR(255);
  DECLARE no_more_values INT DEFAULT FALSE;

  DECLARE avalues CURSOR FOR 
    SELECT av_oa_id, av_user_id, av_lastupdateat, CAST(avn_value as CHAR), avc_value, avc_lang_id, 
           CAST(avd_value as CHAR), CAST(avr_value as CHAR)
    FROM avalues left outer join avalues_number on (avn_av_id = av_id)
         left outer join avalues_char on (avc_av_id = av_id)
         left outer join avalues_date on (avd_av_id = av_id)
         left outer join avalues_ref on (avr_av_id = av_id)
    WHERE av_ao_id = OLD.ao_id; 

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_values = TRUE; 

  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_user_id, log_time) 
  VALUES (OLD.ao_ot_id, OLD.ao_id, 0, OLD.ao_user_id, OLD.ao_lastupdateat);

  OPEN avalues;
  repval: REPEAT

    FETCH avalues INTO oa, user_id, lastupdate, value_number, value_char, lang, value_date, value_ref;
    IF no_more_values THEN
        LEAVE repval;
    END IF;
    
    IF value_number is not null THEN SET value_char = value_number;
    ELSE IF value_date is not null THEN SET value_char = value_date; 
         ELSE IF value_ref is not null THEN SET value_char = value_ref; 
              END IF;
         END IF;
    END IF;
    
    INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_lang_id, log_user_id, log_time) 
    VALUES (oa, OLD.ao_id, 3, value_char, lang, user_id, lastupdate);

  UNTIL FALSE
  END REPEAT repval; 
  CLOSE avalues;
  
END
|

CREATE TRIGGER `appsresource`.`log_bd_avalues` BEFORE DELETE ON `avalues`
FOR EACH ROW
BEGIN
  DECLARE value_number VARCHAR(255);
  DECLARE value_char VARCHAR(255);
  DECLARE lang INT;
  DECLARE value_date VARCHAR(255);
  DECLARE value_ref VARCHAR(255);
  DECLARE no_more_values INT DEFAULT FALSE;

  DECLARE avalues CURSOR FOR 
    SELECT CAST(avn_value as CHAR), avc_value, avc_lang_id, 
           CAST(avd_value as CHAR), CAST(avr_value as CHAR)
    FROM avalues left outer join avalues_number on (avn_av_id = av_id)
         left outer join avalues_char on (avc_av_id = av_id)
         left outer join avalues_date on (avd_av_id = av_id)
         left outer join avalues_ref on (avr_av_id = av_id)
    WHERE av_id = OLD.av_id; 

  DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_values = TRUE; 

  OPEN avalues;
  repval: REPEAT

    FETCH avalues INTO value_number, value_char, lang, value_date, value_ref;
    IF no_more_values THEN
        LEAVE repval;
    END IF;
    
    IF value_number is not null THEN SET value_char = value_number;
    ELSE IF value_date is not null THEN SET value_char = value_date; 
         ELSE IF value_ref is not null THEN SET value_char = value_ref; 
              END IF;
         END IF;
    END IF;
    
    INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_lang_id, log_user_id, log_time) 
    VALUES (OLD.av_oa_id, OLD.av_ao_id, 3, value_char, lang, OLD.av_user_id, OLD.av_lastupdateat);

  UNTIL FALSE
  END REPEAT repval; 
  CLOSE avalues;
  
END
|


CREATE TRIGGER `appsresource`.`log_bu_avalues_char` BEFORE UPDATE ON `avalues_char`
FOR EACH ROW
BEGIN
  IF OLD.avc_value <> NEW.avc_value THEN
	  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_lang_id, log_user_id, log_time) 
	  SELECT av_oa_id, av_ao_id, 3, OLD.avc_value, OLD.avc_lang_id, av_user_id, av_lastupdateat 
	  FROM avalues
	  WHERE av_id = OLD.avc_av_id;
  END IF;	  
END
|

CREATE TRIGGER `appsresource`.`log_bd_avalues_char` BEFORE DELETE ON `avalues_char`
FOR EACH ROW
BEGIN
  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_lang_id, log_user_id, log_time) 
  SELECT av_oa_id, av_ao_id, 3, OLD.avc_value, OLD.avc_lang_id, av_user_id, av_lastupdateat 
  FROM avalues
  WHERE av_id = OLD.avc_av_id;
END
|

CREATE TRIGGER `appsresource`.`log_bu_avalues_date` BEFORE UPDATE ON `avalues_date`
FOR EACH ROW
BEGIN
  IF OLD.avd_value <> NEW.avd_value THEN
	  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
	  SELECT av_oa_id, av_ao_id, 3, OLD.avd_value, av_user_id, av_lastupdateat 
	  FROM avalues
	  WHERE av_id = OLD.avd_av_id;
  END IF;
END
|

CREATE TRIGGER `appsresource`.`log_bd_avalues_date` BEFORE DELETE ON `avalues_date`
FOR EACH ROW
BEGIN
  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
  SELECT av_oa_id, av_ao_id, 3, OLD.avd_value, av_user_id, av_lastupdateat 
  FROM avalues
  WHERE av_id = OLD.avd_av_id;
END
|

CREATE TRIGGER `appsresource`.`log_bu_avalues_number` BEFORE UPDATE ON `avalues_number`
FOR EACH ROW
BEGIN
  IF OLD.avn_value <> NEW.avn_value THEN
	  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
	  SELECT av_oa_id, av_ao_id, 3, OLD.avn_value, av_user_id, av_lastupdateat 
	  FROM avalues
	  WHERE av_id = OLD.avn_av_id;
  END IF;
END
|

CREATE TRIGGER `appsresource`.`log_bd_avalues_number` BEFORE DELETE ON `avalues_number`
FOR EACH ROW
BEGIN
  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
  SELECT av_oa_id, av_ao_id, 3, OLD.avn_value, av_user_id, av_lastupdateat 
  FROM avalues
  WHERE av_id = OLD.avn_av_id;
END
|

CREATE TRIGGER `appsresource`.`delete_relations` BEFORE DELETE ON avalues_ref
  FOR EACH ROW BEGIN
	  DELETE relations.* FROM avalues, relations, object_attributes
	  WHERE av_id = OLD.avr_av_id 
	    AND ar_ao_id2 = OLD.avr_value
	    AND av_oa_id = oa_id
	    AND oa_shared2 = ar_or_id
	    AND av_ao_id = ar_ao_id1
      AND av_rank = ar_rank;
    
    DELETE relations.* FROM avalues, object_relations, relations, 
        object_attributes
    WHERE av_id = OLD.avr_av_id 
      AND ar_ao_id1 = OLD.avr_value
	    AND av_oa_id = oa_id
	    AND oa_shared2 = or_id
	    AND or_or_id = ar_or_id
	    AND av_ao_id = ar_ao_id2
      AND av_rank = ar_rank; 

  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
  SELECT av_oa_id, av_ao_id, 3, OLD.avr_value, av_user_id, av_lastupdateat 
  FROM avalues
  WHERE av_id = OLD.avr_av_id;
	    
END;
|

CREATE TRIGGER `appsresource`.`delete_avalues` AFTER DELETE ON avalues_ref
  FOR EACH ROW BEGIN
    DELETE FROM avalues
    WHERE av_id = OLD.avr_av_id; 
  END;
|

CREATE TRIGGER `appsresource`.`insert_relations` AFTER INSERT ON avalues_ref
  FOR EACH ROW BEGIN
	INSERT INTO relations (ar_ao_id1, ar_ao_id2, ar_or_id, ar_rank, ar_user_id, ar_lastupdateat) 
	  SELECT av_ao_id, NEW.avr_value, oa_shared2, av_rank, av_user_id, now() 
	  FROM avalues, object_attributes
	  WHERE av_id = NEW.avr_av_id
	    AND av_oa_id = oa_id
	    AND oa_shared2 > 0;

  INSERT INTO relations (ar_ao_id1, ar_ao_id2, ar_or_id, ar_user_id, ar_lastupdateat) 
    SELECT NEW.avr_value, av_ao_id, or_or_id, av_user_id, now() 
    FROM avalues, object_attributes, object_relations
    WHERE av_id = NEW.avr_av_id
      AND av_oa_id = oa_id
      AND oa_shared2 > 0
      AND oa_shared2 = or_id
      AND or_or_id > 0;

END;
|

CREATE TRIGGER `appsresource`.`log_bu_avalues_ref` BEFORE UPDATE ON `avalues_ref`
FOR EACH ROW
BEGIN
  IF OLD.avr_value <> NEW.avr_value THEN
      INSERT INTO logs (log_type_id, log_ao_id, log_type, log_value, log_user_id, log_time) 
      SELECT av_oa_id, av_ao_id, 3, OLD.avr_value, av_user_id, av_lastupdateat 
      FROM avalues
      WHERE av_id = OLD.avr_av_id;
  END IF;
END;
|

CREATE TRIGGER `appsresource`.`update_relations` AFTER UPDATE ON avalues_ref
  FOR EACH ROW BEGIN
  UPDATE relations, avalues, object_attributes
    SET ar_ao_id2 = NEW.avr_value
    WHERE av_id = NEW.avr_av_id 
      AND av_oa_id = oa_id
      AND ar_or_id = oa_shared2
      AND ar_ao_id1 = av_ao_id
      AND ar_rank = av_rank;

  UPDATE relations, avalues, object_attributes, object_relations
    SET ar_ao_id1 = NEW.avr_value
    WHERE av_id = NEW.avr_av_id 
      AND av_oa_id = oa_id
      AND oa_shared2 = or_id
      AND ar_or_id = or_or_id
      AND ar_ao_id2 = av_ao_id;
END;
|

CREATE PROCEDURE `appsresource`.`clean_multivalues` (IN obj INT, IN rank INT)
BEGIN
  DECLARE count INT;

  SET count = (SELECT count(av_id) 
               FROM avalues
               WHERE av_ao_id = obj
                 AND av_rank = rank); 

  IF count = 0 THEN
     UPDATE relations
     SET ar_rank = ar_rank - 1
     WHERE ar_ao_id1 = obj
       AND ar_rank > rank;  

     UPDATE avalues
     SET av_rank = av_rank - 1
     WHERE av_ao_id = obj
       AND av_rank > rank;
  END IF;  
END;
|

CREATE PROCEDURE `appsresource`.`share_values`(IN obj INT, IN relobj INT, IN user_id INT)
BEGIN
  DECLARE oa INT;
  DECLARE vt INT;
  DECLARE value_number DOUBLE;
  DECLARE value_char VARCHAR(255);
  DECLARE lang INT;
  DECLARE value_date DATETIME;
  DECLARE value_ref INT;
  DECLARE av INT;
  DECLARE ao INT;
  DECLARE n INT;
  DECLARE no_more_attributes INT DEFAULT FALSE;

  DECLARE shared_attributes CURSOR FOR 
    SELECT oa1.oa_id, vt.vt_type, avn_value, avc_value, avc_lang_id, avd_value, avr_value
    FROM aobjects ao1, object_attributes oa1, aobjects ao2, object_attributes  oa2, value_types vt,
         avalues left outer join avalues_number on (avn_av_id = av_id)
         left outer join avalues_char on (avc_av_id = av_id)
         left outer join avalues_date on (avd_av_id = av_id)
         left outer join avalues_ref on (avr_av_id = av_id)
    WHERE ao1.ao_id = obj AND ao1.ao_ot_id = oa1.oa_ot_id 
        AND ao2.ao_id = relobj AND ao2.ao_ot_id = oa2.oa_ot_id 
        AND oa1.oa_vt_id = oa2.oa_vt_id AND oa1.oa_vt_id = vt.vt_id 
        AND vt.vt_flags & 2
        AND av_ao_id = relobj AND av_oa_id = oa2.oa_id; 
              
  DECLARE attributes_to_clean CURSOR FOR 
    SELECT av_id, avr_value 
    FROM avalues, avalues_ref 
    WHERE av_ao_id = obj AND av_id = avr_av_id AND avr_value <> relobj; 
              
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET no_more_attributes = TRUE; 

  OPEN shared_attributes;
  repshare: REPEAT

    FETCH shared_attributes INTO oa, vt, value_number, value_char, lang, value_date, value_ref;
    IF no_more_attributes THEN
        LEAVE repshare;
    END IF;
    
    SET av = (SELECT av_id 
                 FROM avalues
                 WHERE av_ao_id = obj
                   AND av_oa_id = oa
                   AND av_rank = 0); 

    IF av is null THEN
      INSERT INTO avalues (av_ao_id, av_oa_id, av_rank, av_user_id) 
        VALUES (obj, oa, 0, user_id);
      SET av = (SELECT LAST_INSERT_ID());
    END IF;  

    CASE vt
      WHEN 1 THEN
        INSERT INTO avalues_number (avn_av_id, avn_value) 
          VALUES (av, value_number)
          ON DUPLICATE KEY UPDATE avn_value = value_number;
      WHEN 2 THEN
        INSERT INTO avalues_number (avn_av_id, avn_value) 
          VALUES (av, value_number)
          ON DUPLICATE KEY UPDATE avn_value = value_number;
      WHEN 3 THEN
        INSERT INTO avalues_char (avc_av_id, avc_lang_id, avc_value) 
          VALUES (av, lang, value_char)
          ON DUPLICATE KEY UPDATE avc_value = value_char;
      WHEN 4 THEN
        INSERT INTO avalues_date (avd_av_id, avd_value) 
          VALUES (av, value_date)
          ON DUPLICATE KEY UPDATE avd_value = value_date;
      WHEN 5 THEN
        INSERT INTO avalues_ref (avr_av_id, avr_value) 
          VALUES (av, value_ref)
          ON DUPLICATE KEY UPDATE avr_value = value_ref;
    END CASE;


  UNTIL FALSE
  END REPEAT repshare; 
  CLOSE shared_attributes;

  SET no_more_attributes = FALSE;

  OPEN attributes_to_clean;
  repclean: REPEAT

    FETCH attributes_to_clean INTO av, ao;
    IF no_more_attributes THEN
        LEAVE repclean;
    END IF;

    SET n = (SELECT count(*)
        FROM aobjects ao1, object_attributes oa1, aobjects ao2, object_attributes  oa2, value_types vt,
             avalues av2 left outer join avalues_number avn2 on (avn2.avn_av_id = av2.av_id)
             left outer join avalues_char avc2 on (avc2.avc_av_id = av2.av_id)
             left outer join avalues_date avd2 on  (avd2.avd_av_id = av2.av_id)
             left outer join avalues_ref avr2 on (avr2.avr_av_id = av2.av_id),
             avalues av1 left outer join avalues_number avn1 on (avn1.avn_av_id = av1.av_id)
             left outer join avalues_char avc1 on (avc1.avc_av_id = av1.av_id)
             left outer join avalues_date avd1 on (avd1.avd_av_id = av1.av_id)
             left outer join avalues_ref avr1 on (avr1.avr_av_id = av1.av_id)
             
        WHERE ao1.ao_id = obj AND ao1.ao_ot_id = oa1.oa_ot_id 
            AND ao2.ao_id = ao AND ao2.ao_ot_id = oa2.oa_ot_id 
            AND oa1.oa_vt_id = oa2.oa_vt_id AND oa1.oa_vt_id = vt.vt_id 
            AND vt.vt_flags & 2
            AND av1.av_ao_id = obj AND av1.av_oa_id = oa1.oa_id
            AND av2.av_ao_id = ao AND av2.av_oa_id = oa2.oa_id 
            
            AND (avn1.avn_value <> avn2.avn_value 
            OR (avc1.avc_value <> avc2.avc_value AND avc1.avc_lang_id = avc2.avc_lang_id) 
            OR avd1.avd_value <> avd2.avd_value 
            OR avr1.avr_value <> avr2.avr_value));
        
    IF n > 0 THEN
        DELETE FROM avalues WHERE av_id = av;
    END IF;

  UNTIL FALSE
  END REPEAT repclean; 
  CLOSE attributes_to_clean;

END
|

CREATE PROCEDURE `log_delete`(IN type INT, IN obj INT, IN oper INT, IN user_id INT)
BEGIN
  INSERT INTO logs (log_type_id, log_ao_id, log_type, log_user_id) 
    VALUES (type, obj, oper, user_id);
END
|

CREATE PROCEDURE `get_object_attributes`(IN otid INT)
BEGIN
    SET @ots = CONCAT('(',otid);
    SET @ot = otid;
    repshare: REPEAT
        SET @ot = (SELECT ot_ot_id 
                 FROM object_types
                 WHERE ot_id = @ot); 
        IF @ot is not null THEN
            SET @ots = CONCAT(@ots,',',@ot);
        END IF;
    
    UNTIL @ot is null
    END REPEAT repshare; 

    SET @ots = CONCAT(@ots,')');
    
    SET @s = CONCAT('SELECT * 
        FROM object_attributes left outer join units on (oa_unit_id = unit_id),
             value_types, object_types
        WHERE oa_vt_id = vt_id
          AND oa_ot_id = ot_id
          AND ot_id in ', @ots, ' ORDER BY oa_name');
    PREPARE stmt FROM @s;
    EXECUTE stmt;
END
|

delimiter ;