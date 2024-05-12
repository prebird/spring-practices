truncate table person_from;
truncate table person_to;

insert into person_from
    (last_name, first_name, age)
values
    ('김', '진우', 20)
    ,('서', '경석', 30)
    ,('이', '민우', 40);
