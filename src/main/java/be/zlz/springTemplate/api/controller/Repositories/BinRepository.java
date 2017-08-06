package be.zlz.springTemplate.api.controller.Repositories;

import be.zlz.springTemplate.api.controller.domain.Bin;
import org.springframework.data.repository.CrudRepository;

public interface BinRepository extends CrudRepository<Bin, Long>{
    public Bin getByName(String name);
}
