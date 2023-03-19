package hellospringboot.memories.exception;

public class MemoryNotFoundException extends RuntimeException {
    public MemoryNotFoundException(Long id) {
        super("Could not find this memory " +id);
    }
}
