/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

/**
 *
 * @author Brad
 */
public class JPARunner extends DbRunner {

    public JPARunner(DataSource ds, int icptCount) {
        super(ds, icptCount);
    }

    @Override
    public void run() {
        try {
            doJPAWithOneTransaction();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void doJPAWithOneTransaction() {
        final String PERSISTENCE_UNIT_NAME = "intercept";
        EntityManagerFactory factory;
        factory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        EntityManager em = factory.createEntityManager();

        em.getTransaction().begin();
        for (int i = 0; i < super.icptCount; i++) {
            Intercept icpt = InterceptGenerator.createIntercept();
            em.persist(icpt);
        }

        em.getTransaction().commit();

        em.close();
    }

}
