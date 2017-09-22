package com.test.unit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.summarization.export.CalculateMinimalTypes;

import org.junit.Test;

public class CalculateMinimalTypesTest extends TestWithTemporaryData{

	@Test
	public void shouldParseManyFiles() throws Exception {
		
		temporary.file(new ToyOntology()
								.owl()
								.definingConcept("http://concept")
									.aSubconceptOf("http://thing")
								.serialize(), "owl");
		temporary.namedFile("http://instance1##type##http://concept", "0_types.nt");
		temporary.namedFile("http://instance2##type##http://concept", "1_types.nt");
		
		File folder = new File(temporary.path());
		Collection<File> listOfFiles = FileUtils.listFiles(folder, new String[]{"owl"}, false);
		String fileName = listOfFiles.iterator().next().getName();
	
		
		CalculateMinimalTypes.main(new String[]{
				temporary.path() + "/" + fileName,
				temporary.path(),
				temporary.path()
		});
		
		assertThat(temporary.files("_minType.txt").length, equalTo(2));
	}
}
