package ntnu.idatt2105.section.controller

import com.fasterxml.jackson.databind.ObjectMapper
import ntnu.idatt2105.section.model.Section
import ntnu.idatt2105.section.repository.SectionRepository
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*


@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    private val URL = "/sections/"

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var mvc: MockMvc

    @Autowired
    private lateinit var sectionRepository: SectionRepository

    private lateinit var section : Section


    @BeforeEach
    fun setUp(){
        TODO()
    }

    @Test
    fun `test author controller GET returns OK and the author`() {
        this.mvc.perform(get("$URL{sectionId}", section.id))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("\$.name").value(section.name))
                .andExpect(jsonPath("\$.id").value(section.id.toString()))
    }

    @Test
    fun `test author controller GET returns not found`() {
        this.mvc.perform(get("$URL{name}","test"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `test author controller POST returns OK`() {
        this.mvc.perform(post(URL)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newAuthor)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.name").value(newAuthor.name))
                .andExpect(jsonPath("\$.age").value(newAuthor.age))
    }

    @Test
    fun `test author controller PUT returns OK`() {
        this.mvc.perform(put("$URL{name}/", name)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isOk)
                .andExpect(jsonPath("\$.name").value(newName))
                .andExpect(jsonPath("\$.age").value(author.age))
    }

    @Test
    fun `test author controller PUT returns not found`() {
        this.mvc.perform(put("$URL{name}/","test")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isNotFound)
    }

    @Test
    fun `test author controller DELETE returns OK`() {
        this.mvc.perform(delete("$URL{name}/", author.name))
                .andExpect(status().isOk)
    }

    @Test
    fun `test author controller DELETE return NotFound`() {
        this.mvc.perform(delete("$URL{name}/", getRandomString(5)))
                .andExpect(status().isNotFound)
    }
}