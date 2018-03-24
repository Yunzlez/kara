package be.zlz.zlzbin.bin.repositories;

import be.zlz.zlzbin.bin.domain.Bin;
import be.zlz.zlzbin.bin.domain.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface RequestRepository extends CrudRepository<Request, Long> {

    void deleteAllByBin(Bin bin);

    Page<Request> getByBinOrderByRequestTimeDesc(Bin bin, Pageable pageable);

    Request findTopByBinOrderByRequestTime(Bin bin);
}
