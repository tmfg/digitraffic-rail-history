INSERT INTO `train` (
    `departure_date`, `train_number`, `version`, `deleted`, `fetch_date`, `json`
) VALUES
      ('2023-11-01', 12345, 1, b'0', NOW(), '{"key": "value1"}'),
      ('2023-11-01', 67890, 2, b'0', NOW(), '{"key": "value2"}'),
      ('2023-11-02', 12345, 3, b'0', NOW(), '{"key": "value3"}'),
      ('2023-11-03', 54321, 4, b'1', NOW(), '{"key": "value4"}'),
      ('2023-11-04', 98765, 5, b'1', NOW(), '{"key": "value5"}');