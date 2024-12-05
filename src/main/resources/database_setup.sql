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
                      role enum('employee', 'manager') not null,
                      check (role = 'employee' OR (role = 'manager' and password is not null))
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
insert into user (username, full_name, password, role, clocked_in, on_break, logged_in) values
                                                                                            ('brian', 'Brian Donatello', '$2a$12$5Vo5DAvp0t6WT7UxnMqtAOhbh5wKjc09R153p2j.2acIBmohc5yvC', 'manager', true, true, false), -- Example hashed password for 'admin'
                                                                                            ('dorte', 'Dorte Johannes', null, 'employee', false, false, false),
                                                                                            ('emilie', 'Emilie Nutella', null, 'employee', true, false, false);

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
-- Day 1
(2, '2024-11-25', '2024-11-25 07:00:00', 'check_in'),
(2, '2024-11-25', '2024-11-25 11:00:00', 'break_start'),
(2, '2024-11-25', '2024-11-25 12:00:00', 'break_end'),
(2, '2024-11-25', '2024-11-25 18:00:00', 'check_out'),
-- Day 2
(2, '2024-11-26', '2024-11-26 09:00:00', 'check_in'),
(2, '2024-11-26', '2024-11-26 12:00:00', 'break_start'),
(2, '2024-11-26', '2024-11-26 12:30:00', 'break_end'),
(2, '2024-11-26', '2024-11-26 16:00:00', 'check_out'),

(2, '2024-11-27', '2024-11-27 08:00:00', 'check_in'),
(2, '2024-11-27', '2024-11-27 11:30:00', 'break_start'),
(2, '2024-11-27', '2024-11-27 12:00:00', 'break_end'),
(2, '2024-11-27', '2024-11-27 15:30:00', 'check_out'),

(2, '2024-11-28', '2024-11-28 10:00:00', 'check_in'),
(2, '2024-11-28', '2024-11-28 13:00:00', 'check_out'),

(2, '2024-11-29', '2024-11-29 07:00:00', 'check_in'),
(2, '2024-11-29', '2024-11-29 10:00:00', 'break_start'),
(2, '2024-11-29', '2024-11-29 10:30:00', 'break_end'),
(2, '2024-11-29', '2024-11-29 18:00:00', 'check_out'),

(2, '2024-11-30', '2024-11-30 09:00:00', 'check_in'),
(2, '2024-11-30', '2024-11-30 12:00:00', 'break_start'),
(2, '2024-11-30', '2024-11-30 12:30:00', 'break_end'),
(2, '2024-11-30', '2024-11-30 14:00:00', 'check_out'),

(2, '2024-12-01', '2024-12-01 09:00:00', 'check_in'),
(2, '2024-12-01', '2024-12-01 12:00:00', 'break_start'),
(2, '2024-12-01', '2024-12-01 12:30:00', 'break_end'),
(2, '2024-12-01', '2024-12-01 14:00:00', 'check_out'),

(2, '2024-12-11', '2024-12-11 08:13:00', 'check_in'),
(2, '2024-12-11', '2024-12-11 12:07:00', 'break_start'),
(2, '2024-12-11', '2024-12-11 12:29:00', 'break_end'),
(2, '2024-12-11', '2024-12-11 17:22:00', 'check_out'),

-- Sequence 2 (2024-12-12)
(2, '2024-12-12', '2024-12-12 08:45:00', 'check_in'),
(2, '2024-12-12', '2024-12-12 11:52:00', 'break_start'),
(2, '2024-12-12', '2024-12-12 12:18:00', 'break_end'),
(2, '2024-12-12', '2024-12-12 16:34:00', 'check_out'),

-- Sequence 3 (2024-12-13)
(2, '2024-12-13', '2024-12-13 07:38:00', 'check_in'),
(2, '2024-12-13', '2024-12-13 10:17:00', 'break_start'),
(2, '2024-12-13', '2024-12-13 10:38:00', 'break_end'),
(2, '2024-12-13', '2024-12-13 15:48:00', 'check_out'),

-- Sequence 4 (2024-12-14)
(2, '2024-12-14', '2024-12-14 08:07:00', 'check_in'),
(2, '2024-12-14', '2024-12-14 13:11:00', 'break_start'),
(2, '2024-12-14', '2024-12-14 13:40:00', 'break_end'),
(2, '2024-12-14', '2024-12-14 17:09:00', 'check_out'),

-- Sequence 5 (2024-12-15)
(2, '2024-12-15', '2024-12-15 08:19:00', 'check_in'),
(2, '2024-12-15', '2024-12-15 11:42:00', 'break_start'),
(2, '2024-12-15', '2024-12-15 12:02:00', 'break_end'),
(2, '2024-12-15', '2024-12-15 16:57:00', 'check_out'),

-- Sequence 6 (2024-12-16)
(2, '2024-12-16', '2024-12-16 09:10:00', 'check_in'),
(2, '2024-12-16', '2024-12-16 14:03:00', 'break_start'),
(2, '2024-12-16', '2024-12-16 14:28:00', 'break_end'),
(2, '2024-12-16', '2024-12-16 18:13:00', 'check_out'),

-- Sequence 7 (2024-12-17)
(2, '2024-12-17', '2024-12-17 08:31:00', 'check_in'),
(2, '2024-12-17', '2024-12-17 12:15:00', 'break_start'),
(2, '2024-12-17', '2024-12-17 12:37:00', 'break_end'),
(2, '2024-12-17', '2024-12-17 17:25:00', 'check_out'),

-- Sequence 8 (2024-12-18)
(2, '2024-12-18', '2024-12-18 07:55:00', 'check_in'),
(2, '2024-12-18', '2024-12-18 10:20:00', 'break_start'),
(2, '2024-12-18', '2024-12-18 10:48:00', 'break_end'),
(2, '2024-12-18', '2024-12-18 15:36:00', 'check_out');



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

-- Til at populate notes, men fordi den er joined med user, så behøver ikke alle informationen at blive skrevet her :) querien for det er forneden
INSERT INTO note (note_date, writer_id, recipient_id, written_note)
VALUES
    ('2024-11-01', 1, 2, 'ændret tid'),
    ('2024-12-02', 3, 3, 'glemte at checke ind, pls Brain ikke vær sur! :(');


-- Denne kan ikke bruge edit_time i nu
-- Dette er en query der fyller weekly_timelog, programmet skal nok køre denne ugeligt/dagligt hvis det er noget vi finder nødvendigt
insert into weekly_timelog (user_id, week_start, total_hours_worked)
select
    check_in_logs.user_id,
    -- coalesce gør sådan at den første ikke NULL bliver anvendt, i dette system checker den først for edited_time derefter event_time
    date_sub(
            date(coalesce(check_in_logs.edited_time, check_in_logs.event_time)),
        interval DAYOFWEEK(date(coalesce(check_in_logs.edited_time, check_in_logs.event_time))) - 1 day
    ) as week_start,
    sec_to_time(
            sum(
                -- Beregning af "arbejdstid"
                    time_to_sec(
                            timediff(
                                    coalesce(check_out_logs.edited_time, check_out_logs.event_time),
                                    coalesce(check_in_logs.edited_time, check_in_logs.event_time)
                            )
                    )
                        -- Beregning af break der bliver taget fra "arbejdstiden"
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
                   on	check_in_logs.user_id = check_out_logs.user_id
                       and	check_in_logs.shift_date = check_out_logs.shift_date
                       and	check_out_logs.event_type = 'check_out'
where check_in_logs.event_type = 'check_in'
group by check_in_logs.user_id, week_start;

-- Dette er for populate notes
SELECT
    note.note_id,
    note.note_date,
    note.written_note,
    writer.user_id as writer_id,
    writer.full_name as writer_name,
    recipient.user_id as recipient_id,
    (note.writer_id = note.recipient_id) as self_note
from note
         join user as writer on note.writer_id = writer.user_id
         join user as recipient on note.recipient_id = recipient.user_id;

