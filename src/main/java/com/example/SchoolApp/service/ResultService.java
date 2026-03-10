package com.example.SchoolApp.service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
		result.setComputer(resultDto.getComputer());
		result.setRhymes(resultDto.getRhymes());
		result.setHandwriting(result.getHandwriting());
		result.setQuantitative(resultDto.getQuantitative());
		result.setVerbalReasoning(resultDto.getVerbalReasoning());
		result.setCreativeArt(resultDto.getCreativeArt());
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
	public List<ResultDto> ResultByTerm(String term){
		List<Result> list = resultRepository.findAllByTerm(term);
		return list.stream()
				.map(result -> mapToResultDto(result))
				.collect(Collectors.toList());
	}
	
	private ResultDto mapToResultDto(Result result) {
		ResultDto resultDto =  new ResultDto();
		resultDto.setBasicScience(result.getBasicScience());
		resultDto.setCivicEducation(result.getCivicEducation());
		resultDto.setCRK(result.getCRK());
		resultDto.setEnglish(result.getEnglish());
		resultDto.setId(result.getId());
		resultDto.setMath(result.getMath());
		resultDto.setPHE(result.getPHE());
		resultDto.setSocialStudies(result.getSocialStudies());
		resultDto.setTerm(result.getTerm());
		resultDto.setStudentId(result.getStudent().getId());
		resultDto.setComputer(result.getComputer());
		resultDto.setRhymes(result.getRhymes());
		resultDto.setHandwriting(result.getHandwriting());
		resultDto.setQuantitative(result.getQuantitative());
		resultDto.setVerbalReasoning(result.getVerbalReasoning());
		resultDto.setCreativeArt(result.getCreativeArt());
		return resultDto;
	}
	public List<ResultDto> getStudentResult(Long studentId){
		List<Result> results = studentRepository.findById(studentId).get().getResults();
		return results.stream().
				map(result -> mapToResultDto(result))
				.collect(Collectors.toList());
		
	}
	public void selectionSort(ArrayList<ResultDto> resultArrayList) {
		ResultDto temp;
		for(int i =0; i < resultArrayList.size(); i++) {
			int min = i;
			int j = i;
			while(j < resultArrayList.size()) {
				if(resultArrayList.get(min).getId() > resultArrayList.get(j).getId() )
					min = j;
				j++;
			}
			temp = resultArrayList.get(i);
			resultArrayList.set(i, resultArrayList.get(min));
			resultArrayList.set(min, temp);
		}
		return;
	}
}
