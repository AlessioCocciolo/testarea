package it.ac.csv.csvparser.model;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DbfRow {
	
	String dbfValue;
	String srcidFeat;
	String srcidRast;
	
	
	public String getDbfValue() {
		return dbfValue;
	}
	public void setDbfValue(String value) {
		this.dbfValue = value;
	}
	public String getSrcidFeat() {
		return srcidFeat;
	}
	public void setSrcidFeat(String srcidFeat) {
		this.srcidFeat = srcidFeat;
	}
	public String getSrcidRast() {
		return srcidRast;
	}
	public void setSrcidRast(String srcidRast) {
		this.srcidRast = srcidRast;
	}
	
	
	public String toCsvRow() {
	    return Stream.of(dbfValue, srcidFeat, srcidRast)
	            .map(value -> value.replaceAll("\"", "\"\""))
	            .map(value -> Stream.of("\"", ";").anyMatch(value::contains) ? "\"" + value + "\"" : value)
	            .collect(Collectors.joining(";"));
	}
}
