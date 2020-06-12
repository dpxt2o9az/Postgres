create table if not exists idb_states ( 
    intercept_id int not null,
    flow_control varchar(50) not null,
    last_modified timestamp not null default current_timestamp,
    PRIMARY KEY (intercept_id, flow_control ),
    FOREIGN KEY ( intercept_id ) references intercept ( intercept_id )
);

