package repository;

import config.DbConnector;
import entity.Client;
import exception.NoUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientRepository implements Repository<Client> {

    private static final Logger log = LoggerFactory.getLogger(ClientRepository.class);
    private final Connection connector = new DbConnector().getConnection();

    @Override
    public Client getById(int id) {
        String select = "SELECT * FROM CLIENTS WHERE id = ?";
        try (PreparedStatement updateClient = connector.prepareStatement(select)) {
            connector.setAutoCommit(false);
            updateClient.setInt(1, id);
            ResultSet resultSet = updateClient.executeQuery();
            while (resultSet.next()) {
                int fetchedId = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String city = resultSet.getString("city");
                String address = resultSet.getString("address");
                return new Client(fetchedId, firstName, lastName, city, address);
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
        }
        throw new NoUserException("User with id: [" + id + "] does not exists in database.");
    }

    @Override
    public List<Client> getAll() {
        List<Client> result = new ArrayList<>();
        String select = "SELECT * FROM CLIENTS";
        try (Statement statement = connector.createStatement()) {
            ResultSet resultSet = statement.executeQuery(select);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String city = resultSet.getString("city");
                String address = resultSet.getString("address");
                result.add(new Client(id, firstName, lastName, city, address));
            }
        } catch (SQLException exception) {
            log.error(exception.getMessage());
        }
        return result;
    }

    @Override
    public void update(Client client) {
        String update = """
                UPDATE clients SET
                first_name = ?,
                last_name = ?,
                address = ?,
                city = ?
                WHERE id = ?""";

        try (PreparedStatement updateClient = connector.prepareStatement(update)) {
            connector.setAutoCommit(false);
            updateClient.setString(1, client.getFirstName());
            updateClient.setString(2, client.getLastName());
            updateClient.setString(3, client.getAddress());
            updateClient.setString(4, client.getCity());
            updateClient.setInt(5, client.getId());
            updateClient.executeUpdate();
            connector.commit();
        } catch (SQLException e) {
            rollbackAndLogException(e);
        }
    }

    @Override
    public void delete(Client client) {
        String delete = """
                DELETE FROM clients
                WHERE id = ?""";
        try (PreparedStatement deleteClient = connector.prepareStatement(delete)) {
            connector.setAutoCommit(false);
            deleteClient.setInt(1, client.getId());
            deleteClient.executeUpdate();
            connector.commit();
        } catch (SQLException e) {
            rollbackAndLogException(e);
        }
    }

    @Override
    public void insert(Client client) {
        String insert = """
                INSERT INTO clients (first_name, last_name, address, city)
                VALUES (?,?,?,?)
                """;
        try (PreparedStatement insertClient = connector.prepareStatement(insert)) {
            connector.setAutoCommit(false);
            insertClient.setString(1, client.getFirstName());
            insertClient.setString(2, client.getLastName());
            insertClient.setString(3, client.getAddress());
            insertClient.setString(4, client.getCity());
            insertClient.executeUpdate();
            connector.commit();
        } catch (SQLException e) {
            rollbackAndLogException(e);
        }
    }

    private void rollbackAndLogException(SQLException e) {
        try {
            connector.rollback();
            log.error("Rollback transaction, exception occurred. Message: [{}]", e.getMessage(), e);
        } catch (SQLException exception) {
            log.error("Exception occurred during rollback. Message: [{}]", e.getMessage(), e);
        }
    }
}
