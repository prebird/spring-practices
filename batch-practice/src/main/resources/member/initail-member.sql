truncate table member;

insert into member(id, name, member_status, leave_at, is_deleted)
values (1, 'test1', 'INUSE', null, 'N')
, (2, 'test2', 'LEAVE', '2021-01-01 00:00:00', 'N')
, (3, 'test3', 'LEAVE', '2019-01-01 00:00:00', 'N');
