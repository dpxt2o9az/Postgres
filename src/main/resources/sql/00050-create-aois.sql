create table if not exists intercept_aois (
    intercept_id int not null,
    aois varchar(4000) not null,
    primary key ( intercept_id ),
    foreign key ( intercept_id ) references intercepts ( intercept_id ) on delete cascade
);
