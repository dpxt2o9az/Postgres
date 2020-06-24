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
public abstract class Producer<E> implements Runnable {

    private static final Logger LOG = Logger.getLogger(Producer.class.getName());

    protected final BlockingQueue<E> queue;
    private final E poison;

    public Producer(BlockingQueue<E> queue, E poisonPill) {
        this.queue = queue;
        this.poison = poisonPill;
    }

    public abstract E emit();

    @Override
    public void run() {
        System.out.println("producer started");
        while (true) {
            try {
                E element = emit();
                if (element != null) {
                    queue.put(element);
                } else {
                    break;
                }
            } catch (InterruptedException e) {
                LOG.log(Level.SEVERE, "producer thread interrupted", e);
                throw new RuntimeException(e);
            }
        }
        System.out.println("producer ending, adding poison");
        queue.add(poison);
        System.out.println("producer leaving");
    }

}
