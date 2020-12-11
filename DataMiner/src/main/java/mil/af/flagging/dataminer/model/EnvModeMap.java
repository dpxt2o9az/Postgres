/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 *
 * @author Brad
 */
public class EnvModeMap {

    public Long id;
    public List<EnvModeMapping> mappings;
    public EnvSequence priSequence;
    public String MT;

    public EnvModeMap(long opModeId, String modType, EnvSequence sequence) {
        this.id = opModeId;
        this.MT = modType;
        this.priSequence = sequence;
    }

    public boolean stillWorthy(int minModeInts) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void fill(Map<Parameter, SortedSet<EnvRange>> envRanges) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public void fill(SortedSet<EnvRange> envRangesRF, SortedSet<EnvRange> envRangesPD, SortedSet<EnvRange> envRangesSP, SortedSet<EnvRange> envRangesIR) {
        throw new UnsupportedOperationException("not supported yet");
    }

    public void addMapping(EnvRange rfRange, EnvRange pdRange, EnvRange spRange, EnvRange irRange, String ST) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
}
