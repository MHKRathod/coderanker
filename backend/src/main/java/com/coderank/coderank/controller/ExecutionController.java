package com.coderank.coderank.controller;

import com.coderank.coderank.entity.Submission;
import com.coderank.coderank.model.CodeRequest;
import com.coderank.coderank.repository.SubmissionRepository;
import com.coderank.coderank.service.ExecutionService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class ExecutionController {

    private final SubmissionRepository submissionRepository;
    private final ExecutionService executionService;

    public ExecutionController(
            SubmissionRepository submissionRepository,
            ExecutionService executionService
    ) {
        this.submissionRepository = submissionRepository;
        this.executionService = executionService;
    }

    @PostMapping("/submit")
    public String submitCode(@RequestBody CodeRequest request) {

        Submission submission = new Submission();

        submission.setCode(request.getCode());
        submission.setLanguage(request.getLanguage());
        submission.setStatus("QUEUED");

        submissionRepository.save(submission);

        // async execution
        executionService.executeSubmission(submission);

        return "Submission ID: " + submission.getId();
    }
    @GetMapping("/result/{id}")
    public Submission getResult(@PathVariable Long id) {

        return submissionRepository.findById(id).orElse(null);
    }
}