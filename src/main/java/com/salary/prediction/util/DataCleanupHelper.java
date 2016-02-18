/**
 * 
 */
package com.salary.prediction.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import com.salary.prediction.data.SalaryInfoRecord;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * @author nnarayan This class merges the training data with salary and cleans
 *         up attributes. Mostly rounding & dimensionality reduction
 */
public class DataCleanupHelper {

    public static final String COMPANY_ID = "companyId";

    public static final String JOB_TYPE = "jobType";

    public static final String DEGREE = "degree";

    public static final String MAJOR = "major";

    public static final String INDUSTRY = "industry";

    public static final String YEARS_EXPERIENCE = "yearsExperience";

    public static final String MILES = "milesFromMetropolis";

    public static final String SALARY = "salary";

    public static final String JOB_ID = "jobId";

    public static final String[] FILE_HEADER_MAPPING = { JOB_ID, COMPANY_ID, JOB_TYPE, DEGREE, MAJOR, INDUSTRY,
	    YEARS_EXPERIENCE, MILES };
    public static final String[] FILE_HEADER_MAPPING_TRANSFORM = { JOB_TYPE, DEGREE, MAJOR, INDUSTRY, YEARS_EXPERIENCE,
	    MILES, SALARY };

    public static final String[] FILE_HEADER_MAPPING_SAL = { JOB_ID, SALARY };
    public static final String TRAIN_FILE = "train_features.csv";
    public static final String TEST_FILE = "test_features.csv";

    public static String dataDirectory, trainFile, testFile, salaryFile;

    public static void cleanUp(String dir, String tFile, String salFile, String tstFile) throws Exception {
	dataDirectory = dir;
	trainFile = tFile;
	testFile = tstFile;
	salaryFile = salFile;

	cleanData();
	// convert to arff
	CSV2Arff.generateARFF(dataDirectory + TRAIN_FILE, dataDirectory + "train_features.arff", false);
	// Includes removing jobid, miles and company
	CSV2Arff.generateARFF(dataDirectory + testFile, dataDirectory + "test_features.arff", true);

    }

    public static void cleanData() {

	CSVFormat csvFileFormat = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING);
	CSVFormat csvFileFormatTransformed = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING_TRANSFORM);
	CSVFormat csvFileFormatSalary = CSVFormat.DEFAULT.withHeader(FILE_HEADER_MAPPING_SAL);

	standardizeTrainData(csvFileFormat);
	try (FileWriter fileWriter = new FileWriter(new File(dataDirectory + TRAIN_FILE));
		CSVPrinter csvPrinter = new CSVPrinter(fileWriter, csvFileFormatTransformed);
		FileReader fileReader = new FileReader(new File((dataDirectory + trainFile)));
		FileReader fileReaderSalary = new FileReader(new File((dataDirectory + salaryFile)));
		CSVParser csvFileParser = new CSVParser(fileReader, csvFileFormat);
		CSVParser csvFileParserSalary = new CSVParser(fileReaderSalary, csvFileFormatSalary);) {

	    List<CSVRecord> csvRecords = csvFileParser.getRecords();
	    List<CSVRecord> csvRecordsSalary = csvFileParserSalary.getRecords();
	    for (int i = 1; i < csvRecords.size(); i++) {
		CSVRecord record = csvRecords.get(i);
		CSVRecord recordSal = csvRecordsSalary.get(i);
		if (record.get(JOB_ID).equals(recordSal.get(JOB_ID))) {
		    SalaryInfoRecord rec = SalaryInfoRecord.fromCSVRecord(record, recordSal);
		    normalizeSalary(rec);
		    List<String> salarayRecord = new ArrayList<String>();
		    salarayRecord.add(rec.getJobType());
		    salarayRecord.add(rec.getDegree());
		    salarayRecord.add(rec.getMajor());
		    salarayRecord.add(rec.getIndustry());
		    Double yearsOfExp = rec.getYearsExperience();
		    salarayRecord.add(new Double(yearsOfExp).toString());
		    Double miles = rec.getMilesFromMetropolis();
		    salarayRecord.add(miles.toString());
		    salarayRecord.add(rec.getSalary().toString());
		    csvPrinter.printRecord(salarayRecord);
		}
	    }
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * @param csvFileFormat
     */
    private static void standardizeTrainData(CSVFormat csvFileFormat) {
	// TODO Auto-generated method stub
	CSVLoader loader = new CSVLoader();
	try {
	    loader.setSource(new File(dataDirectory + trainFile));
	    Instances data = loader.getDataSet();
	    Standardize s_train = new Standardize();
	    s_train.setInputFormat(data);
	    data = Filter.useFilter(data, s_train);
	    CSVSaver saver = new CSVSaver();
	    saver.setInstances(data);
	    saver.setFile(new File(dataDirectory + trainFile));
	    saver.writeBatch();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * @param record
     */
    public static void normalizeSalary(SalaryInfoRecord record) {
	Integer salary = record.getSalary();
	record.setSalary((int) (Math.round(salary / 10.0) * 10));
    }
}
