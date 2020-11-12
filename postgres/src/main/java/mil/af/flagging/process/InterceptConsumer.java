/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.process;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public class InterceptConsumer extends Consumer<Intercept> {

    
    public InterceptConsumer(BlockingQueue<Intercept> bq, Intercept poison) {
        super(bq, poison);
    }

    @Override
    public Intercept consume() throws InterruptedException {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(100));
        } catch(InterruptedException e) {
            // do nothing
        }
        return queue.take();
    }
    
}
