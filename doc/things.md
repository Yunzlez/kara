###Total bin stats: 
* Total request count
    SELECT SUM(request_count) FROM zlzbin.bin;

* db size on disc: 
    SELECT table_schema as "Database", 
    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS "Size (MB)" 
    FROM information_schema.TABLES 
    WHERE table_schema='zlzbin'
    GROUP BY table_schema;

* Req/s in total last 5 min

