-- Run as an authorized MySQL administrator. Replace the example password.
CREATE USER IF NOT EXISTS 'stockpilot_reader'@'localhost' IDENTIFIED BY 'REPLACE_WITH_STRONG_PASSWORD';
GRANT SELECT ON inventory_stress_tester.* TO 'stockpilot_reader'@'localhost';
-- If the user already exists, change its password with ALTER USER instead.
