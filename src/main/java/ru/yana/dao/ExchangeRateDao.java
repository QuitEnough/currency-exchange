package ru.yana.dao;

import ru.yana.entity.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDao extends CrudDao<ExchangeRate, Long> {

    Optional<ExchangeRate> findByCodes (String baseCurrencyCode, String targetCurrencyCode);

}
