package hellospringboot.memories.service;

import hellospringboot.memories.exception.MemoryNotCreateException;
import hellospringboot.memories.exception.MemoryNotFoundException;
import hellospringboot.memories.model.Memory;
import hellospringboot.memories.model.MemoryCreatorUtil;
import hellospringboot.memories.repository.MemoryRepository;
import org.apache.catalina.LifecycleState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemoryServiceTest {

    private static final Long ID = 1L;

    @Mock
    private MemoryRepository repository;

    private MemoryService service;

    private int size = 8192;

    private String title = "pe";

    @BeforeEach
    void setup() {
        service = new MemoryService(repository);
    }

    @Test
    void create_shouldCallCreate() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);


        doReturn(null).when(repository).findByTitle(memory.getTitle());
        doReturn(memory).when(repository).save(memory);

        Memory result = service.create(memory);

        assertEquals(memory, result);
        verify(repository, times(1)).save(memory);
        verify(repository, times(1)).findByTitle(memory.getTitle());
    }

    @Test
    void create_shouldThrowsMemoryNotCreateException_titleAlreadyExists() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);

        doReturn(memory).when(repository).findByTitle(memory.getTitle());

        assertThrows(MemoryNotCreateException.class, () -> {
           service.create(memory);
        });
        verify(repository, only()).findByTitle(memory.getTitle());
    }

    @Test
    void delete_shouldCallDeleteById() {
        doNothing().when(repository).deleteById(ID);

        service.delete(ID);

        verify(repository, only()).deleteById(ID);
    }

    @Test
    void delete_shouldThrowsMemoryNotFoundException() {
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(ID);

        assertThrows(MemoryNotFoundException.class, () -> {
           service.delete(ID);
        });

        verify(repository, only()).deleteById(ID);
    }

    @Test
    void findAll_shouldReturnMemories() {
        List<Memory> memories = new ArrayList<>();
        memories.add(MemoryCreatorUtil.create("peixe", size));

        doReturn(memories).when(repository).findAll();

        List<Memory> result = service.findAll();

        assertEquals(memories, result);
        verify(repository, only()).findAll();
    }

    @Test
    void findById_shouldReturnMemory() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);

        doReturn(Optional.of(memory)).when(repository).findById(ID);

        Memory result = service.findById(ID);

        assertEquals(memory, result);
        verify(repository, only()).findById(ID);
    }

    @Test
    void findById_shouldThrowsException_memoryNotFound() {
        doReturn(Optional.empty()).when(repository).findById(ID);

        assertThrows(MemoryNotFoundException.class, () -> {
            service.findById(ID);
        });

        verify(repository, only()).findById(ID);
    }

    @Test
    void findBySize_shouldReturnMemories() {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));

        doReturn(memories).when(repository).findBySizeAndInStockIsTrue(size);

        List <Memory> result = service.findBySize(size);

        assertEquals(memories, result);
        verify(repository, only()).findBySizeAndInStockIsTrue(size);
    }

    @Test
    void findBySizeGreaterThanEqual_shouldReturnMemories() {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));
        doReturn(memories).when(repository).findBySizeGreaterThanEqual(size);

        List<Memory> result = service.findBySizeGreaterThanEqual(size);

        assertEquals(memories, result);
        verify(repository, only()).findBySizeGreaterThanEqual(size);
    }

    @Test
    void findBySizeGreaterThanEqualAndInStockIsTrue_shouldReturnMemories() {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));
        doReturn(memories).when(repository).findBySizeGreaterThanEqualAndInStockIsTrue(size, Sort.by("size"));

        List<Memory> result = service.findBySizeGreaterThanEqualAndInStockIsTrue(size);

        assertEquals(memories, result);
        verify(repository, only()).findBySizeGreaterThanEqualAndInStockIsTrue(size, Sort.by("size"));
    }

    @Test
    void findByTitleContaining_shouldReturnMemories() {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));

        doReturn(memories).when(repository).findByTitleContaining(title);

        List <Memory> result = service.findByTitleContaining(title);

        assertEquals(memories, result);
        verify(repository, only()).findByTitleContaining(title);

    }

    @Test
    void update_shouldUpdateMemory() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);

        Memory memoryUpdate = MemoryCreatorUtil.create("salmão", size);

        doReturn(Optional.of(memory)).when(repository).findById(ID);
        doReturn(null).when(repository).findByTitle(memoryUpdate.getTitle());
        doReturn(memoryUpdate).when(repository).save(memoryUpdate);

        Memory result = service.update(memoryUpdate, ID);

        assertEquals(memoryUpdate, result);
        verify(repository, times(1)).findById(ID);
        verify(repository, times(1)).findByTitle(memoryUpdate.getTitle());
        verify(repository, times(1)).save(memoryUpdate);
    }

    @Test
    void update_shouldThrowsMemoryNotFoundException_idNotExists() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);
        doReturn(Optional.empty()).when(repository).findById(ID);

        assertThrows(MemoryNotFoundException.class, () -> {
           service.update(memory, ID);
        });

        verify(repository, only()).findById(ID);
    }

    @Test
    void update_shouldThrowsMemoryNotCreateException_titleAlreadyExists() {
        Memory memory = MemoryCreatorUtil.create("peixe", size);
        doReturn(Optional.of(memory)).when(repository).findById(ID);
        doReturn(memory).when(repository).findByTitle(memory.getTitle());

        assertThrows(MemoryNotCreateException.class, () -> {
            service.update(memory, ID);
        });

        verify(repository, times(1)).findById(ID);
        verify(repository, times(1)).findByTitle(memory.getTitle());
        verify(repository, never()).save(any());
    }
}
