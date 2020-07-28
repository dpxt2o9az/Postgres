create table if not exists excluded_elnots (
    elnot varchar(5) not null primary key
);

create table if not exists mod_types (
    mt varchar(2) not null primary key,
    mt_desc varchar(15) not null
);

create table if not exists included_rd_out_stat (
    rd_out_stat varchar(2) not null primary key
);

create table if not exists comparison_queue (
    intercept_id bigint not null primary key
);



insert into excluded_elnots ( elnot ) values 
   ('AAAAA'), ('BBBBB');

insert into included_rd_out_stat ( rd_out_stat ) values ( 'AA' ), ( 'BB' );

insert into mod_types ( mt, mt_desc ) values ( 'AA', 'AA' ), ('BB', 'BB');

