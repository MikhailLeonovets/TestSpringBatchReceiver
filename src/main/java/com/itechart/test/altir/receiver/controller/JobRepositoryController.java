package com.itechart.test.altir.receiver.controller;

import com.itechart.test.altir.receiver.controller.model.ExecutionContextDto;
import com.itechart.test.altir.receiver.controller.model.JobInstWithParamsAndConfigLoc;
import com.itechart.test.altir.receiver.controller.model.JobInstWithStepNameDto;
import com.itechart.test.altir.receiver.controller.model.JobNameWithParamsDto;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Collection;

import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.CREATE_JOB_EXECUTION_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.CREATE_JOB_EXEC_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.CREATE_JOB_INSTANCE_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.CREATE_SIMPLE_JOB_EXECUTION_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_LAST_JOB_EXEC_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_LAST_STEP_EXEC_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.GET_STEP_EXEC_COUNT;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.IS_JOB_INSTANCE_EXISTS_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.JOB_ADD_ALL_STEPS_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.JOB_ADD_STEP_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.JOB_EXEC_UPDATE_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.STEP_EXEC_UPDATE_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.UPDATE_EXECUTION_CONTEXT_URL;
import static com.itechart.test.altir.receiver.controller.storage.UrlStorage.UPDATE_JOB_EXECUTION_CONTEXT_URL;

@RestController
public class JobRepositoryController {
	private final JobRepository jobRepository;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public JobRepositoryController(JobRepository jobRepository, StepBuilderFactory stepBuilderFactory) {
		this.jobRepository = jobRepository;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@GetMapping(IS_JOB_INSTANCE_EXISTS_URL)
	public ResponseEntity<?> isJobInstExists(@RequestBody JobNameWithParamsDto jobNameWithParamsDto) {
		Boolean isExists = jobRepository.isJobInstanceExists(jobNameWithParamsDto.getJobName(),
				jobNameWithParamsDto.getJobParameters());
		return ResponseEntity.ok(isExists);
	}

	@PostMapping(CREATE_JOB_INSTANCE_URL)
	public ResponseEntity<?> createJobInstance(@RequestBody JobNameWithParamsDto jobNameWithParamsDto) {
		return ResponseEntity.ok(jobRepository.createJobInstance(jobNameWithParamsDto.getJobName(),
				jobNameWithParamsDto.getJobParameters()));
	}

	@PostMapping(CREATE_JOB_EXECUTION_URL)
	public ResponseEntity<?> createJobExec(@RequestBody JobNameWithParamsDto jobNameWithParamsDto)
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobRestartException {
		JobExecution jobExecution = jobRepository.createJobExecution(jobNameWithParamsDto.getJobName(),
				jobNameWithParamsDto.getJobParameters());
		return ResponseEntity.ok(jobExecution);
	}

	@PutMapping(JOB_EXEC_UPDATE_URL)
	public ResponseEntity<?> updateExecContext(@RequestBody JobExecution jobExecution) {
		jobRepository.update(jobExecution);
		return ResponseEntity.ok("Updated successfully");
	}

	@PostMapping(JOB_ADD_STEP_URL)
	public ResponseEntity<?> addStepExec(@RequestBody StepExecution stepExecution) {
		jobRepository.add(stepExecution);
		return ResponseEntity.ok("Step added successfully");
	}

	@PostMapping(JOB_ADD_ALL_STEPS_URL)
	public ResponseEntity<?> addAllStepsExec(@RequestBody Collection<StepExecution> stepExecutions) {
		jobRepository.addAll(stepExecutions);
		return ResponseEntity.ok("Steps added successfully");
	}

	@PutMapping(STEP_EXEC_UPDATE_URL)
	public ResponseEntity<?> updateStepExec(@RequestBody ExecutionContextDto executionContextDto)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<StepExecution> stepExecutionConstructor = StepExecution.class.getDeclaredConstructor(String.class);
		stepExecutionConstructor.setAccessible(true);
		StepExecution stepExecution = stepExecutionConstructor.newInstance(executionContextDto.getStepName());
		stepExecution.setId(executionContextDto.getExecutionId());
		stepExecution.setExecutionContext(executionContextDto.getExecutionContext());
		jobRepository.update(stepExecution);
		return ResponseEntity.ok("Step Execution updated successfully");
	}

	@PutMapping(UPDATE_EXECUTION_CONTEXT_URL)
	public ResponseEntity<?> updateExecContext(@RequestBody ExecutionContextDto executionContextDto)
			throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Constructor<StepExecution> stepExecutionConstructor = StepExecution.class.getDeclaredConstructor(String.class);
		stepExecutionConstructor.setAccessible(true);
		StepExecution stepExecution = stepExecutionConstructor.newInstance(executionContextDto.getStepName());
		stepExecution.setId(executionContextDto.getExecutionId());
		stepExecution.setExecutionContext(executionContextDto.getExecutionContext());
		jobRepository.updateExecutionContext(stepExecution);
		return ResponseEntity.ok("Execution Context updated successfully");
	}

	@PutMapping(UPDATE_JOB_EXECUTION_CONTEXT_URL)
	public ResponseEntity<?> updateJobExecContext(@RequestBody JobExecution jobExecution) {
		jobRepository.updateExecutionContext(jobExecution);
		return ResponseEntity.ok("Job Execution Context updated successfully");
	}

	@GetMapping(GET_LAST_STEP_EXEC_URL)
	public ResponseEntity<?> getLastStepExec(@RequestBody JobInstWithStepNameDto jobInstWithStepNameDto) {
		return ResponseEntity.ok(jobRepository.getLastStepExecution(jobInstWithStepNameDto.getJobInstance(),
				jobInstWithStepNameDto.getStepName()));
	}

	@GetMapping(GET_STEP_EXEC_COUNT)
	public ResponseEntity<?> getStepExecCount(@RequestBody JobInstWithStepNameDto jobInstWithStepNameDto) {
		return ResponseEntity.ok(jobRepository.getStepExecutionCount(jobInstWithStepNameDto.getJobInstance(),
				jobInstWithStepNameDto.getStepName()));
	}

	@GetMapping(GET_LAST_JOB_EXEC_URL)
	public ResponseEntity<?> getLastJobExec(@RequestBody JobNameWithParamsDto jobNameWithParamsDto) {
		return ResponseEntity.ok(jobRepository.getLastJobExecution(jobNameWithParamsDto.getJobName(),
				jobNameWithParamsDto.getJobParameters()));
	}

	@PostMapping(CREATE_JOB_EXEC_URL)
	public ResponseEntity<?> createJobExec(@RequestBody JobInstWithParamsAndConfigLoc jobInstWithParamsAndConfigLoc) {
		return ResponseEntity.ok(jobRepository.createJobExecution(
				jobInstWithParamsAndConfigLoc.getJobInstance(),
				jobInstWithParamsAndConfigLoc.getJobParameters(),
				jobInstWithParamsAndConfigLoc.getJobConfigurationLocation())
		);
	}

	@PostMapping(CREATE_SIMPLE_JOB_EXECUTION_URL)
	public ResponseEntity<?> createSimpleJobExec(@RequestBody JobNameWithParamsDto jobNameWithParamsDto)
			throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobRestartException {
		return ResponseEntity.ok(jobRepository.createJobExecution(
				jobNameWithParamsDto.getJobName(), jobNameWithParamsDto.getJobParameters()
		));
	}
}
