package com.project.controller;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import com.project.service.impl.StudentServiceImpl;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/v1.0")
public class StudentController {

  @Autowired
  private StudentServiceImpl studentServiceImpl;
  @Autowired
  private StudentRepository studentRepository;
  
  
  @GetMapping("/student")
 public Mono<ResponseEntity<Flux<Student>>> findAll() {
    return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8)
    .body(studentServiceImpl.findAll()));
  }
  
  @GetMapping("/student/{id}")
  public Mono<ResponseEntity<Student>> findById(@PathVariable String id) {
    return studentServiceImpl.findById(id).map(p -> ResponseEntity.ok()
    .contentType(MediaType.APPLICATION_JSON_UTF8)
    .body(p)).defaultIfEmpty(ResponseEntity.notFound().build());
  }


  @GetMapping("numberDocument/{numberDocument}")
  public Mono<ResponseEntity<Student>> findBynumberDocument(@PathVariable String numberDocument) {
    return studentServiceImpl.findBynumberDocument(numberDocument)
    .map(p -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON_UTF8).body(p))
    .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @GetMapping("fullName/{fullName}")
  public Flux<ResponseEntity<Student>> findFullName(@PathVariable String fullName) {
    return studentServiceImpl.findByFullName(fullName).map(s -> ResponseEntity.ok()
    .contentType(MediaType.APPLICATION_JSON_UTF8).body(s))
    .defaultIfEmpty(ResponseEntity.notFound().build());
  }
  
  @PostMapping
  public Mono<ResponseEntity<Map<String, Object>>> create(
      @Valid @RequestBody Mono<Student> studentMono) {
    Map<String, Object> reply = new HashMap<String, Object>();
    return studentMono.flatMap(student -> {
      if (student.getBirthdate() == null) {
        student.setBirthdate(new Date());
      }
      return studentServiceImpl.save(student).map(
        p -> {
          reply.put("student", p);
          reply.put("message", "Student created successfully");
          reply.put("dateTime", new Date());
          return ResponseEntity.created(URI.create("/api/v1.0".concat(p.getId())))
            .contentType(MediaType.APPLICATION_JSON_UTF8).body(reply);
        });
    });
  }
  
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Student>> save(@RequestBody Student student,@PathVariable String id) {
    return studentServiceImpl.findById(id).flatMap(
      s -> {
        s.setFullName(student.getFullName());
        s.setGender(student.getGender());
        s.setBirthdate(student.getBirthdate());
        s.setTypeDocument(student.getTypeDocument());
        s.setNumberDocument(student.getNumberDocument());
    
        return studentServiceImpl.save(s);
      }).map(
          s -> ResponseEntity.created(
         URI.create("/api/v1.0".concat(s.getId())))
.contentType(MediaType.APPLICATION_JSON_UTF8).body(s)).defaultIfEmpty(ResponseEntity
.notFound().build());
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable("id") String id) {
    studentServiceImpl.delete(id).subscribe();
  }
  
  @GetMapping("student/date/{birthdate}/{birthdate1}")
  public Flux<Student> findByBirthdateBetween(@PathVariable("birthdate")
      @DateTimeFormat(iso = ISO.DATE) Date birthdate,@PathVariable("birthdate1")
      @DateTimeFormat(iso = ISO.DATE) Date birthdate1) {
    return studentRepository.findByBirthdateBetween(birthdate, birthdate1);
  }

}