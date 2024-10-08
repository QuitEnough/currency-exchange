package ru.yana.dao;

import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;
import ru.yana.DatabaseConnectionManager;
import ru.yana.entity.Currency;
import ru.yana.exception.DatabaseOperationException;
import ru.yana.exception.EntityExistsException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyDao implements CurrencyDao{
    @Override
    public Optional<Currency> findById(Long id) {
        final String query = "SELECT * FROM Currencies WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currency with id '" + id + "' from the database");
        }
        return Optional.empty();
    }

    @Override
    public List<Currency> findAll() {
        final String query = "SELECT * FROM Currencies";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();

            List<Currency> currencies = new ArrayList<>();

            while (resultSet.next()) {
                currencies.add(getCurrency(resultSet));
            }

            return currencies;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currencies from the database");
        }
    }

    @Override
    public Currency save(Currency entity) {
        final String query = "INSERT INTO Currencies (code, full_name, sign) VALUES (?, ?, ?) RETURNING *";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());

            ResultSet resultSet = statement.executeQuery();

            if (!resultSet.next()) {
                throw new DatabaseOperationException("Failed to save currency with code '" + entity.getCode() + "' to the database");
            }

            return getCurrency(resultSet);
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException exception = (SQLiteException) e;
                if (exception.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityExistsException("Currency with code '" + entity.getCode() + "' already exists");
                }
            }
            throw new DatabaseOperationException("Failed to save currency with code '" + entity.getCode() + "' already exists");
        }
    }

    @Override
    public Optional<Currency> update(Currency entity) {
        final String query = "UPDATE Currencies SET (code, full_name, sign) = (?, ?, ?) WHERE id = ? RETURNING *";

        try (Connection connection = DatabaseConnectionManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.setLong(4, entity.getId());

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currency with id '" + entity.getId() + "' from the database");
        }
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {
        final String query = "DELETE FROM Currencies WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id);
            statement.executeQuery();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currency with id '" + id + "' from the database");
        }

    }

    @Override
    public Optional<Currency> findByCode(String code) {
        final String query = "SELECT * FROM Currencies WHERE code = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return Optional.of(getCurrency(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to read currency with code '" + code + "' from the database");
        }
        return Optional.empty();
    }

    private static Currency getCurrency(ResultSet resultSet) throws SQLException {
        return new Currency(
                resultSet.getLong("id"),
                resultSet.getString("code"),
                resultSet.getString("full_name"),
                resultSet.getString("sign")
        );
    }
}
