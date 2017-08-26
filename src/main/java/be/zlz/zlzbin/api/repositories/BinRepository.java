package be.zlz.zlzbin.api.repositories;

import be.zlz.zlzbin.api.domain.Bin;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BinRepository extends CrudRepository<Bin, Long> {
    Bin getByName(String name);

    List<Bin> getBinByCreationDateBefore(Date date);

    List<Bin> getBinByLastRequestBefore(Date date);
}
