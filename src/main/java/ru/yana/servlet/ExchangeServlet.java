package ru.yana.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yana.dao.ExchangeRateDao;
import ru.yana.dao.JdbcExchangeRateDao;
import ru.yana.dto.ExchangeRequestDto;
import ru.yana.dto.ExchangeResponseDto;
import ru.yana.exception.InvalidParameterException;
import ru.yana.service.ExchangeRateService;
import ru.yana.service.ExchangeService;
import ru.yana.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {

    private final ExchangeService exchangeService = new ExchangeService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");

        if (amount == null || amount.isBlank()) {
            throw new InvalidParameterException("Missing parameter - amount");
        }

        ExchangeRequestDto exchangeRequestDto =
                new ExchangeRequestDto(baseCurrencyCode, targetCurrencyCode, convertToNumber(amount));
        ValidationUtils.validate(exchangeRequestDto);

        ExchangeResponseDto exchangeResponseDto = exchangeService.exchange(exchangeRequestDto);
        objectMapper.writeValue(resp.getWriter(), exchangeResponseDto);
    }

    private static BigDecimal convertToNumber(String rate) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(rate));
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter amount must be a number");
        }
    }
}
