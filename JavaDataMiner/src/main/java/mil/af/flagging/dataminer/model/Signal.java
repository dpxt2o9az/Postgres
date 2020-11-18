/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer.model;

import java.util.List;

/**
 *
 * @author Brad
 */
public class Signal {

    public long id;
    public long modeId;
    public String elnot;
    public String mt;
    public Double rf;
    public Double pd;
    public Double sp;
    public Double ir;
    public String st;
    public List<Double> pri;
}
