package com.example.merge;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class MergePDFUtility {
    public static void main(String[] args) throws URISyntaxException, IOException {

        String inputFolderName ="input/files/pdf";
        String outputFileName ="/Merged.pdf";

        MergePDFUtility mergePDFUtility =new MergePDFUtility();

        List<String> fileList = mergePDFUtility.getAllFilesFromResource(inputFolderName);

        System.out.printf("Files found: %d \n",fileList.size());

        String basePath = getBasePath();

        mergePDFUtility.mergePDF(basePath+outputFileName,fileList);

        System.out.println("All files merged successfully");

    }

    private static String getBasePath() throws URISyntaxException {
        URL url = MergePDFUtility.class.getResource("/output/pdf/");
        String basePath= new File(url.toURI()).getAbsoluteFile().getPath();

        System.out.println("Output Path: "+basePath);
        return basePath;
    }


    private List<String> getAllFilesFromResource(String folder)
            throws URISyntaxException, IOException {

        ClassLoader classLoader = getClass().getClassLoader();

        URL resource = classLoader.getResource(folder);

        // dun walk the root path, we will walk all the classes
        return Files.walk(Paths.get(resource.toURI()))
                .filter(Files::isRegularFile)
                .map(Path::toString)
                .collect(Collectors.toList());
    }

    public void mergePDF(String outDir, List<String> pdfFilesList) throws IOException {


        // Instantiating PDFMergerUtility class

        PDFMergerUtility obj = new PDFMergerUtility();

        // Setting the destination file path

        obj.setDestinationFileName(outDir);

        // Add all source files, to be merged

        pdfFilesList.forEach(file->{
            try {
                obj.addSource(file);
            } catch (FileNotFoundException e) {
                System.out.println("Error Occurred"+e.getMessage());
            }
        });


        // Merging documents using temp file only

        obj.mergeDocuments(MemoryUsageSetting.setupTempFileOnly());

        System.out.println(
                "PDF Documents merged to a single file");

    }

}
