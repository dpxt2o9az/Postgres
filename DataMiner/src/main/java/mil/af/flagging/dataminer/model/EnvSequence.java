/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

/**
 *
 * @author Brad
 */
public class EnvSequence {

    public Long id;
    public EnvRange[] seq;

    public EnvSequence(Long id) {
        this.id = id;
    }

    public EnvSequence(EnvRange[] seq) {
        this.seq = seq;
    }

    public void addRange(EnvRange range) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public boolean matches(EnvRange[] ranges) {
        return false;
    }

    public boolean matches(EnvSequence sequence) {
        return false;
    }

}
