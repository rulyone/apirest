/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.people.apirest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.people.apirest.controller.PersonController;
import io.people.apirest.model.Person;
import io.people.apirest.repository.PersonRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.CoreMatchers.is;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 *
 * @author rulyone
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
//@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class PersonControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
        
    @Autowired
    private PersonRepository personRepository;
    
    @Test
    @Order(1)
    public void getPeopleTest() throws Exception {        
        //given 21 persons added (pagination defaults to 20 per page, to test defaults we need more persons than 20)
        createTestPerson("1-9");
        createTestPerson("8643292-7");
        createTestPerson("6945885-8");
        createTestPerson("8797808-7");
        createTestPerson("23052915-9");
        createTestPerson("17601329-k");
        createTestPerson("14508334-6");
        createTestPerson("12387129-4");
        createTestPerson("9562154-6");
        createTestPerson("18929629-0");
        createTestPerson("5103208-k");
        createTestPerson("5506396-6");
        createTestPerson("16741251-3");
        createTestPerson("11299723-7");
        createTestPerson("21192979-0");
        createTestPerson("9334799-4");
        createTestPerson("20408016-k");
        createTestPerson("18107587-2");
        createTestPerson("8492637-k");
        createTestPerson("12587261-1");
        createTestPerson("15392453-8");
        //when
        mockMvc.perform(get("/v1/restapi/people"))
        //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(20))); //default pagination is 20 per page.
    }
    
    @Test
    @Order(2)
    public void getPeoplePaginatedTest() throws Exception {
        //given
        //when
        mockMvc.perform(get("/v1/restapi/people").queryParam("page", "0").queryParam("size", "1"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }
    
    @Test
    @Order(3)
    public void getAllPeopleTest() throws Exception {
        //given
        //when
        mockMvc.perform(get("/v1/restapi/people/all"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(21)));
    }
    
    @Test
    @Order(4)
    public void getPeopleByIdTest() throws Exception {
        //given
        //when
        mockMvc.perform(get("/v1/restapi/people/1"))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rut").value("1-9"));
    }
    
    @Test
    @Order(5)
    public void getPeopleById404Test() throws Exception {
        //given
        //when
        mockMvc.perform(get("/v1/restapi/people/22"))
                //then
                .andExpect(status().is(404));
        //when
        mockMvc.perform(get("/v1/restapi/people/notanumber"))
                //then
                .andExpect(status().is(400));
    }
    
    @Test
    @Order(6)
    public void postPeopleWithValidJson() throws Exception {
        //given
        //when
        mockMvc.perform(
                post("/v1/restapi/people")
                .content(asJsonString(new Person("9422662-7", "FirstName", "LastName", 35, "English")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rut").value("9422662-7"));
    }
    
    @Test
    @Order(7)
    public void postPeopleWithInvalidJson() throws Exception {
        //given
        //when
        mockMvc.perform(
                post("/v1/restapi/people")
                .content("{{}")//invalid json
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is(400));
    }
    
    @Test
    @Order(8)
    public void postPeopleWithInvalidRut() throws Exception {
        //given
        String invalidRut = "1-8";
        //when
        mockMvc.perform(
                post("/v1/restapi/people")
                .content(asJsonString(new Person(invalidRut, "FirstName", "LastName", 35, "English")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().is(400));
    }
    
    @Test
    @Order(9)
    public void putPeopleTest() throws Exception {
        //given
        //when
        mockMvc.perform(
                put("/v1/restapi/people/1")
                .content(asJsonString(new Person("7183459-k", "FirstName", "LastName", 25, "English")))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.rut").value("7183459-k"));
        //assert that age of the object in DB is 25 for id 1. everyone was 35 prior to this test.
        Optional<Person> person = personRepository.findById(1L);
        assertThat(person.get().getAge()).isEqualTo(25);
        
    }
    
    @Test
    @Order(10)
    public void deletePeopleTest() throws Exception {
        //given
        long totalPersons = personRepository.count();
        //when
        mockMvc.perform(delete("/v1/restapi/people/1"))
                //then
                .andExpect(status().isOk());
        //assert that we deleted 1 from the DB
        assertThat(totalPersons - 1).isEqualTo(personRepository.count());
    }
    
    @Test
    @Order(11)
    public void deletePeopleNotFoundTest() throws Exception {
        //given
        long totalPersons = personRepository.count();
        //when 
        mockMvc.perform(delete("/v1/restapi/people/33"))
                //then
                .andExpect(status().is(404));
        //assert that no one was deleted from the DB
        assertThat(totalPersons).isEqualTo(personRepository.count());
    }
    
    @Test
    @Order(12)
    public void deletePeopleBadRequestTest() throws Exception {
        //given
        long totalPersons = personRepository.count();
        //when 
        mockMvc.perform(delete("/v1/restapi/people/notanumber"))
                //then
                .andExpect(status().is(400));
        //assert that no one was deleted from the DB
        assertThat(totalPersons).isEqualTo(personRepository.count());
    }
    
    private Person createTestPerson(String rut) {
        Person person = new Person(rut, "FirstName", "LastName", 35, "English");
        return personRepository.save(person);
    }
    
    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
