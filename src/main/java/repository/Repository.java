package repository;

import entity.Client;

import java.util.List;

public interface Repository<T> {

    T getById(int id);

    List<T> getAll();

    void update(Client client);

    void delete(Client client);

    void insert(Client client);

}
