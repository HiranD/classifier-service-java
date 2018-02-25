package com.ClassifierService.OpenNLPApproach;

import java.io.InputStream;
import java.util.Collection;

public class ClassifierSourceData {

	private InputStream trainingData;
	private Collection<String> testData;

	public ClassifierSourceData(InputStream trainingData, Collection<String> testData) {
		this.trainingData = trainingData;
		this.testData = testData;
	}

	public InputStream getTrainingData() {
		return trainingData;
	}

	public Collection<String> getTestData() {
		return testData;
	}

	public void setTrainingData(InputStream trainingData) {
		this.trainingData = trainingData;
	}

	public void setTestData(Collection<String> testData) {
		this.testData = testData;
	}

}