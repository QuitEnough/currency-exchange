INSERT INTO Currencies (code, full_name, sign)
VALUES ('USD', 'US Dollar', '$'),
       ('EUR', 'EURO', '€'),
       ('CZK', 'Czech Koruna', 'Kč'),
       ('GBP', 'Pound Sterling', '£'),
       ('CNY', 'Yuan Renminbi', '¥'),
       ('JPY', 'Yen', '¥'),
       ('SEK', 'Swedish Krona', 'kr'),
       ('TRY', 'Turkish Lira', '₺');

INSERT INTO Exchange_rates (base_currency_id, target_currency_id, rate)
VALUES (1, 2, 0.92),
       (1, 3, 22.94),
       (1, 4, 0.79),
       (1, 5, 7.18),
       (1, 6, 148.04),
       (3, 8, 0.21),
       (4, 5, 9.11),
       (6, 2, 0.0062),
       (7, 2, 0.088);