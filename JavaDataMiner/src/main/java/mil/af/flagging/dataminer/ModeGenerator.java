/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.Signal;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class ModeGenerator implements Callable<ModeGeneratorWorkUnit>{
    private final Logger LOG;
    private final ModeGeneratorWorkUnit wu;
    
    ModeGenerator (ModeGeneratorWorkUnit workUnit) { 
        this.LOG = Logger.getLogger(workUnit.notation + "-" + ModeGenerator.class.getName());
        this.wu = workUnit;
    }
    
    @Override
    public ModeGeneratorWorkUnit call() {
        
        LOG.log(Level.INFO, "starting run {0}", wu.notation);
        LOG.log(Level.INFO, "building modes for {0}", wu.notation);
        
        buildModes(wu.signals);
        if (!wu.cwSignals.isEmpty()) {
            LOG.log(Level.INFO, "building modes for {0} constants", wu.notation);
            buildModes(wu.cwSignals);
        }
        
        wu.latch.countDown();
        LOG.log(Level.INFO, "ending run {0}", wu.notation);
        
        return this.wu;
    }

    private void buildModes(Collection<Signal> signals) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
