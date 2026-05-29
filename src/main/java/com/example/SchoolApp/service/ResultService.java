package com.example.SchoolApp.service;
import java.util.*;
import java.util.stream.Collectors;

import com.example.SchoolApp.SchoolModels;
import com.example.SchoolApp.events.CreateResultEvent;
import com.example.SchoolApp.wrapper.ModelWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.model.Result;
import com.example.SchoolApp.model.Student;
import com.example.SchoolApp.repository.ResultRepository;
import com.example.SchoolApp.repository.StudentRepository;

import static io.swagger.v3.core.jackson.TypeNameResolver.std;

@Service
public class ResultService {
	private ResultRepository resultRepository;
	public static String[] subjects = new String[]{
			"Mathematics", "English", "Basic Science", "Social Studies", "Computer","handwriting",
			"Verbal Reasoning", "Quantitative", "CRK", "Creative Art"
	};

	@Autowired
	public ResultService(ResultRepository resultRepository) {
		this.resultRepository = resultRepository;
	}

	private ResultDto mapToResultDto(Result result) {
		return ResultDto.builder()
				.Id(result.getId())
				.score(result.getScore())
				.term(result.getTerm())
				.type(result.getType())
				.schoolId(result.getSchoolId())
				.studentId(result.getStudentId())
				.classOfStudent(result.getClassOfStudent())
				.subjectName(result.getSubjectName())
				.build();
	}
	private Result mapToResult(ResultDto result) {
		return Result.builder()
				.score(result.getScore())
				.term(result.getTerm())
				.type(result.getType())
				.schoolId(result.getSchoolId())
				.studentId(result.getStudentId())
				.subjectName(result.getSubjectName())
				.classOfStudent(result.getClassOfStudent())
				.Id(result.getId())
				.build();
	}

	public void selectionSort(ArrayList<ResultDto> resultArrayList) {
		ResultDto temp;
		for (int i = 0; i < resultArrayList.size(); i++) {
			int min = i;
			int j = i;
			while (j < resultArrayList.size()) {
				if (resultArrayList.get(min).getId() > resultArrayList.get(j).getId())
					min = j;
				j++;
			}
			temp = resultArrayList.get(i);
			resultArrayList.set(i, resultArrayList.get(min));
			resultArrayList.set(min, temp);
		}
		return;
	}


	@EventListener
	public void createResultEvent(CreateResultEvent event) {
		String[] term = {"1st term", "2nd term", "3rd term"};
		String[] types = {"test", "exam"};
		Result result;
		for (String str : types) {
			for (String s : term) {
				saveSubjects(event.schoolID(),
						event.studentID(),
						event.classOfStudent(),
						s, str); //save to database
			}
		}
		return;
	}
	public List<Result> getClassResult(
			String term, String type, Long studentId) {
		return resultRepository.findAllByTermAndTypeAndStudentId(
				term, type, studentId);

	}

	public List<ResultDto> getStudentResult(Long studentId, String term, String type) {
		return resultRepository.findAllByStudentIdAndTermAndType(studentId, term, type)
				.stream().map(this::mapToResultDto)
				.collect(Collectors.toList());
	}

	public double getAverageScore(String className, Long schoolId) {
		return resultRepository.sumTestScore(className, SchoolModels.CURRENT_TERM, schoolId);
	}
	public boolean saveResult(Long studentId, String term,
	                       String type, Map<String, Integer> body) {
		if(!(body.size() == subjects.length &&
				body.keySet().containsAll(Arrays.asList(subjects))) ) {
			return false;
		}
		List<ResultDto> resultList = getStudentResult(studentId, term, type);
		HashMap<String, ResultDto> resultMap = new HashMap<String, ResultDto>();
		resultList.forEach(
				result -> {
					resultMap.put(result.getSubjectName(), result);
				}
		);
		body.forEach(
				(subjectName, score) -> {
					resultMap.get(subjectName).setScore(score);
				}
		);
		resultList = new ArrayList<>(resultMap.values());
		resultRepository.saveAll(
				resultList.stream().map(
                this::mapToResult).
				collect(Collectors.toList())
		);
		return true;
	}
	/**
	 * -------------------PRIVATE METHOD--------------------------------------------------
	 */

	/**
	 * -----------------------------------------
	 *
	 * @param schoolId
	 * @param studentId
	 * @param classOfStudent
	 * @param term
	 * @param type
	 */
	private void saveSubjects(Long schoolId, Long studentId,
							  String classOfStudent, String term, String type) {
		List<Result> results = new ArrayList<>();
		for(String s : subjects){
			results.add(Result.builder()
							.schoolId(schoolId)
					.studentId(studentId)
					.term(term)
					.classOfStudent(classOfStudent)
							.subjectName(s)
							.type(type)
					.build());
		}
		resultRepository.saveAll(results);
	}
}
