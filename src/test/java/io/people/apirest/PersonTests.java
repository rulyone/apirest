/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.people.apirest;

import io.people.apirest.model.Person;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 *
 * @author rulyone
 */
@SpringBootTest
class PersonTests {
    
    @Autowired
    private Validator validator;
    
    @Test
    public void testPersonValidator() throws Exception {
        //given
        String goodRut = "1-9";
        String nullRut = null;
        String badRut = "1-8";
        String emptyRut = "";
        
        String firstName = "FirstName";
        String lastName = "LastName";
        int age = 35;
        String course = "English";
        String emptyString = "";
        Set<ConstraintViolation<Person>> violations;
        //when
        Person newPerson = new Person(goodRut, firstName, lastName, age, course);
        violations = validator.validate(newPerson);
        //then
        assertEquals(0, violations.size());
        //when 5 violations (null rut and invalid rut are counted separate because of @NotEmpty and @Rut...)
        Person newPerson2 = new Person(nullRut, emptyString, emptyString, age, emptyString);
        violations = validator.validate(newPerson2);
        //then
        assertEquals(5, violations.size());
        //when 4 violations
        Person newPerson3 = new Person(badRut, emptyString, emptyString, age, emptyString);
        violations = validator.validate(newPerson3);
        //then
        assertEquals(4, violations.size());
        //when 5 violations (empty rut and invalid rut are counted separate because of @NotEmpty and @Rut...)
        Person newPerson4 = new Person(emptyRut, emptyString, emptyString, age, emptyString);
        violations = validator.validate(newPerson4);
        //then
        assertEquals(5, violations.size());
        //when 6 violations (empty rut and invalid rut are counted separate because of @NotEmpty and @Rut...)
        Person newPerson5 = new Person(emptyRut, emptyString, emptyString, null, emptyString);
        violations = validator.validate(newPerson5);
        //then
        assertEquals(6, violations.size());
    }
}
