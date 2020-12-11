/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import java.util.List;
import java.util.Map;
import mil.af.flagging.dataminer.model.Parameter;

/**
 *
 * @author Brad
 */
public class AlgorithmDescription {

    private static final int DAYS_TO_PROCESS = 180;
    private static final int MIN_SIGNALS_TO_PROCESS = 1000;

    public static void main(String[] args) {
        List<String> listOfNotationsInResidue = fetchNotationsFromResidue();
        Map<String, Integer> notationCounts = fetchNotationCountsFromInterceptsYoungerThan(DAYS_TO_PROCESS);

        for (String notation : listOfNotationsInResidue) {
            final Integer notationCount = notationCounts.get(notation);
            if (notationCount != null && notationCount > MIN_SIGNALS_TO_PROCESS) {
                List<Signal> signals = fetchStandardResidueSignalsWithNotation(notation);
                List<Signal> cwSignals = fetchConstantResidueSignalsWithNotation(notation);
                List<Cluster> priClusters = fetchClustersForNotationAndParameter(notation, Parameter.PRI);
                List<Cluster> jprClusters = fetchClustersForNotationAndParameter(notation, Parameter.JPR);
                List<Cluster> newPriClusters = findNewClusters(priClusters, signals);
                List<Cluster> newJprClusters = findNewClusters(jprClusters, signals);
                List<Sequence> sequences = fetchSequencesFor(notation);
            }

        }
    }

    private static List<String> fetchNotationsFromResidue() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static Map<String, Integer> fetchNotationCountsFromInterceptsYoungerThan(int days) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static List<Signal> fetchStandardResidueSignalsWithNotation(String notation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static List<Signal> fetchConstantResidueSignalsWithNotation(String notation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static List<Cluster> fetchClustersForNotationAndParameter(String notation, Parameter parameter) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static List<Cluster> findNewClusters(List<Cluster> priClusters, List<Signal> signals) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private static List<Sequence> fetchSequencesFor(String notation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    static class Signal {

        String modType;

        String getModType() {
            return modType;
        }
    }

    static class Cluster {

        String elnot;
        Parameter parm;
        Double min;
        Double max;
    }

    static class Sequence {

    }
}
