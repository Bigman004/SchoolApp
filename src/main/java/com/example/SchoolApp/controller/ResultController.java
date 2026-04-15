package com.example.SchoolApp.controller;

import java.util.ArrayList;
import org.slf4j.Logger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.example.SchoolApp.dto.PrintResultDto;
import org.slf4j.LoggerFactory;
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

	private static final Logger logger = LoggerFactory.getLogger(ResultController.class);
	
	@Autowired
	public ResultController(ResultService resultService, StudentService studentService) {
		this.resultService = resultService;
		this.studentService = studentService;
	}
	
//	@GetMapping("/{studentId}")
//	public ResponseEntity<?> studentResult(@PathVariable long studentId, Model model) {
//		List<ResultDto> results = resultService.getStudentResult(studentId);
//		return new ResponseEntity<>(results, HttpStatus.OK);
//	}
	@GetMapping("{info}")
	public ResponseEntity<?> getResults(@PathVariable String info) {
		String[] str = info.split("-");
		ResultDto result = resultService.getStudentResult(Long.parseLong(str[0]), str[1], str[2]);
		logger.info("student result: "+ info);
		return ResponseEntity.ok(result);
	}
	@PostMapping("{info}")
	public String uploadResult(@PathVariable String info,
			@RequestBody ResultDto result) {
		System.out.println(result);
		String[] str = info.split("-");
		long Id = resultService.getResultId(Long.parseLong(str[0]), str[1], str[2]);
		resultService.updateResult(result, Id);
		logger.info("upload student result: "+ info);
		return "teacherPage";
	}
	@GetMapping("{studentId}/{term}")
	public ResponseEntity<?> getStudentResult(@PathVariable String term,
											  @PathVariable Long studentId) {
		ResultDto test = resultService.getStudentResult(studentId, term, "test");
		ResultDto exam = resultService.getStudentResult(studentId, term, "exam");
		logger.info("student result print: "+ studentId);

		return new ResponseEntity<>(PrintResultDto.builder()
				.examResult(exam)
				.testResult(test)
				.studentFirstName(studentService.getStudentDtoById(studentId).getFirstName())
				.studentLastName(studentService.getStudentDtoById(studentId).getLastName())
				.studentClass(studentService.getStudentDtoById(studentId).getClassOfStudent())
				.regNumber(studentService.getStudentDtoById(studentId).getRegNumber())
				.build(),
				HttpStatus.OK);
	}
	@GetMapping("/")
	public ResponseEntity<?> resultPageApi() {
		String[] terms = {"1st term", "2nd term", "3rd term"};
		String[] types = {"exam", "test"};
		List<List<StdResultDetails>> detailList = new ArrayList<List<StdResultDetails>>();
		for (String term : terms) {
			for (String type : types) {
				detailList.add(resultService.
						getResultsByTermAndType(term, type)
						.stream().map(result -> new StdResultDetails(result))
						.collect(Collectors.toList())
				);
			}
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
			this.setType(result.getType());
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
