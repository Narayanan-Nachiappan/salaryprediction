/**
 * 
 */
package com.salary.prediction.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.salary.prediction.util.DataCleanupHelper;
import com.salary.prediction.util.FileUtil;

import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Standardize;

/**
 * @author nnarayan This is the main class which accepts run time params [params
 *         are described in README] invlokes regressionHelper for evaluation /
 *         classification and writes the test classes to CSV
 */
public class SalaryPredictor {

    public static void main(String a[]) throws Exception {
	try {
	    if (a.length < 6) {

		System.err.println(
			"Provide 6 args .<evaluate/classify> <algorithm to use> <path to  directory of train/test files with trailing slash> <train jobs file name> <train salary files name> <test file name>");
		System.out.println("Allowed values");
		System.out.println("evaluate - 0 , clasify - 1");
		System.out.println(
			"Algorithm allowed values MULTI_LINEAR_REGRESSION, M5_PRUNE_TREES, MULTI_LAYER_PERCEPTRON, REP_TREE");
		System.out.println("Example to run Multi linear regression in evaluation mode");
		System.err.println(
			"java -jar <path to shaded jar> com.salary.prediction.core.SalaryPredictor 0 MULTI_LINEAR_REGRESSION /user/data trainfilejob.csv trainfilesalary.csv testfile.csv");

		System.exit(-1);
	    }
	    String modeOfRun = a[0], algoToUse = a[1], dataDirectory = a[2], trainDataPath = a[3],
		    trainSalaryDataPath = a[4], testDataPath = a[5];
	    DataCleanupHelper.cleanUp(dataDirectory, trainDataPath, trainSalaryDataPath, testDataPath);
	    Instances data = new Instances(
		    new BufferedReader(new FileReader(new File(dataDirectory + "train_features.arff"))));

	    Instances testData = new Instances(
		    new BufferedReader(new FileReader(new File(dataDirectory + "test_features.arff"))));

	    // Standardize numeric attributes

	    Standardize s_test = new Standardize();
	    s_test.setInputFormat(testData);
	    testData = Filter.useFilter(testData, s_test);

	    RegressionFunctionHelper regrHelper = new RegressionFunctionHelper();
	    // MLR

	    if ("1".equals(a[0])) {
		// classify
		testData = regrHelper.predictSalary(data, RegressionFunctionHelper.ALGO_TYPE.valueOf(algoToUse),
			testData);
		FileUtil.convertDataToRequiredFormat(testData, dataDirectory, testDataPath);
		CSVSaver saver = new CSVSaver();
		saver.setInstances(testData);
		saver.setFile(new File(dataDirectory + "testoutput.csv"));
		saver.setDestination(new File(dataDirectory + "testoutput.csv"));
		saver.writeBatch();
	    } else {
		// evaluate
		regrHelper.evaluateSalaryPrediction(data, RegressionFunctionHelper.ALGO_TYPE.valueOf(algoToUse));
	    }

	} catch (IOException e) {
	    e.printStackTrace();
	}

    }
}
