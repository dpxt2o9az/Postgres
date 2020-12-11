/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.Signal;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.CountDownLatch;
import mil.af.flagging.dataminer.model.EnvModeMap;
import mil.af.flagging.dataminer.model.EnvRange;
import mil.af.flagging.dataminer.model.EnvSequence;
import mil.af.flagging.dataminer.model.Parameter;
import mil.af.flagging.dataminer.model.TuningParameter;

/**
 *
 * @author Brad
 */
public class ModeGeneratorWorkUnit {

    public CountDownLatch latch;
    public String notation;
    public int baselineRuns;
    public int minModeInts;
    public SortedSet<EnvRange> envRangesPRI;
    public SortedSet<EnvRange> envRangesJPR;
    public SortedSet<EnvRange> envRangesRF;
    public SortedSet<EnvRange> envRangesPD;
    public SortedSet<EnvRange> envRangesSP;
    public SortedSet<EnvRange> envRangesIR;
    public Map<String, TuningParameter> tuning;
    public Collection<Signal> signals;
    public Collection<Signal> cwSignals;
    public Map<Parameter, Collection<EnvRange>> envRanges;
    public Set<EnvSequence> envSequences;
    public Collection<EnvModeMap> envModeMaps;

}
