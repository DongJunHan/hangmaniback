 -- 18 +/- SELECT COUNT(*) FROM PUBLIC.BOARD;


-- 17135 +/- SELECT COUNT(*) FROM PUBLIC.STORE;
INSERT INTO STORE VALUES
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', '찬스센타백운점', '인천 부평구 신촌로7번길 3 1층', 37.484812, 126.708757, '1220983100', '032-523-4188', '01033138977', 'NULL', 'NULL', NULL, '인천', '부평구');
-- 5 +/- SELECT COUNT(*) FROM PUBLIC.LOTTO_TYPE;
INSERT INTO LOTTO_TYPE VALUES
(1, 'lotto645'),
(2, 'annual'),
(3, 'speetto500'),
(4, 'speetto1000'),
(5, 'speetto2000');

-- 582 +/- SELECT COUNT(*) FROM PUBLIC.WIN_HISTORY;
INSERT INTO WIN_HISTORY VALUES
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 291, 1),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 271, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 540, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 682, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 996, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 5, 41, 1),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 5, 41, 1);
-- 641 +/- SELECT COUNT(*) FROM PUBLIC.LOTTO_TYPE_HANDLE;
INSERT INTO LOTTO_TYPE_HANDLE VALUES
(1, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(2, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(3, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(4, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(5, '8c354eaa-ac2c-11ed-9b15-12ebd169e012');

-- 11 +/- SELECT COUNT(*) FROM PUBLIC.STORE_ATTACHMENT;
INSERT INTO STORE_ATTACHMENT VALUES
(1, '인천_부평구_세븐일레븐_부평애니점_로고', '로고', 99352, TIMESTAMP '2023-04-18 16:00:40.065358', '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(2, '인천_부평구_세븐일레븐 부평애니점_176462497257574', '176462497257574_0', 541778, TIMESTAMP '2023-04-18 16:00:43.191943', '8c354eaa-ac2c-11ed-9b15-12ebd169e012');
-- insert REPORT_TYPE
INSERT INTO REPORT_TYPE VALUES 
(1, 'closure'),
(2, 'update');
INSERT INTO USER VALUES('email@email.com', '22', 'M', 'id1');
INSERT INTO OAUTH VALUES('id1', 'K', '2e8Q9cLX4wXejiS0aFuQVQ==');

INSERT INTO BOARD VALUES
(1, 'string', 'string', 'id1', DATE '2023-03-04', DATE '2023-03-04', TRUE),
(2, 'string', 'string', 'id1', DATE '2023-03-05', DATE '2023-03-05', TRUE),
(3, 'string', 'string', 'id1', DATE '2023-03-05', DATE '2023-03-05', NULL),
(4, 'string', 'string11', 'id1', DATE '2023-03-05', DATE '2023-03-05', NULL);
CREATE ALIAS get_distance FOR "com.project.hangmani.util.Util.getDistance";