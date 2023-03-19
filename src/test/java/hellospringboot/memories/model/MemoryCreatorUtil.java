package hellospringboot.memories.model;

public class MemoryCreatorUtil {

    public static Memory create(String title, int size) {
        return create(title, size,true);
    }

    public static Memory create(String title, int size, boolean inStock) {
        return new Memory(title, size, inStock);
    }

}
