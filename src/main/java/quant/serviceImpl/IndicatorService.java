package quant.serviceImpl;

import eu.verdelhan.ta4j.TimeSeries;
import eu.verdelhan.ta4j.indicators.simple.ClosePriceIndicator;
import eu.verdelhan.ta4j.indicators.trackers.SMAIndicator;

public class IndicatorService {
	
	public static ClosePriceIndicator makeClosePriceIndicator(TimeSeries ts)
	{
		return new ClosePriceIndicator(ts);
		
	}
	
	public static SMAIndicator makeSMAIndicator(ClosePriceIndicator ci,int para)
	{
		return new SMAIndicator(ci, para);
	}

}
