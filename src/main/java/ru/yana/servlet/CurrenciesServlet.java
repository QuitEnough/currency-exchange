package ru.yana.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yana.dao.CurrencyDao;
import ru.yana.dao.JdbcCurrencyDao;
import ru.yana.dto.CurrencyRequestDto;
import ru.yana.dto.CurrencyResponseDto;
import ru.yana.entity.Currency;
import ru.yana.utils.MappingUtils;
import ru.yana.utils.ValidationUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static jakarta.servlet.http.HttpServletResponse.SC_CREATED;
import static ru.yana.utils.MappingUtils.convertToEntity;
import static ru.yana.utils.MappingUtils.convertToDto;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {

    private final CurrencyDao currencyDao = new JdbcCurrencyDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Currency> currencies = currencyDao.findAll();

        List<CurrencyResponseDto> currenciesDto = currencies.stream()
                .map(MappingUtils::convertToDto)
                .collect(Collectors.toList());

        objectMapper.writeValue(resp.getWriter(), currenciesDto);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        CurrencyRequestDto currencyRequestDto = new CurrencyRequestDto(name, code, sign);

        ValidationUtils.validate(currencyRequestDto);

        Currency currency = currencyDao.save(convertToEntity(currencyRequestDto));

        resp.setStatus(SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), convertToDto(currency));
    }
}
