package it.ac.csv.csvparser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.ac.csv.csvparser.model.DbfRow;

/**
 * 
 * @author Alessio Cocciolo
 *
 */
public class Parser {
	
	private static final String SEPARATOR = ";";
	private static Properties prop;
	
	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new Exception("Deve essere specificato il path del file da elaborare. Il path sarà cosi chiamato: data.csv");

		String configPath = String.format("%s\\config\\config.properties", args[0]);
		prop = new Properties();
		prop.load(new FileInputStream(configPath));
		
		List<DbfRow> elaboratedData = processInputFile(String.format("%s\\data.csv", args[0]));
		
		String recordAsCsv = elaboratedData.stream()
		        .map(DbfRow::toCsvRow)
		        .collect(Collectors.joining("\n"));
		
		Path path = Paths.get(String.format("%s\\output\\output.csv", args[0]));
		try (BufferedWriter writer = Files.newBufferedWriter(path))
		{
		    writer.write(recordAsCsv);
		}
	}

	private static List<DbfRow> processInputFile(String inputPath) {
		List<DbfRow> inputList = new ArrayList<DbfRow>();

		try {

			File inputF = new File(inputPath);
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));

			// skip the header of the csv
			inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());

			br.close();

		} catch (IOException e) {
			// TODO
		}

		return inputList;
	}
	
	private static Function<String, DbfRow> mapToItem = (line) -> {

		  String[] p = line.split(SEPARATOR);

		  DbfRow item = new DbfRow();

		  try {
			  item.setDbfValue(p[0]);
			  item.setSrcidFeat(p[1]);		  
			  item.setSrcidRast(prop.getProperty(p[2]));		  
		  } catch(Throwable e) {
			  System.err.println("Error during the data elaboration. Please check the input data. Error detail: " + e.getMessage());
		  }
		  
		  return item;

		};
}
