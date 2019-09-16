package com.project.controller;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import com.project.service.StudentServiceImpl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController("/api/student")
public class StudentController {

  @Autowired
  private StudentServiceImpl studentServiceImpl;
  private StudentRepository studentRepository;
    
  @GetMapping
  public Flux<Student> findAll() {
    return studentServiceImpl.findAll();
  }

  @GetMapping("/{id}")
  public Mono<Student> getStudent(@PathVariable String id) {
    return studentServiceImpl.findById(id);
  }


  @GetMapping("/numberDocument")
  public Mono<Student> findByDocument(@RequestParam("number") String numberDocument) {
    return studentServiceImpl.findBynumberDocument(numberDocument);
  }


  @GetMapping("/fullName")
  public Flux<Student> findFullName(@RequestParam("filter") String fullName) {
    return studentServiceImpl.findByFullName(fullName);
  }
  
  @PostMapping
  public void create(@RequestBody Student student) {
    studentServiceImpl.create(student);
  }
  
  @PutMapping
  public Mono<Student> save(@RequestBody Student student) {
    return studentServiceImpl.save(student);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") String id) {
    studentServiceImpl.delete(id).subscribe();
  }
  
  @GetMapping("/date/{birthdate}/{birthdate1}")
  public Flux<Student> findByBirthdateBetween(@PathVariable("birthdate")
      @DateTimeFormat(iso = ISO.DATE) Date birthdate,@PathVariable("birthdate1")
      @DateTimeFormat(iso = ISO.DATE) Date birthdate1) {
    return studentRepository.findByBirthdateBetween(birthdate, birthdate1);
  }

}