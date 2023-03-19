package hellospringboot.memories.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hellospringboot.memories.exception.MemoryNotCreateException;
import hellospringboot.memories.exception.MemoryNotFoundException;
import hellospringboot.memories.model.Memory;
import hellospringboot.memories.model.MemoryCreatorUtil;
import hellospringboot.memories.service.MemoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemoryController.class)
@ContextConfiguration(classes = {MemoryController.class, MemoryAPIExceptionHandler.class})
public class MemoryControllerTest {

    private static final String ENDPOINT = "/memories";
    private static final Long ID = 1L;

    @MockBean
    private MemoryService service;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private int size = 8192;

    @Test
    void create_shouldReturnOK() throws Exception {
        Memory memory = MemoryCreatorUtil.create("peixe", size);

        doReturn(memory).when(service).create(memory);
        String json = objectMapper.writeValueAsString(memory);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT).content(json)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).create(memory);
    }

    @Test
    void create_shouldReturnBadRequest_TitleAlreadyExist() throws Exception {
        Memory memory = MemoryCreatorUtil.create("peixe", size);

        doThrow(MemoryNotCreateException.class).when(service).create(memory);
        String json = objectMapper.writeValueAsString(memory);

        mockMvc.perform(MockMvcRequestBuilders.post(ENDPOINT).content(json)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, only()).create(memory);
    }

    @Test
    void delete_shouldReturnOK() throws Exception {
        doNothing().when(service).delete(ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT+"/"+ ID)).andExpect(status().isOk());

        verify(service, only()).delete(ID);
    }

    @Test
    void delete_shouldReturnNotFound() throws Exception {
        doThrow(MemoryNotFoundException.class).when(service).delete(ID);

        mockMvc.perform(MockMvcRequestBuilders.delete(ENDPOINT + "/" + ID)).andExpect(status().isNotFound());

        verify(service, only()).delete(ID);
    }

    @Test
    void findAll_shouldReturnOK_returnEmptyCollection() throws Exception {
        List<Memory> memories = Collections.emptyList();
        doReturn(memories).when(service).findAll();
        String json = objectMapper.writeValueAsString(memories);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT)).andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).findAll();
    }

    @Test
    void findAll_shouldReturnOk_returnMemories() throws Exception {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));
        doReturn(memories).when(service).findAll();
        String json = objectMapper.writeValueAsString(memories);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT)).andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).findAll();
    }

    @Test
    void findById_shouldReturnOk() throws Exception {
        Memory memory = MemoryCreatorUtil.create("peixe", size);
        doReturn(memory).when(service).findById(ID);
        String json = objectMapper.writeValueAsString(memory);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + ID)).andExpect(status().isOk()).andReturn()
                .equals(json);

        verify(service, only()).findById(ID);
    }

    @Test
    void findById_shouldReturnNotFound() throws Exception {
        doThrow(MemoryNotFoundException.class).when(service).findById(ID);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/" + ID)).andExpect(status().isNotFound());

        verify(service, only()).findById(ID);
    }

    @Test
    void findBySize_shouldReturnOk() throws Exception {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));
        doReturn(memories).when(service).findBySize(size);
        String json = objectMapper.writeValueAsString(memories);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/size/" + size)).andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).findBySize(size);
    }

    @Test
    void findBySizeGreaterThanEqualAndInStockIsTrue_shouldReturnOk() throws Exception {
        List<Memory> memories = Arrays.asList(MemoryCreatorUtil.create("salmão", size), MemoryCreatorUtil.create("peixe", size));
        doReturn(memories).when(service).findBySizeGreaterThanEqualAndInStockIsTrue(size);
        String json = objectMapper.writeValueAsString(memories);

        mockMvc.perform(MockMvcRequestBuilders.get(ENDPOINT + "/greaterthanequal/" + size)).andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).findBySizeGreaterThanEqualAndInStockIsTrue(size);
    }

    @Test
    void update_shouldReturnOk() throws Exception {
        Memory memoryUpdate = MemoryCreatorUtil.create("peixe", size);
        doReturn(memoryUpdate).when(service).update(memoryUpdate, ID);
        String json = objectMapper.writeValueAsString(memoryUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/"+ ID).content(json)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().equals(json);

        verify(service, only()).update(memoryUpdate, ID);
    }

    @Test
    void update_shouldReturnBadRequest_TitleAlreadyExist() throws Exception {
        Memory memoryUpdate = MemoryCreatorUtil.create("peixe", size);
        doThrow(MemoryNotCreateException.class).when(service).update(memoryUpdate, ID);
        String json = objectMapper.writeValueAsString(memoryUpdate);

        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/" +ID).content(json)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(service, only()).update(memoryUpdate, ID);
    }

    @Test
    void update_shouldReturnNotFound() throws Exception {
        Memory memoryUpdate = MemoryCreatorUtil.create("peixe", size);
        doThrow(MemoryNotFoundException.class).when(service).update(memoryUpdate, ID);

        String json = objectMapper.writeValueAsString(memoryUpdate);
        mockMvc.perform(MockMvcRequestBuilders.put(ENDPOINT + "/"+ ID).content(json)
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(service, only()).update(memoryUpdate, ID);
    }
}
