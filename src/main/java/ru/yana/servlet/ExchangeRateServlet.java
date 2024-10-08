package ru.yana.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yana.dao.ExchangeRateDao;
import ru.yana.dao.JdbcExchangeRateDao;
import ru.yana.dto.ExchangeRateRequestDto;
import ru.yana.entity.ExchangeRate;
import ru.yana.exception.InvalidParameterException;
import ru.yana.exception.NotFoundException;
import ru.yana.service.ExchangeRateService;
import ru.yana.utils.MappingUtils;
import ru.yana.utils.ValidationUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static ru.yana.utils.MappingUtils.convertToDto;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {

    private final ExchangeRateDao exchangeRateDao = new JdbcExchangeRateDao();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCodes = req.getPathInfo().replaceFirst("/", "");

        if (currencyCodes.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided in an incorrect format");
        }

        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);

        ExchangeRate exchangeRate = exchangeRateDao.findByCodes(baseCurrencyCode, targetCurrencyCode)
                .orElseThrow(() -> new NotFoundException(
                   String.format("Exchange rate '%s' - '%s' - not found",
                   baseCurrencyCode, targetCurrencyCode)
                ));
        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String currencyCodes = req.getPathInfo().replaceFirst("/", "");

        if (currencyCodes.length() != 6) {
            throw new InvalidParameterException("Currency codes are either not provided in an incorrect format");
        }

        String baseCurrencyCode = currencyCodes.substring(0, 3);
        String targetCurrencyCode = currencyCodes.substring(3);

        ValidationUtils.validateCurrencyCode(baseCurrencyCode);
        ValidationUtils.validateCurrencyCode(targetCurrencyCode);

        String parameter = req.getReader().readLine();
        if (parameter == null || !parameter.contains("rate")) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        String rate = parameter.replace("rate=", "");
        if (rate.isBlank()) {
            throw new InvalidParameterException("Missing parameter - rate");
        }

        ExchangeRateRequestDto exchangeRateRequestDto =
                new ExchangeRateRequestDto(baseCurrencyCode, targetCurrencyCode, convertToNumber(rate));
        ExchangeRate exchangeRate = exchangeRateService.update(exchangeRateRequestDto);

        objectMapper.writeValue(resp.getWriter(), convertToDto(exchangeRate));
    }

    private static BigDecimal convertToNumber(String rate) {
        try {
            return BigDecimal.valueOf(Double.parseDouble(rate));
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Parameter rate must be a number");
        }
    }
}
