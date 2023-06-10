-- used in ToursAdminControllerTest::deleteTour test
INSERT INTO tour (`id`, `name`, `legend`, `description`, `restrictions`, `meeting_point`, `type`, `duration`, `price`, `lang`, `deleted`)
VALUES (123L, 'aaaa', 'eeee', 'kkkk', 'mmmm', 'VN', 'CLASSIC', 5, '2500 CZK', 'CZ', false),
-- used in ReservationsAdminControllerTest::testUpdateReservation test
       (124L, 'bbbb', 'cccc', 'hhhh', 'llll', 'MS', 'CHILDREN', 3, '1500 CZK', 'EN', false),

-- used in ToursControllerTest::getTourById test
       (125L, 'kkkk', 'yyyy', 'dddd', 'pppp', 'KN', 'UNDISCOVERED', 4, '2000 CZK', 'EN', false),

-- used in ReservationsControllerTest::testCreateReservation test
       (126L, 'rrrr', 'zzzz', 'wwww', 'bbbb', 'MS', 'THEMATIC', 4, '2500 CZK', 'CZ', false),

-- used in ToursAdminControllerTest::testRenewTour test
       (127L, 'tour to renew', 'tour to renew legend', 'tour to renew desc', 'tour to renew restr', 'MS', 'THEMATIC', 4, '2500 CZK', 'CZ', true);

INSERT INTO reservation (`id`, `client_name`, `client_email`, `adults`, `children`, `datetime`, `tour_id`)
VALUES
-- used in ReservationsAdminControllerTest::testGetReservationById test
    (201L, 'Petr', 'petr@gmail.com', 1, 2, 1647689161L, 123L),

-- used in ReservationsAdminControllerTest::testUpdateReservation test
    (202L, 'Jan', 'honza@gmail.com', 2, 1, 1647689461L, 124L),

-- used in ReservationsAdminControllerTest::testDeleteReservation test
    (203L, 'Karel', 'karel@mail.com', 2, 0, 164769061L, 125L);
