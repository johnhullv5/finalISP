package quant.domain;

import org.joda.time.DateTime;

public class HisData {
	
	private String symbol;
	
	private DateTime date;
	
	private String openPrice;
	
	private String highPrice;
	
	private String lowPrice;
	
	private String closePrice;
	
	private String adjClosePrice;
	
	private String volume;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public DateTime getDate() {
		return date;
	}

	public void setDate(DateTime date) {
		this.date = date;
	}

	public String getOpenPrice() {
		return openPrice;
	}

	public void setOpenPrice(String openPrice) {
		this.openPrice = openPrice;
	}

	public String getHighPrice() {
		return highPrice;
	}

	public void setHighPrice(String highPrice) {
		this.highPrice = highPrice;
	}

	public String getLowPrice() {
		return lowPrice;
	}

	public void setLowPrice(String lowPrice) {
		this.lowPrice = lowPrice;
	}

	public String getClosePrice() {
		return closePrice;
	}

	public void setClosePrice(String closePrice) {
		this.closePrice = closePrice;
	}

	public String getAdjClosePrice() {
		return adjClosePrice;
	}

	public void setAdjClosePrice(String adjClosePrice) {
		this.adjClosePrice = adjClosePrice;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
		

}