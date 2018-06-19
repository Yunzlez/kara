package be.zlz.zlzbin.bin.repositories;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

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

    Page<Request> getByBinOrderByRequestTimeDesc(Bin bin, Pageable pageable);

    Request findTopByBinOrderByRequestTime(Bin bin);
}
