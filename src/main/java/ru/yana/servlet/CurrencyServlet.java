package ru.yana.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yana.dao.CurrencyDao;
import ru.yana.dao.JdbcCurrencyDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.yana.entity.Currency;
import ru.yana.exception.NotFoundException;
import ru.yana.utils.ValidationUtils;

import java.io.IOException;

import static ru.yana.utils.MappingUtils.convertToDto;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyDao currencyDao = new JdbcCurrencyDao();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String code = req.getPathInfo().replaceFirst("/", "");

        ValidationUtils.validateCurrencyCode(code);

        Currency currency = currencyDao.findByCode(code)
                .orElseThrow(() -> new NotFoundException("Currency with code '" + code + "' not found"));

        objectMapper.writeValue(resp.getWriter(), convertToDto(currency));
    }

}
