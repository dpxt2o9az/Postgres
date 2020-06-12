create table if not exists aoi_codes (
    aoi_code varchar(2) not null,
    description varchar(128) not null,
    primary key ( aoi_code )
);

create table if not exists country_codes (
    country_code varchar(2) not null,
    tri_code varchar(3),
    description varchar(128) not null,
    primary key ( country_code )
);

create table if not exists aoi_country_codes (
    country_code varchar(2) not null,
    aoi_code varchar(2) not null,
    primary key ( country_code, aoi_code ),
    foreign key ( country_code ) references country_codes ( country_code ),
    foreign key ( aoi_code ) references aoi_codes ( aoi_code )
);

-- create table if not exists intercept_aois (
--     intercept_id int not null,
--     aois varchar(4000) not null,
--     primary key ( intercept_id ),
--     foreign key ( intercept_id ) references intercepts ( intercept_id ) on delete cascade
-- );

insert into country_codes ( country_code, description ) values ( 'AA', 'test country code' );

insert into aoi_codes ( aoi_code, description ) values ( 'WW', 'Worldwide' );
insert into aoi_codes ( aoi_code, description ) values ( 'W1', 'Psuedo Worldwide' );

insert into aoi_country_codes ( aoi_code, country_code ) 
  values ( 'WW', 'AA' ),
         ( 'W1', 'AA' );

create or replace view intercept_aois as 
  select intercept_id, string_agg(aoi_code, ';' order by aoi_code) from (
    select intercept_id, aoi_code from intercepts
      join idb_states
        using (intercept_id)
      join aoi_country_codes
        using ( country_code )
      where flow_control = 'AOI-REQ'
  ) as a
  group by intercept_id
;
