/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

/**
 *
 * @author Brad
 */
public class Signal {

    public long id;
    public long modeId;
    public String elnot;
    public String mt;
    public BigDecimal rf;
    public BigDecimal pd;
    public BigDecimal sp;
    public BigDecimal ir;
    public String st;
    public List<BigDecimal> pri;
    
    public Signal(Long interceptId, Long modeId, BigDecimal rf, BigDecimal pd, BigDecimal sp, BigDecimal ir, String scanType, BigDecimal[] pris, String modType) {
        this.id = interceptId;
        this.modeId = modeId;
        this.rf = rf;
        this.pd = pd;
        this.sp = sp;
        this.ir = ir;
        this.mt = modType;
        this.st = scanType;
        this.pri = Arrays.asList(pris);
    }

    public EnvRange[] getPriSeq(SortedSet<EnvRange> envRangesPRI, SortedSet<EnvRange> envRangesJPR) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
