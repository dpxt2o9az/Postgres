/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.process;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import mil.af.flagging.model.Country;
import mil.af.flagging.model.CountryGenerator;
import mil.af.flagging.model.Environment;
import mil.af.flagging.model.Intercept;
import mil.af.flagging.model.InterceptGenerator;

/**
 *
 * @author Brad
 */
public class Test {

    private static final int ICPT_CNT = 1000000;

    public static void main(String[] args) throws Exception {

        final Intercept poison = new Intercept();
        poison.setWranglerId("POISON");

        BlockingQueue<Intercept> bq = new ArrayBlockingQueue<>(1000);
        Environment e = Environment.randomEnvironment(0L);
        InterceptGenerator gen = new InterceptGenerator(e);

        Collection<Intercept> icptCollection = gen.createInterceptsWithConflicts(ICPT_CNT, (int) (ICPT_CNT * 0.02));
        Queue<Intercept> icptQueue = new LinkedList<>(icptCollection);
        Producer<Intercept> prod = new InterceptProducer(bq, icptQueue, poison);

        ExecutorService exec = Executors.newFixedThreadPool(10);
        exec.execute(prod);
        for (int c = 0; c < 10; c++) {
            Consumer<Intercept> cons = new InterceptConsumer(bq, poison);
            exec.execute(cons);
        }

        exec.shutdown();

        boolean normalTermination = exec.awaitTermination(10, TimeUnit.SECONDS);
        if (!normalTermination) {
            System.err.println("blargh! executor was terminated abnormally");
        }
    }
}
