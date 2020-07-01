explain analyze select * from intercept_aois;

create or replace view intercept_aois as 
  select intercept_id, string_agg(aoi_code, ';' order by aoi_code) from (
    select intercept_id, aoi_code from intercepts
      join aoi_country_codes
        using ( country_code )
  ) as a
  group by intercept_id
;

select country_code, string_agg(aoi_code, ';' order by aoi_code) from (
  select * from aoi_country_codes ) a 
  group by country_code 
;

select aoi_code, string_agg(country_code, ';' order by country_code) from (
  select * from aoi_country_codes ) a 
  group by aoi_code 
;
  
explain analyze with aaa ( intercept_id, aoi_code ) as (
  select intercept_id, aoi_code from intercepts
    join aoi_country_codes
      using ( country_code )
) select * from aaa
;


