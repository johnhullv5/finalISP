package quant.service;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import quant.domain.*;

import rx.Observable;

public interface LoadService {
	
	 Observable<Pair<DateTime,HisData>> loadOneSymbol(String symbol) throws IOException;
	
	

}
