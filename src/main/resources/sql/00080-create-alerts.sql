create table team_users (
  userid integer not null,
  name varchar(50) not null, 
  rank varchar(10),
  team varchar(3),
  status varchar(1),
  email_address varchar(50),
  primary key (userid)
);

create table alert_groups (
  alert_group_id integer not null,
  alert_group_name varchar(20) not null,
  primary key (alert_group_id)
);

create table alert_group_team_users (
  alert_group_id integer not null,
  team_users_userid integer not null,
  primary key ( alert_group_id, team_users_userid ),
  foreign key (alert_group_id) references
    alert_groups (alert_group_id),
  foreign key ( team_users_userid ) references
    team_users ( userid )
);

create table alert_group_intercept_rules (
  intercept_dataload_rule_id integer not null,
  alert_group_id integer not null,
  primary key ( intercept_dataload_rule_id, alert_group_id ),
  foreign key ( intercept_dataload_rule_id ) references
    intercept_dataload_rules_table ( intercept_dataload_rule_id ),
  foreign key ( alert_group_id ) references
    alert_groups ( alert_group_id )
);
