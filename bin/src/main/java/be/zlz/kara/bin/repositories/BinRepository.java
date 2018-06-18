package be.zlz.kara.bin.repositories;

import be.zlz.kara.bin.domain.Bin;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BinRepository extends CrudRepository<Bin, Long> {
    Bin getByName(String name);

    List<Bin> getBinByLastRequestBeforeAndPermanentIsFalse(Date date);

    List<Bin> getBinByLastRequestBefore(Date date);
}
