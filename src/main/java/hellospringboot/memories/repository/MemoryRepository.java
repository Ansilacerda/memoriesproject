package hellospringboot.memories.repository;

import hellospringboot.memories.model.Memory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemoryRepository extends JpaRepository<Memory, Long> {


    List<Memory> findBySizeAndInStockIsTrue(int size);

    List<Memory> findBySizeGreaterThanEqual(int size);

    List<Memory> findBySizeGreaterThanEqualAndInStockIsTrue(int size, Sort sort);

    Memory findByTitle(String title);

    List<Memory> findBySizeBetween(int low, int high);

    List<Memory> findByTitleContaining(String title);
}
