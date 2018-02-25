package com.ClassifierService.OpenNLPApproach;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.util.InputStreamFactory;

public class DocumentFileInputStreamFactory implements InputStreamFactory {

	private InputStream is;

	public DocumentFileInputStreamFactory(InputStream is) throws FileNotFoundException {
		this.is = is;
	}

	@Override
	public InputStream createInputStream() throws IOException {
		return is;
	}
}