drop table if exists intercepts cascade;

--DO $$ DECLARE
  --tabname RECORD
-- BEGIN
--   FOR tabname IN ( SELECT tablename FROM pg_tables WHERE schemaname = current_schema()) 
--   LOOP
--     EXECUTE 'DROP TABLE IF EXISTS ' || quote_ident(tabname.tablename) || ' CASCADE';
--   EXCEPTION
--     WHEN OTHERS THEN
--       raise notice 'skipping %', quote_ident(tabname.tablename);
--     END;
--   END LOOP;
--END $$;
