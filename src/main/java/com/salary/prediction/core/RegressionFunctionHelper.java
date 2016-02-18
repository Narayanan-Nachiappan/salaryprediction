/**
 * This is the core class that does evaluation[cross validation] and classification
 * This class accepts the Instances/data and depending on the regression algo chosen
 * it trains them, evaluates them and classifies the test data
 */
package com.salary.prediction.core;

import java.util.Enumeration;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.M5P;
import weka.classifiers.trees.REPTree;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class RegressionFunctionHelper {

    public static enum ALGO_TYPE {
	MULTI_LINEAR_REGRESSION, M5_PRUNE_TREES, MULTI_LAYER_PERCEPTRON, REP_TREE
    };

    /*
     * This function evaluates the model and outputs the internal weights
     * assigned for each explanatory variable. Also invokes cross validation
     * 
     */
    public void evaluateSalaryPrediction(Instances data, ALGO_TYPE type) {
	data.setClassIndex(data.numAttributes() - 1);
	// default to be annonymous inner type which will be overwritten in the
	// switch
	Classifier model = new Classifier() {
	    /**
	     * 
	     */
	    private static final long serialVersionUID = 1L;

	    @Override
	    public void buildClassifier(Instances data) throws Exception {
	    }
	};
	switch (type) {
	case M5_PRUNE_TREES:
	    model = new M5P();
	    break;
	case MULTI_LINEAR_REGRESSION:
	    model = new LinearRegression();
	    break;
	case MULTI_LAYER_PERCEPTRON:
	    model = new MultilayerPerceptron();
	    break;
	case REP_TREE:
	    model = new REPTree();
	    break;
	}
	try {
	    model.buildClassifier(data);
	    System.out.println(model);
	    crossValidateModel(model, data);

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

    /**
     * This function accepts the model and performs cross validation Outputs the
     * correlation coefficient
     */
    private void crossValidateModel(Classifier model, Instances data) {

	try {
	    Evaluation eval = new Evaluation(data);
	    eval.crossValidateModel(model, data, 10, new Random(1));
	    System.out.println(eval.toSummaryString());

	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /*
     * 
     * This function performs the actual classification of test data
     */
    public Instances predictSalary(Instances data, ALGO_TYPE type, Instances testData) {
	data.setClassIndex(data.numAttributes() - 1);
	Classifier model = new Classifier() {
	    @Override
	    public void buildClassifier(Instances data) throws Exception {

	    }
	};
	switch (type) {
	case M5_PRUNE_TREES:
	    model = new M5P();
	    break;
	case MULTI_LINEAR_REGRESSION:
	    model = new LinearRegression();
	    break;
	case MULTI_LAYER_PERCEPTRON:
	    model = new LinearRegression();
	    break;
	case REP_TREE:
	    model = new REPTree();
	    break;
	}
	try {
	    model.buildClassifier(data);
	    Enumeration<Instance> instanceEnumerator = testData.enumerateInstances();
	    testData.insertAttributeAt(new Attribute("salary"), testData.numAttributes());
	    while (instanceEnumerator.hasMoreElements()) {
		Instance inst = instanceEnumerator.nextElement();
		inst.setValue(inst.numAttributes() - 1, Math.ceil(model.classifyInstance(inst)));
	    }
	} catch (Exception e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return testData;
    }

}
