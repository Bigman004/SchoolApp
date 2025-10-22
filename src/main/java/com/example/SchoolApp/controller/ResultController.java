package com.example.SchoolApp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.model.Result;
import com.example.SchoolApp.service.ResultService;
import com.example.SchoolApp.service.StudentService;

@RestController
@RequestMapping("/result")
public class ResultController {
	private ResultService resultService;
	private StudentService studentService;
	
	@Autowired
	public ResultController(ResultService resultService, StudentService studentService) {
		this.resultService = resultService;
		this.studentService = studentService;
	}
	
	@GetMapping("/{studentId}")
	public ResponseEntity<?> studentResult(@PathVariable long studentId, Model model) {
		List<ResultDto> results = studentService.getStudentResult(studentId);
		return new ResponseEntity<>(results, HttpStatus.OK);
	}
	@PostMapping("{studentId}/{term}")
	public String uploadResult(@PathVariable String term, @PathVariable Long studentId,
			@RequestBody ResultDto result) {
		System.out.println(result);
		Long Id = resultService.getResultId(studentId, term);
		resultService.updateResult(result, Id);
		return "teacherPage";
	}
	
	
}
