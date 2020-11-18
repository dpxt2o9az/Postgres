/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.TuningConstraints;
import mil.af.flagging.dataminer.model.Signal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.PriSequence;
import mil.af.flagging.dataminer.model.TuningConstraints.Parameter;

/**
 *
 * @author Brad
 */
public class ModeGeneratorWorkUnit {

    public CountDownLatch latch;
    public String notation;
    public int baselineRuns;
    public int minModeInts;
    public TuningConstraints tuning;
    public Collection<Signal> signals;
    public Collection<Signal> cwSignals;
    public Map<Parameter, Collection<EnvRange>> envRanges;
    public Collection<PriSequence> envSequences;
    public Collection<EnvModeMap> envModeMaps;

}
