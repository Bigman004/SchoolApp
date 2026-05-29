package com.example.SchoolApp.controller;

import com.example.SchoolApp.dto.ResultDto;
import com.example.SchoolApp.dto.StudentDto;
import com.example.SchoolApp.model.Result;
import com.example.SchoolApp.security.SecurityUtill;
import com.example.SchoolApp.service.ResultService;
import com.example.SchoolApp.service.StudentService;
import com.example.SchoolApp.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("result")
public class  ResultController {

    private ResultService resultService;
    private StudentService studentService;
    private TeacherService teacherService;

    @Autowired
    public ResultController(ResultService resultService, StudentService studentService,  TeacherService teacherService) {
        this.resultService = resultService;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @GetMapping("/")
    public ResponseEntity<ResultRequest> getClassResult(
            @RequestParam(value = "term", defaultValue = "1st term")  String term,
            @RequestParam(value = "type", defaultValue = "test") String type
    ) {
        String username = SecurityUtill.getSessionLoader();
        String classOfStudent = teacherService.getTeacher(username).getTeacherClass();
        Long schoolId = teacherService.getTeacher(username).getSchoolId();
        return new ResponseEntity<>(
                getStudentResult(term, type, classOfStudent, schoolId),
                HttpStatus.OK
        );
    }
    @GetMapping("/{studentId}")
    public ResponseEntity<?> getStudentResult(
            @PathVariable Long studentId,
            @RequestParam(value = "term", defaultValue = "")  String term,
            @RequestParam(value = "type", defaultValue = "") String type
    )
    {
        if(term.isEmpty() || type.isEmpty())
            return new ResponseEntity<>("term and type must contain a value", HttpStatus.BAD_REQUEST);
        List<ResultDto> results = resultService.getStudentResult(studentId, term, type);
        Map<String, Integer> map = new HashMap<>();
        for(ResultDto result : results) {
            map.put(result.getSubjectName(), result.getScore());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    @PostMapping("/{studentId}")
    public ResponseEntity<?> setStudentResult(
            @PathVariable Long studentId,
            @RequestBody Map<String, Integer> body,
            @RequestParam(value = "term", defaultValue = "")  String term,
            @RequestParam(value = "type", defaultValue = "") String type
    ){
        if(term.isEmpty() || type.isEmpty())
            return new ResponseEntity<>("term and type must contain a value", HttpStatus.BAD_REQUEST);
        resultService.saveResult(studentId, term, type, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResultRequest getStudentResult(String term, String type, String classOfStudent, Long schoolId){
        String[] tableHeader;
        LinkedHashMap<String, List<Result>> tableContent = new LinkedHashMap<>();
        for(StudentDto student: studentService.getStudentsByClass(classOfStudent, schoolId)){
            tableContent.put(
                    student.getId()+" "+student.getLastName()+ " " + student.getFirstName(),
                    resultService.getClassResult(term, type, student.getId())
            );

        }
        tableHeader = ResultService.subjects;
        return new ResultRequest(tableHeader, tableContent);
    }


    public class ResultRequest {
        private String[] tableHeader;
        private LinkedHashMap<String, List<Result>> tableContent;

        public ResultRequest(String[] tableHeader, LinkedHashMap<String, List<Result>> tableContent) {
            this.tableHeader = tableHeader;
            this.tableContent = tableContent;
        }
        public LinkedHashMap<String, List<Result>> getTableContent() {
            return tableContent;
        }
        public String[] getTableHeader() {
            return tableHeader;
        }

    }
}