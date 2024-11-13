DROP DATABASE IF EXISTS `database`;
CREATE DATABASE `database`;
USE `database`;

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL,
    full_name varchar(50) NOT NULL,
    on_break boolean,
    clocked_in boolean,
    logged_in boolean,
    password VARCHAR(255) DEFAULT NULL, 
    role ENUM('employee', 'manager') NOT NULL,
    CHECK (role = 'employee' OR (role = 'manager' AND password IS NOT NULL))
    
);

CREATE TABLE timelog (
    log_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    shift_date DATE NOT NULL,
    event_time DATETIME NOT NULL,
    event_type ENUM('check_in', 'check_out', 'break_start', 'break_end') NOT NULL,
    edited_time DATETIME,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

CREATE TABLE timelog_edit (
    edit_id INT PRIMARY KEY AUTO_INCREMENT,
    log_id INT NOT NULL,
    edit_time DATETIME NOT NULL,
    new_event_time DATETIME NOT NULL,
    edit_type ENUM('check_in_edit', 'check_out_edit', 'break_start_edit', 'break_end_edit') NOT NULL,
    FOREIGN KEY (log_id) REFERENCES timelog(log_id)
);

CREATE TABLE weekly_timelog (
    weekly_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    week_start DATE NOT NULL,
    total_hours_worked TIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Insert users with hashed passwords
INSERT INTO users (username, full_name, password, role) VALUES
('brian', 'Brian Donatello', '$2y$10$T9rVlb8uDPfN8waXfdiBveFr9f8RI1YgpEvhDTnvHzQchD0Vngq1.', 'manager'), -- Example hashed password for 'admin'
('dorte', 'Dorte Johannes', NULL, 'employee'),
('emilie', 'Emilie Nutella', NULL, 'employee');
 
 
 -- test til flere breaks på en dag
INSERT INTO timelog (user_id, shift_date, event_time, event_type) VALUES
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

-- test til samme user men forskellige uger
INSERT INTO timelog (user_id, shift_date, event_time, event_type) VALUES
-- Day 1
(2, '2024-11-01', '2024-11-01 09:00:00', 'check_in'),
(2, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(2, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(2, '2024-11-01', '2024-11-01 14:00:00', 'check_out'),
-- Day 2
(2, '2024-12-02', '2024-11-02 09:00:00', 'check_in'),
(2, '2024-12-02', '2024-11-02 12:00:00', 'break_start'),
(2, '2024-12-02', '2024-11-02 12:30:00', 'break_end'),
(2, '2024-12-02', '2024-11-02 14:00:00', 'check_out');

-- standard stikprøve test
INSERT INTO timelog (user_id, shift_date, event_time, event_type) VALUES
-- Day 1
(3, '2024-11-01', '2024-11-01 08:00:00', 'check_in'),
(3, '2024-11-01', '2024-11-01 12:00:00', 'break_start'),
(3, '2024-11-01', '2024-11-01 12:30:00', 'break_end'),
(3, '2024-11-01', '2024-11-01 17:00:00', 'check_out'),
-- Day 2
(3, '2024-11-02', '2024-11-02 08:00:00', 'check_in'),
(3, '2024-11-02', '2024-11-02 12:00:00', 'break_start'),
(3, '2024-11-02', '2024-11-02 12:30:00', 'break_end'),
(3, '2024-11-02', '2024-11-02 17:00:00', 'check_out');

-- Denne kan ikke bruge edit_time i nu
-- Dette er en query der fyller weekly_timelog, programmet skal nok køre denne ugeligt/dagligt hvis det er noget vi finder nødvendigt
INSERT INTO weekly_timelog (user_id, week_start, total_hours_worked)
SELECT 
    check_in_logs.user_id,
    DATE_SUB(check_in_logs.shift_date, INTERVAL DAYOFWEEK(check_in_logs.shift_date) - 1 DAY) AS week_start,
    SEC_TO_TIME(
        SUM(
			-- beregning af "arbejdstid"
            TIME_TO_SEC(TIMEDIFF(check_out_logs.event_time, check_in_logs.event_time)) 
            -- beregning af break der bliver taget fra "arbejdstiden"
            - IFNULL((
                -- summen af breaks per dag lagt sammen
                SELECT SUM(TIME_TO_SEC(TIMEDIFF(break_end_logs.event_time, break_start_logs.event_time)))
                FROM timelog break_start_logs
                LEFT JOIN timelog break_end_logs 
                    ON break_start_logs.user_id = break_end_logs.user_id 
                    AND break_start_logs.shift_date = break_end_logs.shift_date
                -- DETTE KAN DANNE FEJL FIX DET, den checker lige nu kun for per dag basis "shift_date" der skal laves en failsafe hvis det overskrider kl. 24
                WHERE break_start_logs.event_type = 'break_start' 
                  AND break_end_logs.event_type = 'break_end'
                  AND break_start_logs.user_id = check_in_logs.user_id 
                  AND break_start_logs.shift_date = check_in_logs.shift_date
            ), 0)
        )
    ) AS total_hours_worked
FROM timelog check_in_logs
LEFT JOIN timelog check_out_logs 
    ON check_in_logs.user_id = check_out_logs.user_id 
    AND check_in_logs.shift_date = check_out_logs.shift_date 
    AND check_out_logs.event_type = 'check_out'
WHERE check_in_logs.event_type = 'check_in'
-- grupering af dataen for en hel uge, til en enkel user
GROUP BY check_in_logs.user_id, week_start;






