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

    void deleteAllByBin(Bin bin);

    @Modifying
    @Query(nativeQuery = true, value = "delete from request where bin_id=?1")
    void deleteAllByBinEfficient(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from request_headers where request_id in (select request.id from request where bin_id=?1);")
    void deleteHeadersForBin(long binId);

    @Modifying
    @Query(nativeQuery = true,
            value = "delete from request_query_params where request_id in (select request.id from request where bin_id=?1);" )
    void deleteQueryParamsForBin(long binId);

    @Modifying
    @Query("UPDATE Request r set r.body='Body truncated.' where r.id in :ids")
    void clearBodies(@Param(value = "ids") List<Long> ids);

    Page<Request> getByBinOrderByRequestTimeDesc(Bin bin, Pageable pageable);

    Request findTopByBinOrderByRequestTime(Bin bin);

    @Query(value = "OPTIMIZE TABLE request;", nativeQuery = true)
    String optimizeTable();
}
