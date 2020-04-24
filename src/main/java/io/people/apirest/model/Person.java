/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.people.apirest.model;

import io.people.apirest.validation.Rut;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rulyone
 */

@Entity
@Table(name = "persons")
public class Person implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(name = "rut", nullable = false)
    @NotEmpty
    @Rut
    private String rut;
    
    @Column(name = "first_name", nullable = false)
    @NotEmpty
    private String firstName;
    
    @Column(name = "last_name", nullable = false)
    @NotEmpty
    private String lastName;
    
    @Column(name = "age", nullable = false)
    @NotNull
    private Integer age; //ToDo Technical Debt: best practice would be to save the birthday instead of age.
    
    @Column(name = "course", nullable = false)
    @NotEmpty    
    private String course; //ToDo Technical Debt: best practice would be to create a Course entity with its own attributes.

    public Person() {
    }

    public Person(String rut, String firstName, String lastName, Integer age, String course) {
        this.rut = rut;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
    
}
