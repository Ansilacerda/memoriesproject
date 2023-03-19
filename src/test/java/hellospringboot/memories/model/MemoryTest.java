package hellospringboot.memories.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MemoryTest {

    @Test
    void constructor_shouldSetAttributes() {
        String title = "Peixe";
        int size = 8192;
        boolean inStock = true;

        Memory memory = new Memory(title, size, inStock);

        assertEquals(inStock, memory.isInStock());
        assertEquals(size, memory.getSize());
        assertEquals(title, memory.getTitle());
        assertEquals(0L, memory.getId());
    }

    @Test
    void constructor_shouldNotSetAttributes() {
        Memory memory = new Memory();

        assertEquals(0L, memory.getId());
        assertEquals("", memory.getTitle());
    }

    @Test
    void setId_shouldSetId(){
        Long id = 0L;
        Memory memory = new Memory();

        memory.setId(id);

        assertEquals(memory.getId(), id);
    }

    @Test
    void setTitle_shouldSetTitle(){
        String title = "salmão";
        Memory memory = new Memory();

        memory.setTitle(title);

        assertEquals(title, memory.getTitle());
    }
}
