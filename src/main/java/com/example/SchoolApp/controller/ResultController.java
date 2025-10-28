package com.example.SchoolApp.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

import lombok.experimental.SuperBuilder;

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
		List<ResultDto> results = resultService.getStudentResult(studentId);
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
	@GetMapping("/")
	public ResponseEntity<?> resultPageApi() {
		String[] terms = {"1st term", "2nd term", "3rd term"};
		ArrayList<ResultDto> results;
		List<List<StdResultDetails>> detailList = new ArrayList<>();
		ArrayList<StdResultDetails> list;
		for(String term: terms) {
			results = (ArrayList<ResultDto>) resultService.ResultByTerm(term);
			resultService.selectionSort(results);
			list = new ArrayList<StdResultDetails>();
			for(ResultDto result: results) {
				StdResultDetails resultDetails = new StdResultDetails(result);
				list.add(resultDetails);
			}
			detailList.add(list);
		}
		return new ResponseEntity<>(detailList, HttpStatus.OK);
	}
	
	private class StdResultDetails extends ResultDto{
		String firstName;
		String lastName;
		StdResultDetails(ResultDto result){
			this.setTerm(result.getTerm());
			this.setBasicScience(result.getBasicScience());
			this.setCivicEducation(result.getCivicEducation());
			this.setCRK(result.getCRK());
			this.setEnglish(result.getEnglish());
			this.setId(result.getId());
			this.setMath(result.getMath());
			this.setPHE(result.getPHE());
			this.setSocialStudies(result.getSocialStudies());
			this.setStudentId(result.getStudentId());
			this.firstName = studentService.getStudentDtoById(result.getStudentId()).getFirstName();
			this.lastName = studentService.getStudentDtoById(result.getStudentId()).getLastName();
		}
		public String getFirstName() { return firstName;}
		public String getLastName() {return lastName;}
	}
	private class WrapperRequest{
		private List<StdResultDetails> firstTerm, secondTerm, thirdTerm;
		
		WrapperRequest(List<StdResultDetails>... list){
			this.firstTerm = list[0];
			this.secondTerm = list[1];
			this.thirdTerm = list[2];
		}
		public List<?> getFirstTerm(){return firstTerm;}
		public List<?> getSecondTerm(){return secondTerm;}
		public List<?> getThirdTerm(){return thirdTerm;}
	}
}
