package be.zlz.zlzbin.api.repositories;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {

    void deleteAllByBin(Bin bin);

    Page<Request> getByBinOrderByRequestTimeDesc(Bin bin, Pageable pageable);
}
