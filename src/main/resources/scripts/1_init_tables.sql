CREATE TABLE IF NOT EXISTS Currencies
(
    id        INTEGER PRIMARY KEY AUTOINCREMENT,
    code      TEXT CHECK ( length(code) == 3 ) NOT NULL,
    full_name TEXT                             NOT NULL,
    sign      TEXT                             NOT NULL,
    CONSTRAINT currencies_unique_code UNIQUE (code)
);

CREATE TABLE IF NOT EXISTS Exchange_rates
(
    id                 INTEGER PRIMARY KEY AUTOINCREMENT,
    base_currency_id   INTEGER NOT NULL,
    target_currency_id INTEGER NOT NULL,
    rate               REAL    NOT NULL,
    FOREIGN KEY (base_currency_id) REFERENCES Currencies (id),
    FOREIGN KEY (target_currency_id) REFERENCES Currencies (id),
    CONSTRAINT exchange_rates_unique_currency_ids UNIQUE (base_currency_id, target_currency_id)
);