package ch.bbw.m320.restintro.api.v1;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

import ch.bbw.m320.restintro.dto.PonyDto;
import ch.bbw.m320.restintro.entity.PonyEntity;
import ch.bbw.m320.restintro.repository.PonyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class PonyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PonyRepository ponyRepository;

    @BeforeEach
    public void setup() {
        PonyEntity pony1 = new PonyEntity("Thunder", 10, 1.35, 350, true, LocalDate.of(2014, 5, 12), "Äpfel");
        // this test is all about REST calls.
        // calling the DB directly is like cheating. and it is not clean because you expose internal behaviour.
        // instead use the POST method to create new ponies and avoid any dependency on internals.
        ponyRepository.save(pony1);
    }

    @Test
    void testGetPonies() throws Exception { // no public test methods, there is no need. see sonar.
        mockMvc.perform(get("/api/ponies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetPonyById() throws Exception {
        mockMvc.perform(get("/api/ponies/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Thunder")));
    }

    @Test
    public void testGetPonyByIdFalse() throws Exception {
        mockMvc.perform(get("/api/ponies/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetPonyByName() throws Exception {
        // this test is NOT idempotent: if run by itself it will be green, together with the others will be red.
        // we want tests to not have side effects.
        mockMvc.perform(get("/api/ponies/name/Thunder"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Thunder")));
    }

    @Test
    public void testNewPony() throws Exception {
        PonyDto newPony = new PonyDto("Blitz", 5, 0, 1.25, 280, true, LocalDate.of(2018, 5, 10), "Äpfel");
        mockMvc.perform(post("/api/ponies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPony)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testNewPonies() throws Exception {
        List<PonyDto> newPonies = List.of(
                new PonyDto("Blitz", 5, 0, 1.25, 280, true, LocalDate.of(2018, 5, 10), "Äpfel"),
                new PonyDto("Sternchen", 8, 0, 1.30, 310, false, LocalDate.of(2016, 3, 15), "Möhren")
        );
        mockMvc.perform(post("/api/ponies/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newPonies)))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdatePony() throws Exception {
        PonyDto updatedPony = new PonyDto("Thunderbolt", 11, 99, 1.40, 360, false, LocalDate.of(2013, 10, 22), "Heu");
        mockMvc.perform(put("/api/ponies/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPony)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeletePony() throws Exception {
        mockMvc.perform(delete("/api/ponies/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeletePonyFalse() throws Exception {
        mockMvc.perform(delete("/api/ponies/99"))
                .andExpect(status().isNotFound());
    }
}
