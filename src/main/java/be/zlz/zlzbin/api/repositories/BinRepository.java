package be.zlz.zlzbin.api.repositories;

import be.zlz.zlzbin.api.domain.Bin;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BinRepository extends CrudRepository<Bin, Long> {
    public Bin getByName(String name);

    public List<Bin> getBinByCreationDateBefore(Date date);
}
