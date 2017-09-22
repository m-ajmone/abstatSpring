package com.test.unit;

import com.summarization.dataset.InputFile;
import com.summarization.dataset.Processing;

import java.util.ArrayList;
import java.util.List;

public class ProcessingInspector implements Processing{

	List<InputFile> processedFile = new ArrayList<InputFile>();
	
	@Override
	public void process(InputFile file) throws Exception {
		processedFile.add(file);
	}

	@Override
	public void endProcessing() throws Exception {}
	
	public int countProcessed(){
		return processedFile.size();
	}
}