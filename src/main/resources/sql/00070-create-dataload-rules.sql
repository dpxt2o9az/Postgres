create table intercept_dataload_rules_table (
  intercept_dataload_rule_id integer not null,
  type numeric not null,
  comment_val varchar(500) not null,
  status numeric not null,
  re_id_elnot varchar(10),
  st_conf numeric not null,
  sp_conf numeric not null,
  ir_conf numeric not null,
  pd_conf numeric not null,
  mt_conf numeric not null,
  mt_conf_new_mt varchar(5),
  last_updated_by varchar(32),
  creator varchar(32),
  created_date timestamp,
  last_edited timestamp,
  email_list varchar(2000),
  ops_page varchar(6),
  bbs varchar(6),
  heardcount numeric not null default 0,
  heardcount_threshold numeric not null default 1,
  primary key (intercept_dataload_rule_id)
);

create table intercept_dataload_details (
  intercept_dataload_detail_id integer not null,
  rule_id integer not null,
  intercept_field varchar(80) not null,
  operation varchar(12) not null,
  value varchar(500) not null,
  value2 varchar(500),
  primary key (intercept_dataload_detail_id),
  foreign key (rule_id) references
    intercept_dataload_rules_table ( intercept_dataload_rule_id )
);
