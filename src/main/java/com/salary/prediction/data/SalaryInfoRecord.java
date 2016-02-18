/**
 * 
 */
package com.salary.prediction.data;

import org.apache.commons.csv.CSVRecord;

import com.salary.prediction.util.DataCleanupHelper;

/**
 * @author nnarayan Data Model describing the train / test data
 */
public class SalaryInfoRecord {
    // { COMPANY_ID, JOB_TYPE, DEGREE, MAJOR, INDUSTRY,
    // YEARS_EXPERIENCE, MILES, SALARY };
    private String companyId;
    private String jobType;
    private String degree;
    private String major;
    private String industry;
    private Double yearsExperience;
    private Double milesFromMetropolis;
    private Integer salary;

    /**
     * @param record
     * @return
     */
    public static SalaryInfoRecord fromCSVRecord(CSVRecord record, CSVRecord recordSal) {
	// TODO Auto-generated method stub
	SalaryInfoRecord rec = new SalaryInfoRecord();
	rec.setDegree(record.get(DataCleanupHelper.DEGREE));
	rec.setMajor(record.get(DataCleanupHelper.MAJOR));
	rec.setCompanyId(record.get(DataCleanupHelper.COMPANY_ID));
	rec.setIndustry(record.get(DataCleanupHelper.INDUSTRY));
	rec.setJobType(record.get(DataCleanupHelper.JOB_TYPE));
	rec.setMilesFromMetropolis(new Double(record.get(DataCleanupHelper.MILES)));
	rec.setSalary(new Integer(recordSal.get(DataCleanupHelper.SALARY)));
	rec.setYearsExperience(new Double(record.get(DataCleanupHelper.YEARS_EXPERIENCE)));
	return rec;
    }

    public String getCompanyId() {
	return companyId;
    }

    public void setCompanyId(String id) {
	this.companyId = id;
    }

    public String getJobType() {
	return jobType;
    }

    public void setJobType(String jobType) {
	this.jobType = jobType;
    }

    public String getDegree() {
	return degree;
    }

    public void setDegree(String degree) {
	this.degree = degree;
    }

    public String getMajor() {
	return major;
    }

    public void setMajor(String major) {
	this.major = major;
    }

    public String getIndustry() {
	return industry;
    }

    public void setIndustry(String industry) {
	this.industry = industry;
    }

    public Double getYearsExperience() {
	return yearsExperience;
    }

    public void setYearsExperience(Double yearsExperience) {
	this.yearsExperience = yearsExperience;
    }

    public Double getMilesFromMetropolis() {
	return milesFromMetropolis;
    }

    public void setMilesFromMetropolis(Double milesFromMetropolis) {
	this.milesFromMetropolis = milesFromMetropolis;
    }

    public Integer getSalary() {
	return salary;
    }

    public void setSalary(Integer salary) {
	this.salary = salary;
    }

}
