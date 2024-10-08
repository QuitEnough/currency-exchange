package ru.yana;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.yana.dto.ErrorResponseDto;
import ru.yana.exception.DatabaseOperationException;
import ru.yana.exception.EntityExistsException;
import ru.yana.exception.InvalidParameterException;
import ru.yana.exception.NotFoundException;

import java.io.IOException;

import static jakarta.servlet.http.HttpServletResponse.*;

@WebFilter("/*")
public class ExceptionHandlingFilter extends HttpFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        try {
            super.doFilter(req, res, chain);
        } catch (DatabaseOperationException e) {
            writeErrorResponse(res, SC_INTERNAL_SERVER_ERROR, e);
        } catch (EntityExistsException e) {
            writeErrorResponse(res, SC_CONFLICT, e);
        } catch (InvalidParameterException e) {
            writeErrorResponse(res, SC_BAD_REQUEST, e);
        } catch (NotFoundException e) {
            writeErrorResponse(res, SC_NOT_FOUND, e);
        }
    }

    private void writeErrorResponse(HttpServletResponse response, int errorCode, RuntimeException e) throws IOException {
        response.setStatus(errorCode);

        objectMapper.writeValue(response.getWriter(), new ErrorResponseDto(
                errorCode,
                e.getMessage()
        ));
    }
}
