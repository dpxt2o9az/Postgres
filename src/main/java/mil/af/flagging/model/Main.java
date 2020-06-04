package mil.af.flagging.model;

import java.sql.*;
import javax.persistence.*;

public class Main {

    static {
        InterceptGenerator.seed(25L);
    }

    public static void main(String[] args) throws Exception {
        doJDBCWithoutAutoCommit();
    }

    private static void doJPAWithOneTransaction() {
        final String PERSISTENCE_UNIT_NAME = "intercept";
        EntityManagerFactory factory;
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();
        for (int i = 0; i < 1000; i++) {
            em.persist(InterceptGenerator.createIntercept());
        }

        em.getTransaction().commit();

        em.close();
    }

    private static void doJDBCWithoutAutoCommit() throws SQLException {
        try (Connection c = DriverManager.getConnection("jdbc:postgresql://maelstrom.lan:32769/postgres", "postgres", "")) {
            c.setAutoCommit(false);
            SchemaCreator.dewIt(c);
            SingleRecordWriter db = new SingleRecordWriter(c);
            for (int i = 0; i < 1000; i++) {
                db.writeRecord(InterceptGenerator.createIntercept());
            }
            c.commit();
        }
    }
}
