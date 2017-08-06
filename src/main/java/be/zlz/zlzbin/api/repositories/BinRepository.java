package be.zlz.zlzbin.api.repositories;

import be.zlz.zlzbin.api.domain.Bin;
import org.springframework.data.repository.CrudRepository;

public interface BinRepository extends CrudRepository<Bin, Long>{
    public Bin getByName(String name);
}
