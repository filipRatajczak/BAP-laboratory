import entity.Client;

import repository.ClientRepository;
import repository.Repository;

public class Main {

    public static void main(String[] args) {

        Repository<Client> clientRepository = new ClientRepository();

        System.out.println(clientRepository.getAll());

    }
}
