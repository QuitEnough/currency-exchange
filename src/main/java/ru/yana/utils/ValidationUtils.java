package ru.yana.utils;

import java.math.BigDecimal;
import java.util.Currency;

import ru.yana.dto.CurrencyRequestDto;
import ru.yana.dto.ExchangeRateRequestDto;
import ru.yana.dto.ExchangeRequestDto;
import ru.yana.exception.InvalidParameterException;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    private static Set<String> currencyCodes;
    public static void validate(CurrencyRequestDto currencyRequestDto) {
        String code = currencyRequestDto.getCode();
        String name = currencyRequestDto.getName();
        String sign = currencyRequestDto.getSign();

        if (code == null || code.isBlank()) {
            throw new InvalidParameterException("Missing parameter - code");
        }

        if (name == null || name.isBlank()) {
            throw new InvalidParameterException("Missing parameter - name");
        }

        if (sign == null || sign.isBlank()) {
            throw new InvalidParameterException("Missing parameter - sign");
        }
    }

    public static void validate(ExchangeRateRequestDto exchangeRateRequestDto) {
        String baseCurrencyCode = exchangeRateRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRateRequestDto.getTargetCurrencyCode();
        BigDecimal rate = exchangeRateRequestDto.getRate();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - baseCurrencyCode");
        }

        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - targetCurrencyCode");
        }

        if (rate == null) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        if (rate.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidParameterException("Invalid parameter - rate must be non-negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    public static void validate(ExchangeRequestDto exchangeRequestDto) {
        String baseCurrencyCode = exchangeRequestDto.getBaseCurrencyCode();
        String targetCurrencyCode = exchangeRequestDto.getTargetCurrencyCode();
        BigDecimal amount = exchangeRequestDto.getAmount();

        if (baseCurrencyCode == null || baseCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - from");
        }

        if (targetCurrencyCode == null || targetCurrencyCode.isBlank()) {
            throw new InvalidParameterException("Missing parameter - to");
        }

        if (amount == null) {
            throw new InvalidParameterException("Missing parameter - amount");
        }

        if (amount.compareTo(new BigDecimal("0.0")) < 0) {
            throw new InvalidParameterException("Invalid parameter - Amount must be non-negative");
        }

        validateCurrencyCode(baseCurrencyCode);
        validateCurrencyCode(targetCurrencyCode);
    }

    public static void validateCurrencyCode(String code) {
        if (code.length() != 3) {
            throw new InvalidParameterException("Currency code must contain exactly 3 letters");
        }

        if (currencyCodes == null) {
            Set<Currency> currencies = Currency.getAvailableCurrencies();
            currencyCodes = currencies.stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());
        }

        if (!currencyCodes.contains(code)) {
            throw new InvalidParameterException("Currency code must be in ISO 4217 format");
        }
    }

}
