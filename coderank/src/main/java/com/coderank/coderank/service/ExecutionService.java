package com.coderank.coderank.service;

import com.coderank.coderank.entity.Submission;
import com.coderank.coderank.repository.SubmissionRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutionService {

    private final SubmissionRepository submissionRepository;

    public ExecutionService(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Async
    public void executeSubmission(Submission submission) {

        try {

            submission.setStatus("RUNNING");
            submissionRepository.save(submission);

            String language = submission.getLanguage();
            ProcessBuilder pb;

            // ---------------- PYTHON ----------------

            if (language.equalsIgnoreCase("python")) {

                File pyFile = File.createTempFile("code", ".py");

                BufferedWriter writer = new BufferedWriter(new FileWriter(pyFile));
                writer.write(submission.getCode());
                writer.close();

                pb = new ProcessBuilder(
                        "docker",
                        "run",
                        "--rm",

                        "--memory=128m",
                        "--cpus=0.5",
                        "--network",
                        "none",

                        "-v",
                        pyFile.getParent() + ":/app",

                        "python",

                        "python",
                        "/app/" + pyFile.getName()
                );
            }

            // ---------------- JAVA ----------------

            else if (language.equalsIgnoreCase("java")) {

                File javaFile = new File(System.getProperty("java.io.tmpdir"), "Main.java");

                BufferedWriter writer = new BufferedWriter(new FileWriter(javaFile));
                writer.write(submission.getCode());
                writer.close();

                // Compile Java
                ProcessBuilder compileProcess =
                        new ProcessBuilder("javac", javaFile.getAbsolutePath());

                Process compile = compileProcess.start();

                int compileResult = compile.waitFor();

                // Compilation errors
                if (compileResult != 0) {

                    BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(compile.getErrorStream())
                    );

                    StringBuilder compileErrors = new StringBuilder();
                    String line;

                    while ((line = errorReader.readLine()) != null) {
                        compileErrors.append(line).append("\n");
                    }

                    submission.setOutput("Compilation Error:\n" + compileErrors);
                    submission.setStatus("FAILED");

                    submissionRepository.save(submission);

                    return;
                }

                pb = new ProcessBuilder(
                        "java",
                        "-cp",
                        javaFile.getParent(),
                        "Main"
                );
            }

            // ---------------- C++ ----------------

            else if (language.equalsIgnoreCase("cpp")) {

                File cppFile = new File(System.getProperty("java.io.tmpdir"), "main.cpp");

                BufferedWriter writer = new BufferedWriter(new FileWriter(cppFile));
                writer.write(submission.getCode());
                writer.close();

                File outputFile = new File(System.getProperty("java.io.tmpdir"), "main");

                // Compile C++
                ProcessBuilder compileProcess = new ProcessBuilder(
                        "g++",
                        cppFile.getAbsolutePath(),
                        "-o",
                        outputFile.getAbsolutePath()
                );

                Process compile = compileProcess.start();

                int compileResult = compile.waitFor();

                // Compilation errors
                if (compileResult != 0) {

                    BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(compile.getErrorStream())
                    );

                    StringBuilder compileErrors = new StringBuilder();
                    String line;

                    while ((line = errorReader.readLine()) != null) {
                        compileErrors.append(line).append("\n");
                    }

                    submission.setOutput("Compilation Error:\n" + compileErrors);
                    submission.setStatus("FAILED");

                    submissionRepository.save(submission);

                    return;
                }

                pb = new ProcessBuilder(outputFile.getAbsolutePath());
            }

            else {

                submission.setOutput("Unsupported Language");
                submission.setStatus("FAILED");

                submissionRepository.save(submission);

                return;
            }

            // ---------------- EXECUTION ----------------

            Process process = pb.start();

            BufferedReader outputReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );

            StringBuilder output = new StringBuilder();

            // Timeout handling
            boolean finished = process.waitFor(3, TimeUnit.SECONDS);

            if (!finished) {

                process.destroy();

                submission.setOutput("Execution Timed Out!");
                submission.setStatus("FAILED");

                submissionRepository.save(submission);

                return;
            }

            String line;

            // Standard Output
            while ((line = outputReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            // Errors
            while ((line = errorReader.readLine()) != null) {
                output.append(line).append("\n");
            }

            submission.setOutput(output.toString());
            submission.setStatus("COMPLETED");

            submissionRepository.save(submission);

        }

        catch (Exception e) {

            submission.setOutput("Error: " + e.getMessage());
            submission.setStatus("FAILED");

            submissionRepository.save(submission);
        }
    }
}