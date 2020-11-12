/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mil.af.flagging.dataload;

import java.util.TreeMap;

/**
 *
 * @author Brad
 */
public class PostgresDialect extends TreeMap<String, String> {

    {
        this.put("INSERT_COUNTRY_CODES", "insert into country_codes ( country_code, description ) values ( ?, ? ) on conflict do nothing");
        this.put("FETCH_COUNTRY_CODES", "select * from country_codes");
        this.put("INSERT_AOI_COUNTRY_CODES", "insert into aoi_country_codes ( aoi_code, country_code ) values ( ?, ? ) on conflict do nothing");
        this.put("INSERT_AOI_CODES", "insert into aoi_codes ( aoi_code, description ) values ( ?, ? ) on conflict do nothing");
        this.put("SELECT_AOI_COUNTRIES", "select * from aoi_country_codes where aoi_code = ?");
        this.put("SELECT_ALL_AOI_CODES", "select * from aoi_codes");
        this.put("PARENT_RECORD_INSERTION", "INSERT INTO intercepts ( intercept_id, wrangler_id, elnot, mod_type, scan_type, scan_period, "
                + "time_process, int_up_time, int_down_time, country_code, latitude, longitude, semi_major, semi_minor, orientation, "
                + "read_out_station, num_of_burst ) "
                + "values "
                + "( DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) "
                + "ON CONFLICT DO NOTHING");
        this.put("RF_RECORD_INSERTION", "INSERT INTO intercept_rfs "
                + "  ( intercept_id, sequence, value ) "
                + "     values "
                + "   ( ?, ?, ? )");
        this.put("PRI_RECORD_INSERTION",
                "INSERT INTO intercept_pris "
                + "  ( intercept_id, sequence, value ) "
                + "     values "
                + "   ( ?, ?, ? )");
        this.put("PD_RECORD_INSERTION",
                "INSERT INTO intercept_pds "
                + "  ( intercept_id, sequence, value ) "
                + "     values "
                + "   ( ?, ?, ? )");
        this.put(
                "INSERT_AOI_REQ", "insert into idb_states ( intercept_id, flow_control, last_modified ) values ( ?, 'AOI-REQ', DEFAULT )\";\n");
    }
}
