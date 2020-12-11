/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.util.Set;

/**
 *
 * @author Brad
 */
public class EnvModeMapping {
    public Long id;
    public EnvRange RF;
    public EnvRange PD;
    public EnvRange SP;
    public EnvRange IR;
    public String ST;
    public Set<Signal> newSignals;
}
