package com.itechart.test.altir.receiver.controller.storage;

public class UrlStorage {
	public static final String POST_PRODUCT_URL = "/products";
	public static final String GET_PRODUCTS_URL = "/products";
	public static final String GET_PRODUCT_BY_ID = "/products/{id}";
	public static final String GET_PRODUCT_BY_NAME = "/products/name/{name}";
	public static final String PUT_PRODUCT_BY_ID = "/products/{id}";
	public static final String DELETE_PRODUCT_BY_ID = "/products/{id}";

	public static final String IS_JOB_INSTANCE_EXISTS_URL = "/job/instance";
	public static final String CREATE_JOB_EXECUTION_URL = "/job";
	public static final String CREATE_SIMPLE_JOB_EXECUTION_URL = "/job/simple";
	public static final String JOB_EXEC_UPDATE_URL = "/job";
	public static final String JOB_ADD_STEP_URL = "/job/step";
	public static final String JOB_ADD_ALL_STEPS_URL = "/job/steps";
	public static final String STEP_EXEC_UPDATE_URL = "/job/step";
	public static final String UPDATE_EXECUTION_CONTEXT_URL = "/job/exec-context";
	public static final String UPDATE_JOB_EXECUTION_CONTEXT_URL = "/job/job-exec-context";
	public static final String GET_LAST_STEP_EXEC_URL = "/job/last-step";
	public static final String GET_STEP_EXEC_COUNT = "/job/step-exec-count";
	public static final String GET_LAST_JOB_EXEC_URL = "/job/last-job-exec";
	public static final String CREATE_JOB_INSTANCE_URL = "/job/job-instance";
	public static final String CREATE_JOB_EXEC_URL = "/job/job-exec";
}
