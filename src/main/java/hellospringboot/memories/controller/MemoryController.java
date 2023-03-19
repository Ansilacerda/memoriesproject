package hellospringboot.memories.controller;

import java.util.List;
import hellospringboot.memories.service.MemoryService;
import org.springframework.web.bind.annotation.*;
import hellospringboot.memories.model.Memory;

@RestController
@RequestMapping("/memories")
public class MemoryController {

	private MemoryService service;

	public MemoryController(MemoryService service) {
		this.service = service;
	}

	@PostMapping
	public Memory create(@RequestBody Memory memory) {
		return service.create(memory);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id){
		service.delete(id);
	}

	@GetMapping
	public List<Memory> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public Memory findById(@PathVariable Long id){
		return service.findById(id);
	}

	@GetMapping("/size/{size}")
	public List<Memory> findBySize(@PathVariable int size) {
		return service.findBySize(size);
	}

	@GetMapping("/greaterthanequal/{size}")
	public List<Memory> findBySizeGreaterThanEqualAndInStockIsTrue(@PathVariable int size) {
		return service.findBySizeGreaterThanEqualAndInStockIsTrue(size);
	}

	@PutMapping("/{id}")
	public Memory update(@RequestBody Memory newMemory, @PathVariable Long id) {
		return service.update(newMemory, id);
	}

}
