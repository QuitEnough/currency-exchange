package ru.yana.service;

import ru.yana.dao.CurrencyDao;
import ru.yana.dao.ExchangeRateDao;
import ru.yana.dao.JdbcCurrencyDao;
import ru.yana.dao.JdbcExchangeRateDao;
import ru.yana.dto.ExchangeRateRequestDto;
import ru.yana.entity.Currency;
import ru.yana.entity.ExchangeRate;
import ru.yana.exception.NotFoundException;

public class ExchangeRateService {

    private final CurrencyDao currencyDao = new JdbcCurrencyDao();
    private final ExchangeRateDao exchangeRateDao = new JdbcExchangeRateDao();

    public ExchangeRate save(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());
        return  exchangeRateDao.save(exchangeRate);
    }

    public ExchangeRate update(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        //ExchangeRate exchangeRate = initExchangeRate(exchangeRateRequestDto);
        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());

        return exchangeRateDao.update(exchangeRate)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Failed tp update exchange rate '%s' - '%s', no such exchange rate",
                                baseCurrencyCode, targetCurrencyCode)
                ));
    }

    /*public static ExchangeRate initExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();

        Currency baseCurrency = currencyDao.findByCode(baseCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + baseCurrencyCode + "' not found"));
        Currency targetCurrency = currencyDao.findByCode(targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + targetCurrencyCode + "' not found"));

        return new ExchangeRate(baseCurrency, targetCurrency, exchangeRateRequestDto.getRate());
    }*/
}
