/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.people.apirest.controller;

import io.people.apirest.exception.BadRequestException;
import io.people.apirest.exception.ResourceNotFoundException;
import io.people.apirest.model.Person;
import io.people.apirest.repository.PersonRepository;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author rulyone
 */
@RestController
@RequestMapping("/v1/apirest")
public class PersonController {
    
    @Autowired
    private PersonRepository personRepository;
    
    @GetMapping("/people")
    public List<Person> findPaginated(Pageable pageable) {
        return personRepository.findAll(pageable).getContent();
    }
    
    @GetMapping("/people/all")
    public List<Person> findAll() {
        return personRepository.findAll(); //ToDo Technical Debt: implement a stream to avoid in memory of lots of objects when processing this request. 
    }
    
    @GetMapping("/people/{id}")
    public ResponseEntity<Person> findPersonById(@PathVariable(value = "id") Long personId) throws ResourceNotFoundException {
        Person person = personRepository.findById(personId).orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + personId));
        return ResponseEntity.ok().body(person);
    }
    
    @PostMapping("/people")
    public Person createPerson(@Valid @RequestBody Person person, Errors errors) throws BadRequestException {
        if (errors.hasErrors()) {
            throw new BadRequestException("Bad Request.");
        }
        if (person != null && person.getId() != null) {
            throw new BadRequestException("Bad Request. You can't have an ID attribute in the json request payload to create a new person.");
        }
        return personRepository.save(person);
    }
    
    @PutMapping("/people/{id}")
    public Person updatePerson(@Valid @RequestBody Person inputPerson, @PathVariable Long id, Errors errors) throws BadRequestException, ResourceNotFoundException {
        if (errors.hasErrors()) {
            throw new BadRequestException("Bad Request.");
        }
        
        if (id == null) {
            throw new BadRequestException("Bad Request.");
        }
        
        return personRepository.findById(id)
                .map(person -> {
                    person.setAge(inputPerson.getAge());
                    person.setCourse(inputPerson.getCourse());
                    person.setFirstName(inputPerson.getFirstName());
                    person.setLastName(inputPerson.getLastName());
                    person.setRut(inputPerson.getRut());
                    return personRepository.save(person);
                }).orElseThrow(() -> new ResourceNotFoundException("Person not found for this id :: " + id));
    }
    
    @DeleteMapping("/people/{id}")
    public void deletePerson(@PathVariable Long id) throws ResourceNotFoundException {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
        }else{
            throw new ResourceNotFoundException("Person not found for this id :: " + id);
        }
    }

}
