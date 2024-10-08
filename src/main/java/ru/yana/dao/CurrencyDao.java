package ru.yana.dao;

import ru.yana.entity.Currency;

import java.util.Optional;

public interface CurrencyDao extends CrudDao<Currency, Long> {

    Optional<Currency> findByCode(String code);

}
