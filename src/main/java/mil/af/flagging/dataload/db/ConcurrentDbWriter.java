/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload.db;

import java.sql.Connection;
import java.util.concurrent.BlockingQueue;
import mil.af.flagging.model.Intercept;

/**
 *
 * @author Brad
 */
public class ConcurrentDbWriter implements Runnable {
    private final Intercept poison;
    private final BlockingQueue<Intercept> queue;
    private final Connection conn;
    
    public ConcurrentDbWriter(Connection conn, BlockingQueue<Intercept> q, Intercept poison) {
        this.conn = conn;
        this.queue = q;
        this.poison = poison;
    }
    
    public void run() {
        Intercept current = queue.peek();
        while(current != poison) {
            
        }
        
    }
}
