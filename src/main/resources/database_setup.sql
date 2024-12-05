-- De to drop/create for neden er hvis man vil starte forfra.
drop database if exists `database`;
create database `database`;
use `database`;

-- Disse er blot tables jeg opretter
create table user (

    user_id int primary key auto_increment,
    username varchar(50) not null,
    full_name varchar(50) not null,
    clocked_in boolean not null,
    on_break boolean not null,
    logged_in boolean not null,
    password varchar(255) default null, 
    role enum('employee', 'manager', 'deaktiverede') not null,
    check (role = 'employee' OR (role = 'deaktiverede' OR (role = 'manager' and password is not null)))

);

create table timelog (
                         log_id int primary key auto_increment,
                         user_id int not null,
                         shift_date date not null,
                         event_time datetime not null,
                         event_type enum('check_in', 'check_out', 'break_start', 'break_end') not null,
                         edited_time datetime,
                         FOREIGN KEY (user_id) REFERENCES user(user_id)
);

create table weekly_timelog (
                                weekly_id int primary key auto_increment,
                                user_id int not null,
                                full_name varchar(50) not null,
                                week_start date not null,
                                total_hours_worked time not null,
                                FOREIGN KEY (user_id) REFERENCES user(user_id)
);

create table note (
                      note_id int primary key auto_increment,
                      note_date date not null,
                      writer_id int not null,
                      recipient_id int not null,
                      full_name varchar(50),
                      Written_note varchar(255),
                      FOREIGN KEY (writer_id) REFERENCES user(user_id),
                      FOREIGN KEY (recipient_id) REFERENCES user(user_id)
);

-- Til at populate user table
insert into user (username, full_name, password, role, clocked_in, on_break, logged_in) values
('brian', 'Brian Donatello', '$2a$10$ddoVNreE/6trBy5BIn2Al.JKAeclpb3kmliVN0oEBd8yX.Xex89WW', 'manager', true, true, false), -- Example hashed password for 'admin'
('dorte', 'Dorte Johannes', null, 'employee', true, false, false),
('emilie', 'Emilie Nutella', null, 'employee', true, false, true),
('kim', 'Kim Erik', null, 'employee', true, false, true),
('henrik', 'Henrik Larsen', null, 'employee', true, false, true),
('frederik', 'Frederik Skov', null, 'employee', true, false, true),
('børge', 'Børge Lund', null, 'employee', true, false, true),
('villads', 'Villads Schantz', null, 'employee', true, false, true),
('jonna', 'Jonna Jørgensen', null, 'employee', true, false, true),
('sanny', 'Sanny Jensen', null, 'employee', true, false, true),
('john', 'John Anders', null, 'employee', true, false, true),
('åge', 'Åge Boge', null, 'employee', true, false, true),
('Frederikke-Karoline', 'Fredderikke-Karoline Johannesen', null, 'employee', true, false, true);



-- Test til flere breaks på en dag
insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(1, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(1, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(1, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(1, '2024-11-01', '2024-11-01 15:00:00', 'break_start'),
(1, '2024-11-01', '2024-11-01 15:30:00', 'break_end'),
(1, '2024-11-01', '2024-11-01 23:00:00', 'check_out'),
-- Day 2
(1, '2024-11-02', '2024-11-02 08:00:00', 'check_in'),
(1, '2024-11-02', '2024-11-02 12:00:00', 'break_start'),
(1, '2024-11-02', '2024-11-02 12:30:00', 'break_end'),
(1, '2024-11-02', '2024-11-02 14:00:00', 'check_out');

-- Test til samme user men forskellige uger
insert into timelog (user_id, shift_date, event_time, event_type) values
-- december brian
(1, '2024-12-01', '2024-12-01 09:00:00', 'check_in'),
(1, '2024-12-01', '2024-12-01 12:00:00', 'break_start'),
(1, '2024-12-01', '2024-12-01 12:30:00', 'break_end'),
(1, '2024-12-01', '2024-12-01 14:00:00', 'check_out'),

(1, '2024-12-02', '2024-12-02 09:00:00', 'check_in'),
(1, '2024-12-02', '2024-12-02 12:00:00', 'break_start'),
(1, '2024-12-02', '2024-12-02 12:30:00', 'break_end'),
(1, '2024-12-02', '2024-12-02 14:00:00', 'check_out'),

(1, '2024-12-03', '2024-12-03 09:00:00', 'check_in'),
(1, '2024-12-03', '2024-12-03 12:00:00', 'break_start'),
(1, '2024-12-03', '2024-12-03 12:30:00', 'break_end'),
(1, '2024-12-03', '2024-12-03 14:00:00', 'check_out'),

(1, '2024-12-04', '2024-12-04 11:59:00', 'check_in'),
(1, '2024-12-04', '2024-12-04 12:00:00', 'break_start'),
(1, '2024-12-04', '2024-12-04 12:30:00', 'break_end'),
(1, '2024-12-04', '2024-12-04 14:00:00', 'check_out'),
-- december Dorte
(2, '2024-12-01', '2024-12-01 09:00:00', 'check_in'),
(2, '2024-12-01', '2024-12-01 12:00:00', 'break_start'),
(2, '2024-12-01', '2024-12-01 12:30:00', 'break_end'),
(2, '2024-12-01', '2024-12-01 14:00:00', 'check_out'),

(2, '2024-12-02', '2024-12-02 09:00:00', 'check_in'),
(2, '2024-12-02', '2024-12-02 12:00:00', 'break_start'),
(2, '2024-12-02', '2024-12-02 12:30:00', 'break_end'),
(2, '2024-12-02', '2024-12-02 14:00:00', 'check_out'),

(2, '2024-12-03', '2024-12-03 11:59:00', 'check_in'),
(2, '2024-12-03', '2024-12-03 12:00:00', 'break_start'),
(2, '2024-12-03', '2024-12-03 12:30:00', 'break_end'),
(2, '2024-12-03', '2024-12-03 13:00:00', 'break_start'),
(2, '2024-12-03', '2024-12-03 13:30:00', 'break_end'),
(2, '2024-12-03', '2024-12-03 14:00:00', 'check_out'),

(2, '2024-12-04', '2024-12-04 8:59:00', 'check_in'),
(2, '2024-12-04', '2024-12-04 12:00:00', 'break_start'),
(2, '2024-12-04', '2024-12-04 12:30:00', 'break_end'),
(2, '2024-12-04', '2024-12-04 14:00:00', 'check_out'),
-- december emilie
(3, '2024-12-01', '2024-12-01 09:00:00', 'check_in'),
(3, '2024-12-01', '2024-12-01 12:00:00', 'break_start'),
(3, '2024-12-01', '2024-12-01 12:30:00', 'break_end'),
(3, '2024-12-01', '2024-12-01 14:00:00', 'check_out'),

(3, '2024-12-02', '2024-12-02 09:00:00', 'check_in'),
(3, '2024-12-02', '2024-12-02 12:00:00', 'break_start'),
(3, '2024-12-02', '2024-12-02 12:30:00', 'break_end'),
(3, '2024-12-02', '2024-12-02 14:00:00', 'check_out'),

(3, '2024-12-03', '2024-12-03 09:00:00', 'check_in'),
(3, '2024-12-03', '2024-12-03 12:00:00', 'break_start'),
(3, '2024-12-03', '2024-12-03 12:30:00', 'break_end'),
(3, '2024-12-03', '2024-12-03 13:00:00', 'break_start'),
(3, '2024-12-03', '2024-12-03 13:30:00', 'break_end'),
(3, '2024-12-03', '2024-12-03 14:00:00', 'check_out'),

(3, '2024-12-04', '2024-12-04 08:59:00', 'check_in'),
(3, '2024-12-04', '2024-12-04 12:00:00', 'break_start'),
(3, '2024-12-04', '2024-12-04 12:30:00', 'break_end'),
(3, '2024-12-04', '2024-12-04 23:00:00', 'check_out'),

-- uge 48 for emilie
(3, '2024-11-25', '2024-11-25 07:00:00', 'check_in'),
(3, '2024-11-25', '2024-11-25 12:00:00', 'break_start'),
(3, '2024-11-25', '2024-11-25 12:30:00', 'break_end'),
(3, '2024-11-25', '2024-11-25 22:00:00', 'check_out'),

(3, '2024-11-26', '2024-11-26 07:00:00', 'check_in'),
(3, '2024-11-26', '2024-11-26 12:00:00', 'break_start'),
(3, '2024-11-26', '2024-11-26 12:30:00', 'break_end'),
(3, '2024-11-26', '2024-11-26 22:00:00', 'check_out'),

(3, '2024-11-27', '2024-11-27 07:00:00', 'check_in'),
(3, '2024-11-27', '2024-11-27 12:00:00', 'break_start'),
(3, '2024-11-27', '2024-11-27 12:30:00', 'break_end'),
(3, '2024-11-27', '2024-11-27 13:00:00', 'break_start'),
(3, '2024-11-27', '2024-11-27 13:30:00', 'break_end'),
(3, '2024-11-27', '2024-11-27 22:00:00', 'check_out'),

(3, '2024-11-28', '2024-11-28 7:59:00', 'check_in'),
(3, '2024-11-28', '2024-11-28 12:00:00', 'break_start'),
(3, '2024-11-28', '2024-11-28 12:30:00', 'break_end'),
(3, '2024-11-28', '2024-11-28 22:00:00', 'check_out');

INSERT INTO note (note_date, writer_id, recipient_id, written_note)
VALUES
    ('2024-12-04', 2, 2, 'Glemte at checke ind om morgen kl. 8'),
    ('2024-12-02', 1, 2, 'Ser man det');

-- Edited timestamps test og det virker!
insert into timelog (user_id, shift_date, event_time, event_type, edited_time) values
-- Day 1
(3, '2024-11-01', '2024-11-01 08:00:00', 'check_in', '2024-11-01 09:00:00'),
(3, '2024-11-01', '2024-11-01 12:00:00', 'break_start', null),
(3, '2024-11-01', '2024-11-01 12:30:00', 'break_end', null),
(3, '2024-11-01', '2024-11-01 17:00:00', 'check_out', null),
-- Day 2
(3, '2024-11-02', '2024-11-02 08:00:00', 'check_in', null),
(3, '2024-11-02', '2024-11-02 12:00:00', 'break_start', null),
(3, '2024-11-02', '2024-11-02 12:30:00', 'break_end', null),
(3, '2024-11-02', '2024-11-02 17:00:00', 'check_out', null);

insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(4, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(4, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(4, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(4, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
-- Day 2
(4, '2024-11-02', '2024-11-02 08:00:00', 'check_in'),
(4, '2024-11-02', '2024-11-02 12:00:00', 'break_start'),
(4, '2024-11-02', '2024-11-02 12:30:00', 'break_end'),
(4, '2024-11-02', '2024-11-02 17:00:00', 'check_out'),
-- Day 3
(4, '2024-11-03', '2024-11-03 08:00:00', 'check_in'),
(4, '2024-11-03', '2024-11-03 12:00:00', 'break_start'),
(4, '2024-11-03', '2024-11-03 12:30:00', 'break_end'),
(4, '2024-11-03', '2024-11-03 17:00:00', 'check_out'),
-- Day 4
(4, '2024-11-04', '2024-11-04 08:00:00', 'check_in'),
(4, '2024-11-04', '2024-11-04 12:00:00', 'break_start'),
(4, '2024-11-04', '2024-11-04 12:30:00', 'break_end'),
(4, '2024-11-04', '2024-11-04 17:00:00', 'check_out'),
-- Day 5
(4, '2024-11-05', '2024-11-05 08:00:00', 'check_in'),
(4, '2024-11-05', '2024-11-05 12:00:00', 'break_start'),
(4, '2024-11-05', '2024-11-05 12:30:00', 'break_end'),
(4, '2024-11-05', '2024-11-05 17:00:00', 'check_out'),
-- Day 6
(4, '2024-11-06', '2024-11-06 08:00:00', 'check_in'),
(4, '2024-11-06', '2024-11-06 12:00:00', 'break_start'),
(4, '2024-11-06', '2024-11-06 12:30:00', 'break_end'),
(4, '2024-11-06', '2024-11-06 17:00:00', 'check_out'),
-- Day 7
(4, '2024-11-07', '2024-11-07 08:00:00', 'check_in'),
(4, '2024-11-07', '2024-11-07 12:00:00', 'break_start'),
(4, '2024-11-07', '2024-11-07 12:30:00', 'break_end'),
(4, '2024-11-07', '2024-11-07 17:00:00', 'check_out'),
-- Day 8
(4, '2024-11-08', '2024-11-08 08:00:00', 'check_in'),
(4, '2024-11-08', '2024-11-08 12:00:00', 'break_start'),
(4, '2024-11-08', '2024-11-08 12:30:00', 'break_end'),
(4, '2024-11-08', '2024-11-08 17:00:00', 'check_out'),
-- Day 9
(4, '2024-11-09', '2024-11-09 08:00:00', 'check_in'),
(4, '2024-11-09', '2024-11-09 12:00:00', 'break_start'),
(4, '2024-11-09', '2024-11-09 12:30:00', 'break_end'),
(4, '2024-11-09', '2024-11-09 17:00:00', 'check_out'),
-- Day 10
(4, '2024-12-28', '2024-12-28 09:00:00', 'check_in'),
(4, '2024-12-28', '2024-12-28 12:00:00', 'break_start'),
(4, '2024-12-28', '2024-12-28 15:30:00', 'break_end'),
(4, '2024-12-28', '2024-12-28 16:00:00', 'check_out');

insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(5, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(5, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(5, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(5, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(6, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(6, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(6, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(6, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(7, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(7, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(7, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(7, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(8, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(8, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(8, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(8, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(9, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(9, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(9, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(9, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(10, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(10, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(10, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(10, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(11, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(11, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(11, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(11, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
(12, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(12, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(12, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(12, '2024-11-01', '2024-11-01 17:00:00', 'check_out');
insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(13, '2025-01-08', '2025-01-08 08:00:00', 'check_in'),
(13, '2025-01-08', '2025-01-08 12:00:00', 'break_start'),
(13, '2025-01-08', '2025-01-08 12:30:00', 'break_end'),
(13, '2025-01-08', '2025-01-08 17:00:00', 'check_out');
insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(12, '2025-01-08', '2025-01-08 08:00:00', 'check_in'),
(12, '2025-01-08', '2025-01-08 12:00:00', 'break_start'),
(12, '2025-01-08', '2025-01-08 12:30:00', 'break_end'),
(12, '2025-01-08', '2025-01-08 17:00:00', 'check_out');
insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(11, '2025-01-08', '2025-01-08 08:00:00', 'check_in'),
(11, '2025-01-08', '2025-01-08 12:00:00', 'break_start'),
(11, '2025-01-08', '2025-01-08 12:30:00', 'break_end'),
(11, '2025-01-08', '2025-01-08 17:00:00', 'check_out');
insert into timelog (user_id, shift_date, event_time, event_type) values
-- Day 1
(10, '2025-01-08', '2025-01-08 08:00:00', 'check_in'),
(10, '2025-01-08', '2025-01-08 12:00:00', 'break_start'),
(10, '2025-01-08', '2025-01-08 12:30:00', 'break_end'),
(10, '2025-01-08', '2025-01-08 17:00:00', 'check_out');

-- Til at populate notes, men fordi den er joined med user, så behøver ikke alle informationen at blive skrevet her :) querien for det er forneden
INSERT INTO note (note_date, writer_id, recipient_id, written_note)
VALUES
    ('2024-11-01', 1, 2, 'ændret tid'),
    ('2024-12-02', 3, 3, 'glemte at checke ind, pls Brain ikke vær sur! :(');


-- Denne kan ikke bruge edit_time i nu
-- Dette er en query der fyller weekly_timelog, programmet skal nok køre denne ugeligt/dagligt hvis det er noget vi finder nødvendigt
insert into weekly_timelog (user_id, full_name, week_start, total_hours_worked)
select
    check_in_logs.user_id,
    u.full_name,
    date_sub(
            date(coalesce(check_in_logs.edited_time, check_in_logs.event_time)),
        interval DAYOFWEEK(date(coalesce(check_in_logs.edited_time, check_in_logs.event_time))) - 1 day
    ) as week_start,
    sec_to_time(
            sum(
                    time_to_sec(
                            timediff(
                                    coalesce(check_out_logs.edited_time, check_out_logs.event_time),
                                    coalesce(check_in_logs.edited_time, check_in_logs.event_time)
                            )
                    )
                        - ifnull((
                                     select SUM(TIME_TO_SEC(TIMEDIFF(
                                             coalesce(break_end_logs.edited_time, break_end_logs.event_time),
                                             coalesce(break_start_logs.edited_time, break_start_logs.event_time)
                                                            )))
                                     from timelog break_start_logs
                                              left join timelog break_end_logs
                                                        on break_start_logs.user_id = break_end_logs.user_id
                                                            and break_start_logs.shift_date = break_end_logs.shift_date
                                     where break_start_logs.event_type = 'break_start'
                                       and break_end_logs.event_type = 'break_end'
                                       and break_start_logs.user_id = check_in_logs.user_id
                                       and break_start_logs.shift_date = check_in_logs.shift_date
                                 ), 0)
            )
    ) as total_hours_worked
from timelog check_in_logs
         left join timelog check_out_logs
                   on check_in_logs.user_id = check_out_logs.user_id
                       and check_in_logs.shift_date = check_out_logs.shift_date
                       and check_out_logs.event_type = 'check_out'
         join user u on check_in_logs.user_id = u.user_id
where check_in_logs.event_type = 'check_in'
group by check_in_logs.user_id, week_start, u.full_name;


-- Dette er for populate notes
SELECT
    notes.note_id,
    notes.note_date,
    notes.written_note,
    writer.user_id as writer_id,
    writer.full_name as writer_name,
    recipient.user_id as recipient_id,
    (notes.writer_id = notes.recipient_id) as self_note
from notes
         join user as writer on notes.writer_id = writer.user_id
         join user as recipient on notes.recipient_id = recipient.user_id;








