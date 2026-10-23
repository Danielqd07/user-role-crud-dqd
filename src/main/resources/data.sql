INSERT IGNORE INTO roles (name) VALUES
('ADMIN'),
('USER'),
('EDITOR');

INSERT IGNORE INTO users (username, email, password) VALUES
('daniel', 'daniel@email.com', '1234'),
('antonio', 'antonio@email.com', '1234');

INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
(1, 1),
(1, 2),
(2, 2);