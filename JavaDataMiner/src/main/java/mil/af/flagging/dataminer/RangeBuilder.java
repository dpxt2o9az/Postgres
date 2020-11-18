/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataminer;

import mil.af.flagging.dataminer.model.Range;
import mil.af.flagging.dataminer.model.Slope;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Brad
 */
public class RangeBuilder {

    private static final Logger LOG = Logger.getLogger(RangeBuilder.class.getName());

    private static final int DEFAULT_MIN_CLSTR_INTS = 5;
    private final SlopeBuilder slopeBuilder;

    public RangeBuilder(SlopeBuilder slopeBuilder) {
        this.slopeBuilder = slopeBuilder;
    }

    public List<Range> generateRanges(BigDecimal step_size, BigDecimal threshold_modifier, BigDecimal horizon) {
        return generateRanges(step_size, threshold_modifier, horizon, DEFAULT_MIN_CLSTR_INTS);
    }

    public List<Slope> generateSlopes() {

        return slopeBuilder.generateSlopes();
    }

    public List<Range> generateRanges(BigDecimal incr, BigDecimal threshold_modifier, BigDecimal horizon, int min_clstr_ints) {
        LOG.log(Level.FINER, "horizon: {0}", horizon);

        List<Slope> slopes = slopeBuilder.generateSlopes();

        List<Range> ranges = new ArrayList<>();
        if (slopes.isEmpty()) {
            return ranges;
        }

        final BigDecimal minClusterInts = new BigDecimal(min_clstr_ints);
        final BigDecimal x_min = slopes.stream().map(s->s.parm_value).min(Comparator.naturalOrder()).get();
        final BigDecimal x_max = slopes.stream().map(s->s.parm_value).max(Comparator.naturalOrder()).get();
        final BigDecimal max_slope = slopes.stream().map(e -> e.slope).max(Comparator.naturalOrder()).get();

        LOG.log(Level.FINE, "x_min: {0}, x_max: {1}, max_slope: {2}, ", new Object[]{x_min, x_max, max_slope});

        if (x_max.subtract(x_min).compareTo(BigDecimal.ZERO) <= 0) {
            LOG.log(Level.FINE, "input data error: no parametric variation, x-max - x-min = 0");
            return ranges;
        }

        BigDecimal threshold;
        threshold = threshold_modifier.multiply(max_slope);

        LOG.log(Level.FINE, "threshold: {0}", threshold);
        final BigDecimal alternateThreshold = minClusterInts.divide(incr);

        if (max_slope.compareTo(alternateThreshold) < 0) {
            LOG.log(Level.FINE, "no clusters found");
            return ranges;
        }

        if (max_slope.equals(BigDecimal.ZERO)) {
            LOG.log(Level.FINE, "max_slope is 0");
            return ranges;
        }

        if (threshold.compareTo(alternateThreshold) < 0) {
            threshold = alternateThreshold;
            LOG.log(Level.FINE, "using alternate threshold: {0}", threshold);
        }

        BigDecimal m_start = null;
        BigDecimal m_stop = null;
        BigDecimal temp_stop = null;

        BigDecimal prev_parm_val = BigDecimal.ZERO;
        BigDecimal parm_curr_val = null;
        BigDecimal test_stat;

        boolean in_mode = false;
        boolean gathering = false;

        BigDecimal h_count = BigDecimal.ZERO;

        for (Slope e : slopes) {

            parm_curr_val = e.parm_value;
            test_stat = e.slope;

            h_count = h_count.add(BigDecimal.ONE);

            if (test_stat.compareTo(threshold) >= 0) {
                h_count = BigDecimal.ZERO;
                temp_stop = null;
            }

            if (test_stat.compareTo(threshold) >= 0 && !in_mode) {
                m_start = parm_curr_val;
                in_mode = true;
            }

            if (in_mode && test_stat.compareTo(threshold) < 0) {
                if (h_count.compareTo(horizon) > 0) {
                    m_stop = temp_stop;
                    temp_stop = null;
                    gathering = false;
                    in_mode = false;
                } else if (temp_stop == null) {
                    temp_stop = prev_parm_val;
                }
            }

            if (m_start != null && m_stop != null) {
                final Range range = new Range(m_start, m_stop.add(incr));
                LOG.log(Level.FINE, "new {0} range", range);
                ranges.add(range);
                m_start = null;
                m_stop = null;
                in_mode = false;
                gathering = false;
            }

            prev_parm_val = parm_curr_val;

            if (in_mode) {
                if (temp_stop == null) {
                    m_stop = x_max;
                } else {
                    m_stop = temp_stop;
                }
                final Range range = new Range(m_start, m_stop.add(incr));
                LOG.log(Level.FINE, "new {0} range", range);
                ranges.add(range);
            } else {
                LOG.log(Level.FINE, "post loop, not in-mode");
            }
        }
        return ranges;
    }
}
