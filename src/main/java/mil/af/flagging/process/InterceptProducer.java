/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.process;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public class InterceptProducer extends Producer<Intercept> {

    private final Queue<Intercept> intercepts;
    
    public InterceptProducer(BlockingQueue<Intercept> queue, Queue<Intercept> icpts, Intercept poison) {
        super(queue, poison);
        this.intercepts = icpts;
    }
    
    @Override
    public Intercept emit() {
        return intercepts.poll();
    }

}
