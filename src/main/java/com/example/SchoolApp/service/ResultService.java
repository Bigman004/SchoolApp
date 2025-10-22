package com.example.SchoolApp.service;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.model.Result;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.repository.ResultRepository;
import com.example.SchoolApp.repository.StudentRepository;

@Service
public class ResultService {
	private ResultRepository resultRepository;
	private StudentRepository studentRepository;
	
	@Autowired
	public ResultService(ResultRepository resultRepository, StudentRepository studentRepository){
		this.resultRepository = resultRepository;
		this.studentRepository = studentRepository;
	}
	
	public void saveResult(Result result, Long studentId) {
		Student std = studentRepository.findById(studentId).get();
		result.setStudent(std);
		resultRepository.save(result);
	}
	public void updateResult(ResultDto resultDto, long resultId) {
		Result result = resultRepository.findById(resultId).get();
		result.setBasicScience(resultDto.getBasicScience());
		result.setCivicEducation(resultDto.getCivicEducation());
		result.setCRK(resultDto.getCRK());
		result.setEnglish(resultDto.getEnglish());
		result.setMath(resultDto.getMath());
		result.setPHE(resultDto.getPHE());
		result.setSocialStudies(resultDto.getSocialStudies());
		resultRepository.save(result);	
		}
	public Long getResultId(Long studentId, String term) {
		if(!(term.equals("1st term")||term.equals("2nd term")|| term.equals("3rd term"))) {
			throw new IllegalArgumentException("you did not add a valid term");
		}
		Result result;
		List<Result> list = studentRepository.
				findById(studentId).get().getResults();
		for(var i = 0; i < 3; i++) {
			result = list.get(i);
			if(result.getTerm().equals(term))
				return result.getId();
		}
		return null;
	}
}
