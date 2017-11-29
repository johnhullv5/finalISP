package quant.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import quant.domain.*;
//import com.COMP313.ISP.domain.HistoricalData2;
import com.opencsv.CSVReader;

public class CSVReaderRunner {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

//	public static SortedMap<DateTime, FunData> readCsvFunForOneSymbol(String path, String symbol) throws IOException {
//		DateTime t = DateTime.now();
//		SortedMap<DateTime, FunData> result = new TreeMap<DateTime, FunData>();
//		String csvFile = path + symbol + ".csv";
//
//		CSVReader reader = null;
//		try {
//			reader = new CSVReader(new FileReader(csvFile));
//			String[] line;
//			while ((line = reader.readNext()) != null) {
//				FunData data = new FunData();
//				data.setDate(t);
//				System.out.println(line[0]);
//				data.setTrillingPE(Double.parseDouble(line[0]));
//				data.setPriceToSales(Double.parseDouble(line[1]));
//				data.setYield(Double.parseDouble(line[2]));
//				data.setPB(Double.parseDouble(line[3]));
//				data.setEV(Double.parseDouble(line[4]));
//				data.setEarningsQuarterlyGrowth(Double.parseDouble(line[5]));
//				data.setNetIncomeToCommon(Double.parseDouble(line[6]));
//				data.setRevenueGrowth(Double.parseDouble(line[7]));
//				data.setOperatingCashflow(Double.parseDouble(line[8]));
//				data.setReturnOnEquity(Double.parseDouble(line[9]));
//
//				if (!result.keySet().contains(t)) {
//					result.put(t, data);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			Logger.getLogger(CSVReaderRunner.class.getName()).log(Level.ERROR, "Error while open file", e);
//		}
//
//		return result;
//
//	}

	public static List<String> loadSymbols(String path) {

		String csvFile = path + "symbols.csv";
		
		List<String> symbols = new ArrayList<String>();

		CSVReader reader = null;
		try {
			reader = new CSVReader(new FileReader(csvFile));
			String[] line;
			while ((line = reader.readNext()) != null) {
				
				symbols.add(line[0]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			Logger.getLogger(CSVReaderRunner.class.getName()).log(Level.ERROR, "Error while open file", e);
		}

		return symbols;

	}

//	public static SortedMap<DateTime, HistoricalData2> readCsvForOneSymbol(String path, String symbol)
//			throws IOException {
//
//		SortedMap<DateTime, HistoricalData2> result = new TreeMap<DateTime, HistoricalData2>();
//
//		
//
//		String csvFile = path + symbol + ".csv";
//
//
//
//		System.out.println("csv file: " + csvFile);
//
//		CSVReader reader = null;
//		try {
//			reader = new CSVReader(new FileReader(csvFile));
//			String[] line;
//			while ((line = reader.readNext()) != null) {
//				HistoricalData2 data = new HistoricalData2();
//				DateTime t = new DateTime(DATE_FORMAT.parse(line[0]));
//				data.setDate(t);
//				data.setAdjClosePrice(line[6]);
//				data.setClosePrice(line[4]);
//				data.setHighPrice(line[2]);
//				data.setLowPrice(line[3]);
//				data.setOpenPrice(line[1]);
//				data.setSymbol(symbol);
//				data.setVolume(line[5]);
//				if (!result.keySet().contains(t)) {
//					result.put(t, data);
//				}
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			Logger.getLogger(CSVReaderRunner.class.getName()).log(Level.ERROR, "Error while open file", e);
//		} catch (ParseException e) {
//			Logger.getLogger(CSVReaderRunner.class.getName()).log(Level.ERROR, "Error while parsing date", e);
//		}
//
//		return result;
//
//	}

}
