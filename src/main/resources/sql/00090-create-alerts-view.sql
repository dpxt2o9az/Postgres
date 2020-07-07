create or replace view intercept_dataload_rules as
  select * from ( 
    select 
      idrt.intercept_dataload_rule_id,
      idrt.type,
      idrt.comment_val,
      idrt.status,
      idrt.re_id_elnot,
      idrt.st_conf,
      idrt.sp_conf,
      idrt.ir_conf,
      idrt.pd_conf,
      idrt.mt_conf,
      idrt.mt_conf_new_mt,
      idrt.creator,
      idrt.created_date,
      idrt.last_edited,
      idrt.ops_page,
      idrt.bbs,
      idrt.heardcount,
      idrt.heardcount_threshold,
      el.email_list email_list,
    from intercept_dataload_rules_table idrt
      left outer join
        ( select rule_id, string_agg(upper(email_address), ',' order by email_address) email_list
            from ( 
              select
                distinct agir.intercept_dataload_rule_id rule_id,
                tu.email_address email_address
              from alert_group_intercept_rules agir,
                alert_group_team_users agtu,
                team_users tu
              where 
                agir.alert_group_id = agtu.alert_group_id
                and tu.userid = agtu.team_users_userid
                and tu.status = 'A') x1
            group by rule_id) el
      on idrt.intercept_dataload_rule_id = el.rule_id) x2
where x2.email_list is not null;