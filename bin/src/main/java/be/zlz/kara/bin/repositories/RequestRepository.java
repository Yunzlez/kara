package be.zlz.kara.bin.repositories;

import be.zlz.kara.bin.domain.Bin;
import be.zlz.kara.bin.domain.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Iterator;
import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {

    Page<Request> getByBinOrderByRequestTimeDesc(Bin bin, Pageable pageable);

    @Modifying
    @Query(nativeQuery = true, value = "delete from request where bin_id=?1")
    void deleteAllByBinEfficient(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from request_headers where request_id in (select request.id from request where bin_id=?1);")
    void deleteHeadersForBin(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from request_query_params where request_id in (select request.id from request where bin_id=?1);")
    void deleteQueryParamsForBin(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "update request set body = 'Body truncated.' where id < (select id from (select id from request order by request_time desc limit 100, 1) as tmp) and bin_id=?1")
    void clearBodies(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete rh from kara.request_headers rh left join kara.request r on r.id = rh.request_id where r.id IS NULL;")
    void deleteRequestOrphans();

    @Query(value = "OPTIMIZE TABLE request;", nativeQuery = true)
    String optimizeRequestTable();

    @Query(value = "OPTIMIZE TABLE request_headers;", nativeQuery = true)
    String optimizeHeaderTable();
}
