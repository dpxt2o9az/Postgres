
create table rules (
    id serial not null,
    remarks text,
    primary key (id)
);

create table criteria (
    id integer not null,
    op varchar(2) not null,
    field varchar(20) not null,
    value varchar(20) not null,
    value2 varchar(20),
    primary key ( id )
);

create table rules_criteria (
    rule_id integer not null,
    criteria_id integer not null,
    primary key (rule_id, criteria_id),
    foreign key ( rule_id ) 
      references rules ( id ),
    foreign key ( criteria_id ) 
      references criteria ( id )
);

create or replace view rules_with_criteria as 
  select r.id rule_id, remarks, c.id criteria_id, op, field, value, value2 from rules r 
    join rules_criteria rc
      on ( r.id = rc.rule_id )
    join criteria c
      on ( c.id = rc.criteria_id )
  ;

