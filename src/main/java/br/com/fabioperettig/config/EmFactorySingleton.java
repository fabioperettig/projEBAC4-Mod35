package br.com.fabioperettig.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EmFactorySingleton {

    private static volatile EntityManagerFactory emFactory;

    public static EntityManager getEntityManager() {

        if (emFactory == null) {
            synchronized (EntityManager.class) {
                if (emFactory == null) {

                    Dotenv dotenv = Dotenv.load();

                    String url = dotenv.get("DB_URL");
                    String user = dotenv.get("DB_USER");
                    String pass = dotenv.get("DB_PASS");

                    Map<String, String> dados = new HashMap<>();
                    dados.put("jakarta.persistence.jdbc.url", url);
                    dados.put("jakarta.persistence.jdbc.user", user);
                    dados.put("jakarta.persistence.jdbc.password", pass);

                    emFactory = Persistence.createEntityManagerFactory("projebac4", dados);

                }
            }
        }
        return emFactory.createEntityManager();
    }
}
