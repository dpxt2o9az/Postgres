/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.util.ArrayList;
import mil.af.flagging.dataminer.model.Signal;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.EnvSequence;

/**
 *
 * @author Brad
 */
public class ModeGenerator implements Callable<ModeGeneratorWorkUnit> {

    private final Logger LOG;
    private final ModeGeneratorWorkUnit wu;

    ModeGenerator(ModeGeneratorWorkUnit workUnit) {
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
        try {
            LOG.log(Level.FINE, "build modes started for {0}", wu.notation);
            if ((!wu.envRangesPRI.isEmpty() || !wu.envRangesJPR.isEmpty()) && wu.baselineRuns > 1) {
                LOG.log(Level.SEVERE, "preserved environment exists for {0} and cannot use baseline building. please remove baseline building option.", wu.notation);
                return;
            }

            for (int run = 0; run < wu.baselineRuns; run++) {
                List<EnvRange> generatePRIs = generateRanges(wu.envRangesPRI, "PRI", signals);
                wu.envRangesPRI = mergeRangeLists("PRI", wu.envRangesPRI, generatePRIs);
                
                List<EnvRange> generateJPRs = generateRanges(wu.envRangesJPR, "JPR", signals);
                wu.envRangesJPR = mergeRangeLists("JPR", wu.envRangesJPR, generateJPRs);
                
                if (generatePRIs.isEmpty() || generateJPRs.isEmpty()) {
                    // NOTE: break; should work here instead?
                    run = wu.baselineRuns;
                }
            }
            if (wu.baselineRuns > 1) {
                List<EnvRange> tmp = new ArrayList<>();
                tmp.addAll(wu.envRangesPRI);
                wu.envRangesPRI = mergeRangeLists("PRI", wu.envRangesPRI, tmp);
                tmp.clear();
                
                tmp.addAll(wu.envRangesJPR);
                wu.envRangesJPR = mergeRangeLists("JPR", wu.envRangesJPR, tmp);
            }
            
            for (Signal signal : signals) {
                if (signal.modeId < 0) {
                    EnvRange[] priSeq = signal.getPriSeq(wu.envRangesPRI, wu.envRangesJPR);
                    if (priSeq != null) {
                        EnvSequence signalSeq = findPriSequence(priSeq, wu.envSequences);
                        if (!wu.envSequences.contains(signalSeq)) {
                            wu.envSequences.add(signalSeq);
                        }
                    }
                }
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "ERROR DURING BUILD MODES", e);
            throw new RuntimeException(e);
        }
    }

    private List<EnvRange> generateRanges(SortedSet<EnvRange> envRangesPRI, String pri, Collection<Signal> signals) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private SortedSet<EnvRange> mergeRangeLists(String pri, SortedSet<EnvRange> envRangesPRI, List<EnvRange> generatePRIs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private EnvSequence findPriSequence(EnvRange[] priSeq, Set<EnvSequence> envSequences) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
