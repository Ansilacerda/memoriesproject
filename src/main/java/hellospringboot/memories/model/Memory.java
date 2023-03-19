package hellospringboot.memories.model;

import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class Memory {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	private String title;
	private int size;
	private boolean inStock;

	public Memory(String title, int size, boolean inStock) {
		super();
		this.id = 0L;
		this.title = title;
		this.size = size;
		this.inStock = inStock;
	}

	Memory() {
		this("",0, true);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Memory memory = (Memory) o;
		return size == memory.size && Objects.equals(id, memory.id) && Objects.equals(title, memory.title);
	}

	public int getSize() {
		return size;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, size);
	}

	public boolean isInStock() {
		return inStock;
	}

	@Override
	public String toString() {
		return "Memory{" +
				"id=" + id +
				", title='" + title + '\'' +
				", size=" + size +
				'}';
	}
}
