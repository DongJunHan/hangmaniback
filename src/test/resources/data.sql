 -- 18 +/- SELECT COUNT(*) FROM PUBLIC.BOARD;


-- 17135 +/- SELECT COUNT(*) FROM PUBLIC.STORE;
INSERT INTO STORE(storeUuid,storeName,storeAddress,storeLatitude,storeLongitude,
                   storeBizNo,storeTelNum,storeMobileNum,storesido,storesigugun) VALUES
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', '찬스센타백운점', '인천 부평구 신촌로7번길 3 1층',
 37.484812, 126.708757, '1220983100', '032-523-4188', '01033138977', '인천', '부평구') ON DUPLICATE KEY UPDATE
    storeUuid='8c354eaa-ac2c-11ed-9b15-12ebd169e012',storeName='찬스센타백운점',storeAddress='인천 부평구 신촌로7번길 3 1층',
    storeLatitude=37.484812, storeLongitude=126.708757,storesido='인천',storesigugun='부평구';

-- 5 +/- SELECT COUNT(*) FROM PUBLIC.LOTTO_TYPE;
INSERT INTO LOTTO_TYPE(lottoid, lottoname) VALUES
(1, 'lotto645'),
(2, 'annual'),
(3, 'speetto500'),
(4, 'speetto1000'),
(5, 'speetto2000') ON DUPLICATE KEY UPDATE lottoname = VALUES(lottoname);

-- 582 +/- SELECT COUNT(*) FROM PUBLIC.WIN_HISTORY;
INSERT INTO WIN_HISTORY(storeuuid,lottoid,winround,winrank) VALUES
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 291, 1),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 271, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 540, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 682, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 1, 996, 2),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 5, 41, 1),
('8c354eaa-ac2c-11ed-9b15-12ebd169e012', 5, 41, 1) ON DUPLICATE KEY UPDATE
 storeuuid=VALUES(storeuuid),lottoid = VALUES(lottoid), winround = VALUES(winround),winrank = VALUES(winrank);
-- 641 +/- SELECT COUNT(*) FROM PUBLIC.LOTTO_TYPE_HANDLE;
INSERT INTO LOTTO_TYPE_HANDLE(lottoid,storeuuid) VALUES
(1, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(2, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(3, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(4, '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(5, '8c354eaa-ac2c-11ed-9b15-12ebd169e012') ON DUPLICATE KEY UPDATE
    storeuuid=VALUES(storeuuid),lottoid = VALUES(lottoid);

-- 11 +/- SELECT COUNT(*) FROM PUBLIC.STORE_ATTACHMENT;
INSERT INTO STORE_ATTACHMENT(attachment_no,original_file_name,saved_file_name,storeuuid) VALUES
(1, '인천_부평구_세븐일레븐_부평애니점_로고', '로고', '8c354eaa-ac2c-11ed-9b15-12ebd169e012'),
(2, '인천_부평구_세븐일레븐 부평애니점_176462497257574', '176462497257574_0', '8c354eaa-ac2c-11ed-9b15-12ebd169e012')
 ON DUPLICATE KEY UPDATE original_file_name=VALUES(original_file_name),
    saved_file_name = VALUES(saved_file_name), storeuuid=VALUES(storeuuid);
-- insert REPORT_TYPE
INSERT INTO REPORT_TYPE VALUES
(1, 'closure'),
(2, 'update') ON DUPLICATE KEY UPDATE report_type=VALUES(report_type);

INSERT INTO USER VALUES('email@email.com', '22', 'M', 'id1') ON DUPLICATE KEY UPDATE email=VALUES(email), age=VALUES(age),
    gender=VALUES(gender);
INSERT INTO OAUTH VALUES('id1', 'K', '2e8Q9cLX4wXejiS0aFuQVQ==') ON DUPLICATE KEY UPDATE
    oauthtype=VALUES(oauthtype),oauthid=VALUES(oauthid);

INSERT INTO BOARD VALUES
(1, 'string', 'string', 'id1', DATE '2023-03-04', DATE '2023-03-04', TRUE),
(2, 'string', 'string', 'id1', DATE '2023-03-05', DATE '2023-03-05', TRUE),
(3, 'string', 'string', 'id1', DATE '2023-03-05', DATE '2023-03-05', NULL),
(4, 'string', 'string11', 'id1', DATE '2023-03-05', DATE '2023-03-05', NULL)
 ON DUPLICATE KEY UPDATE title=VALUES(title),content=VALUES(content),boardwriter=VALUES(boardwriter),
 createat=VALUES(createat),updateat=VALUES(updateat);
CREATE ALIAS IF NOT EXISTS get_distance FOR "com.project.hangmani.util.Util.getDistance";