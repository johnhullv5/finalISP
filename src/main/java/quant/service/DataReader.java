package quant.service;

import java.text.ParseException;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import quant.domain.HisData;
import rx.Observable;

public interface DataReader{
	
	public Observable<Pair<DateTime, HisData>> readOneSymbol(String restUrl,String symbol) throws ParseException;

	
}