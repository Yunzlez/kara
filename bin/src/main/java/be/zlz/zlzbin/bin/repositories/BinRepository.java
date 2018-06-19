package be.zlz.zlzbin.bin.repositories;

import be.zlz.zlzbin.bin.domain.Bin;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface BinRepository extends CrudRepository<Bin, Long> {
    Bin getByName(String name);

    List<Bin> getBinByLastRequestBeforeAndPermanentIsFalse(Date date);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "insert into request_metric_counts (request_metric_id, counts, counts_key) values (?1, 1, ?2) on duplicate key update counts = counts+1;")
    void updateMetric(long requestMetricId, String key);

    List<Bin> getBinByLastRequestBefore(Date date);

    @Query(value = "select sum(row_size) from (select ifnull(length(body), 0) as row_size from zlzbin.request where bin_id=:id ) as `size`;", nativeQuery = true)
    Long getBinSizeInBytes(@Param("id") Long id);
}
