package br.uff.ic.gems.peixeespadacliente.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is responsible for collecting generic statistics and
 * presenting these collected statistics in a comma separated value (CSV)
 * format.
 * 
 * @author murta
 */
public class StatisticsCollector {
	
	/**
	 * Names of the collected statistics
	 */
	private List<String> names = null;
	
	/**
	 * Stores the collected statistics
	 */
	private Map<String, Map<Integer, String>> statisticsByName = null;
	
	/**
	 * Current statistics tuple
	 */
	private int tuple = 0;
	
	/**
	 * Singleton instance
	 */
	private static StatisticsCollector instance = null;
	
	/**
	 * Singleton constructor
	 */
	private StatisticsCollector() {
		names = new ArrayList<String>();
		statisticsByName = new HashMap<String, Map<Integer, String>>();
	}
	
	/**
	 * Provides the singleton instance
	 */
	public synchronized static StatisticsCollector getInstance() {
		if (instance == null) {
			instance = new StatisticsCollector();
		}
		return instance;
	}
	
	/**
	 * Resets the StatisticsCollector singleton instance
	 */
	public synchronized static void resetInstance() {
		instance = null;
	}

	/**
	 * Adds new numeric statistics
	 */
	public synchronized void add(String name, long value) {
		this.add(name, String.valueOf(value));
	}
	
	/**
	 * Adds new numeric (floating point) statistics
	 */
	public synchronized void add(String name, double value) {
		this.add(name, String.valueOf(value));
	}
	
	/**
	 * Adds new statistics
	 */
	@SuppressWarnings("boxing")
	public synchronized void add(String name, String value) {
		Map<Integer, String> statistics = statisticsByName.get(name);
		if (statistics == null) {
			statistics = new HashMap<Integer, String>();
			names.add(name);
			statisticsByName.put(name, statistics);
		}
		statistics.put(tuple, value);
	}
	
	/**
	 * Saves a file with the current set of statistics as CSV.
	 */
	@SuppressWarnings("boxing")
	public synchronized void save() throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("statistics.txt"));
		
		// Saves the metadata
		int position = 0; 
		for (String name : names) {
			if (position > 0) {
				writer.write(",");
			}		
			writer.write(name);
			position++;
		}
		
		// Saves the statistics
		for (int i = 0; i <= tuple; i++) {
			writer.newLine();
			
			position = 0; 
			for (String name : names) {
				if (position > 0) {
					writer.write(",");
				}
				String value = statisticsByName.get(name).get(i); 
				if (value != null) {
					writer.write(value);	
				}
				position++;
			}
		}

		writer.close();
		tuple++;
	}
}
