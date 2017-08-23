package be.zlz.zlzbin.api.repositories;

import be.zlz.zlzbin.api.domain.Bin;
import be.zlz.zlzbin.api.domain.Request;
import com.sun.org.apache.regexp.internal.RE;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RequestRepository extends CrudRepository<Request, Long> {
    List<Request> getAllByBin(Bin bin);

    void deleteAllByBin(Bin bin);

    List<Request> getAllByBinOrderByRequestTimeAsc(Bin bin);
}
