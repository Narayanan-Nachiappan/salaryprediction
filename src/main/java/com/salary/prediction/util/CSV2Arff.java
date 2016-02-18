/**
 * 
 */
package com.salary.prediction.util;

import java.io.File;

/**
 * @author nnarayan
 *
 *Converts the CSV files to ARFF that weka uses for creating instances
 */
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class CSV2Arff {
    /**
     * takes 2 arguments: - CSV input file - ARFF output file
     */

    public static void generateARFF(String sourceFile, String DestFile, boolean test) throws Exception {

	// load CSV
	CSVLoader loader = new CSVLoader();
	loader.setSource(new File(sourceFile));
	Instances data = loader.getDataSet();
	if (test) {
	    data.deleteAttributeAt(0);// remove JobId from test file
	    data.deleteAttributeAt(0);// remove companyId
	    // data.deleteAttributeAt(data.numAttributes() - 1);
	}

	// save ARFF
	ArffSaver saver = new ArffSaver();
	saver.setInstances(data);
	saver.setFile(new File(DestFile));
	saver.setDestination(new File(DestFile));
	saver.writeBatch();
    }
}
