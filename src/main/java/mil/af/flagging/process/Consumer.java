/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.process;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public abstract class Consumer<E> implements Runnable {

    private static final Logger LOG = Logger.getLogger(Consumer.class.getName());

    protected final BlockingQueue<E> queue;
    protected final E poison;

    public Consumer(BlockingQueue<E> queue, E poisonPill) {
        this.queue = queue;
        this.poison = poisonPill;
    }

    public abstract E consume() throws InterruptedException;

    @Override
    public void run() {
        System.out.println("consumer started");
        E current = null;
        while (current != poison) {
            try {
                 current = consume();
                LOG.log(Level.INFO, "processed {0}", current);
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "consumer interrupted", e);
                //throw new RuntimeException(e);
            }
        }
        System.out.println("consumer leaving");
    }
}
