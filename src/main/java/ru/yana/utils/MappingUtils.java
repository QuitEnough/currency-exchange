package ru.yana.utils;

import org.modelmapper.ModelMapper;
import ru.yana.dto.CurrencyRequestDto;
import ru.yana.dto.CurrencyResponseDto;
import ru.yana.dto.ExchangeRateResponseDto;
import ru.yana.entity.Currency;
import ru.yana.entity.ExchangeRate;

public class MappingUtils {

    private static final ModelMapper MODEL_MAPPER;

    static {
        MODEL_MAPPER = new ModelMapper();

        MODEL_MAPPER.typeMap(CurrencyRequestDto.class, Currency.class)
                .addMapping(CurrencyRequestDto::getName, Currency::setFullName);
    }

    public static Currency convertToEntity(CurrencyRequestDto currencyRequestDto) {
        return MODEL_MAPPER.map(currencyRequestDto, Currency.class);
    }

    public static CurrencyResponseDto convertToDto(Currency currency) {
        return MODEL_MAPPER.map(currency, CurrencyResponseDto.class);
    }

    public static ExchangeRateResponseDto convertToDto(ExchangeRate exchangeRate) {
        return MODEL_MAPPER.map(exchangeRate, ExchangeRateResponseDto.class);
    }
}
