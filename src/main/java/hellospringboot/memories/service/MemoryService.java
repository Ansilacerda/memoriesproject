package hellospringboot.memories.service;

import hellospringboot.memories.exception.MemoryNotCreateException;
import hellospringboot.memories.exception.MemoryNotFoundException;
import hellospringboot.memories.model.Memory;
import hellospringboot.memories.repository.MemoryRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
public class MemoryService {

    private MemoryRepository repository;

    public MemoryService(MemoryRepository repository) {
        this.repository = repository;
    }

    public Memory create(Memory memory) {
        checkTitle(memory.getTitle());
        return repository.save(memory);
    }

    public void delete(Long id) {
        try {
            repository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new MemoryNotFoundException(id);
        }
    }

    public List<Memory> findAll() {
        return repository.findAll();
    }

    public Memory findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new MemoryNotFoundException(id));
    }

    public List<Memory> findBySize(int size) {
        return repository.findBySizeAndInStockIsTrue(size);
    }

    public List<Memory> findBySizeGreaterThanEqual(int size) {
        return repository.findBySizeGreaterThanEqual(size);
    }

    public List<Memory> findBySizeGreaterThanEqualAndInStockIsTrue(int size) {
        return repository.findBySizeGreaterThanEqualAndInStockIsTrue(size, Sort.by("size"));
    }

    public List<Memory> findByTitleContaining(String title) {
        return repository.findByTitleContaining(title);
    }

    public Memory update(Memory newMemory, Long id) {
        return repository.findById(id).map(memory -> {
            checkTitle(newMemory.getTitle());
            memory.setTitle(newMemory.getTitle());
            return repository.save(memory);
        }).orElseThrow(() -> new MemoryNotFoundException(id));
    }

    private void checkTitle(String title) {
        Memory memory = repository.findByTitle(title);
        if (memory != null) {
            throw new MemoryNotCreateException(String.format("Memory with title = $s already exists", memory.getTitle()));
        }
    }
}
