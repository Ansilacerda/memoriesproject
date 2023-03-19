package hellospringboot.memories.repository;

import hellospringboot.memories.model.Memory;
import hellospringboot.memories.model.MemoryCreatorUtil;
import org.hibernate.engine.jdbc.Size;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@TestPropertySource({"classpath:application-test.properties"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemoryRepositoryTest {

    @Autowired
    private MemoryRepository repository;

    private static final int SIZE = 8192;
    private static final String TITLE = "peixe";

    @AfterEach
     void tearDown() {
        repository.deleteAllInBatch();
    }

    @Test
    void deleteById_shouldThrowsEmptyResultDataAccessException_memoryNotFound() {

        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(Long.MAX_VALUE);
        });
    }

    @Test
    void findBySizeAndInStockIsTrue_shouldReturnMemory() {
        saveMemory(TITLE, SIZE);
        int size = SIZE * 2;
        Memory memory = saveMemory(TITLE, size);

        List<Memory> memories = repository.findBySizeAndInStockIsTrue(size);

        assertEquals(1, memories.size());
        assertEquals(memory, memories.get(0));
    }

    @Test
    void findBySizeAndInStockIsTrue_shouldReturnNull() {
        saveMemory(TITLE, SIZE);
        int size = SIZE + 1;
        List<Memory> result = repository.findBySizeAndInStockIsTrue(size);

        assertTrue(result.isEmpty());
    }

    @Test
    void findBySizeAndInStockIsTrue_shouldReturnInStockOk() {
        Memory memory1 = repository.save(MemoryCreatorUtil.create(TITLE, SIZE, true));
        repository.save(MemoryCreatorUtil.create(TITLE + "salmão", SIZE, false));

        List<Memory> result = repository.findBySizeAndInStockIsTrue(SIZE);

        assertEquals(memory1, result.get(0));
        assertEquals(1, result.size());
    }

    @Test
    void findBySizeBetween() {
        Memory memory1 = repository.save(MemoryCreatorUtil.create(TITLE, SIZE, true));
        Memory memory2 = repository.save(MemoryCreatorUtil.create(TITLE, SIZE + 1, true));

        List<Memory> result = repository.findBySizeBetween(SIZE - 1, SIZE + 2);

        assertEquals(2, result.size());
        assertTrue(result.contains(memory1));
        assertTrue(result.contains(memory2));
    }

    @Test
    void findBySizeGreaterThanEqualAndInStockIsTrue_shouldReturnInStockOk() {
        Memory memory1 = repository.save(MemoryCreatorUtil.create(TITLE + "salmão", SIZE * 2, true));
        repository.save(MemoryCreatorUtil.create(TITLE + "peixe", SIZE - 1, false));
        Memory memory3 = repository.save(MemoryCreatorUtil.create(TITLE + "lula", SIZE, true));

        List<Memory> result = repository.findBySizeGreaterThanEqualAndInStockIsTrue(SIZE, Sort.by("size"));

        assertEquals(memory3, result.get(0));
        assertEquals(memory1, result.get(1));
        assertEquals(2, result.size());
    }

    @Test
    void findByTitle_shouldReturnMemory() {
        saveMemory(TITLE, SIZE);
        Memory memory = repository.findByTitle(TITLE);

        assertEquals(TITLE, memory.getTitle());
        assertNotNull(memory);
    }

    @Test
    void findByTitle_shouldReturnNull() {
        saveMemory(TITLE,SIZE);
        Memory memory = repository.findByTitle(TITLE + "salmão");

        assertNull(memory);
    }

    @Test
    void findByTitleContaining_shouldReturnInStockOk() {

        Memory memory1 = repository.save(MemoryCreatorUtil.create(TITLE + "salmão", SIZE * 2, true));
        repository.save(MemoryCreatorUtil.create(TITLE + "peixe", SIZE, false));
        Memory memory3 = repository.save(MemoryCreatorUtil.create(TITLE + "lula", SIZE + 1, true));

        List<Memory> result = repository.findByTitleContaining(TITLE);

        assertEquals(memory1, result.get(0));
        assertEquals(memory3, result.get(2));
        assertEquals(3, result.size());
    }

    private Memory saveMemory(String title, int size) {
       Memory memory = MemoryCreatorUtil.create(title, size);
       return repository.save(memory);
    }

}
