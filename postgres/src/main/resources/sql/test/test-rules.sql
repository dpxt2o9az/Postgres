
insert into rules ( id, remarks ) values ( 10, 'test rule' );

insert into criteria ( id, op, field, value, value2 ) 
  values ( 12, 'BW', 'any-rf', 340, 450 );
insert into criteria ( id, op, field, value ) 
  values ( 11, 'NE', 'elnot', 'L0000'),
         ( 13, 'GT', 'elnot', 'A0000'),
         ( 14, 'LT', 'elnot', 'B0000'),
         ( 15, 'GE', 'elnot', 'A0000'),
         ( 16, 'LE', 'elnot', 'C0000'),
         ( 17, 'EQ', 'elnot', 'XYYXY');

insert into rules_criteria ( rule_id, criteria_id ) values ( 10, 11 );
insert into rules_criteria ( rule_id, criteria_id ) values ( 10, 12 );