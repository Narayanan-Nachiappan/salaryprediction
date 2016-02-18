/**
 * 
 */
package com.salary.prediction.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

/**
 * @author nnarayan
 *
 */
public class FileUtil {

    public static void convertDataToRequiredFormat(Instances testData, String dataDirectory, String testFilePath) {
	// TODO Auto-generated method stub
	try (FileReader testInputReader = new FileReader(dataDirectory + testFilePath);
		
		BufferedReader br = new BufferedReader(testInputReader)) {
	    br.readLine();
	    FastVector values = null;
	    testData.insertAttributeAt(new Attribute("jobId", values), 0);
	    Enumeration<Instance> instanceEnumerator = testData.enumerateInstances();
	    while (instanceEnumerator.hasMoreElements()) {
		Instance inst = instanceEnumerator.nextElement();
		inst.setValue(0, new String(br.readLine().split(",")[0]));
	    }
	    int stopIndex = testData.numAttributes() - 3;
	    for (int i = 0; i <= stopIndex; i++) {
		testData.deleteAttributeAt(1);
	    }

	} catch (FileNotFoundException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e1) {
	    // TODO Auto-generated catch block
	    e1.printStackTrace();
	}

    }

}
