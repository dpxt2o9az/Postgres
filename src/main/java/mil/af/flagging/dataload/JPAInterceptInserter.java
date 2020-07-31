/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class JPAInterceptInserter extends InterceptInserter {

    private final InterceptGenerator g;
    
    public JPAInterceptInserter(DataSource ds, InterceptGenerator g, int icptCount) {
        super(ds, icptCount);
        this.g = g;
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

        EntityTransaction tx = em.getTransaction();

        for (int i = 0; i < super.icptCount; i++) {
            tx.begin();
            Intercept icpt = g.createIntercept();
            em.persist(icpt);
            tx.commit();
        }

        em.close();
    }

}
