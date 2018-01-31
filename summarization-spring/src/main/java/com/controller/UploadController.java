package com.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.model.Dataset;
import com.model.Ontology;
import com.service.DatasetService;
import com.service.OntologyService;

@Controller
@RequestMapping(value = "upload")
public class UploadController {
	
	@Autowired
	DatasetService datasetService;
	@Autowired
	OntologyService ontologyService;
	
	private static String UPLOADED_FOLDER = "../data/DsAndOnt/";
	
	@RequestMapping(value = "/ds", method = RequestMethod.POST)
	public String datasetUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
		try {

            // Get the file and save it somewhere
            InputStream stream = file.getInputStream();
            
            Dataset dataset = new Dataset();
            dataset.setType("dataset");
            dataset.setName(file.getOriginalFilename().replaceFirst("[.][^.]+$", ""));
            dataset.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
            String percorso = UPLOADED_FOLDER + "dataset" + "/" + dataset.getName();
            dataset.setPath(percorso  +"/"+ file.getOriginalFilename());
            //path del file
            Path path = Paths.get(percorso +"/"+ file.getOriginalFilename());
            File file1 = new File(percorso);
	        if (!file1.exists()) {
	            if (file1.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
            if(dataset.getId() != null && !dataset.getId().trim().equals("")) {
    			//update
    			datasetService.update(dataset);
            }else {
            	FileUtils.copyInputStreamToFile(stream, path.toFile());
            	datasetService.add(dataset);
            	redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:uploadStatus";
    }
	
	
	@RequestMapping(value = "/ont", method = RequestMethod.POST)
	public String ontologyUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
		
		if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
		try {

            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Ontology ontology = new Ontology();
            ontology.setType("ontology");
            ontology.setName(file.getOriginalFilename().replaceFirst("[.][^.]+$", ""));
            ontology.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
            String percorso = UPLOADED_FOLDER + "ontology";
            ontology.setPath(percorso  +"/"+ file.getOriginalFilename());
            //path del file
            Path path = Paths.get(percorso +"/"+ file.getOriginalFilename());
            File file1 = new File(percorso);
	        if (!file1.exists()) {
	            if (file1.mkdirs()) {
	                System.out.println("Directory is created!");
	            } else {
	                System.out.println("Failed to create directory!");
	            }
	        }
            if(ontology.getId() != null && !ontology.getId().trim().equals("")) {
    			//update
    			ontologyService.update(ontology);
            }else {
            	Files.write(path, bytes);
            	ontologyService.add(ontology);
            	redirectAttributes.addFlashAttribute("message", "You successfully uploaded '" + file.getOriginalFilename() + "'");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "redirect:uploadStatus";
    }
	
	
	@RequestMapping(value = "/uploadStatus", method = RequestMethod.GET)
    public String uploadStatus() {
        return "uploadStatus";
    }
	
}
