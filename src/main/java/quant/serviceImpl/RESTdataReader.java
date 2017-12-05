package quant.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.web.client.RestTemplate;

import quant.domain.Foo;
import quant.domain.Foos;
import quant.domain.HisData;
import quant.service.DataReader;
import rx.Observable;

public class RESTdataReader implements DataReader{
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public  Observable<Pair<DateTime, HisData>> readOneSymbol(String restUrl, String symbol) throws ParseException {
		// TODO Auto-generated method stub
		String completeUrl = restUrl+"/"+symbol;
		Foos o = new RestTemplate().getForObject("http://localhost:9000", Foos.class);
		
		Iterator<String> symbols = o.getProperties().keySet().iterator();
		SortedMap<DateTime, HisData> result = new TreeMap<DateTime,HisData>();
		
		while(symbols.hasNext())
		{
			String s = symbols.next();
			List<Foo> f = o.getProperties().get(s);
			for(Foo d : f)
			{
				HisData data = new HisData();
				DateTime t = new DateTime(DATE_FORMAT.parse(d.getDate()));
				data.setDate(t);
				data.setAdjClosePrice(d.getAdj());
				data.setClosePrice(d.getClose());
				data.setHighPrice(d.getHigh());
				data.setLowPrice(d.getLow());
				data.setOpenPrice(d.getOpen());
				data.setSymbol(d.getSymbol());
				data.setVolume(d.getVol());
				if (!result.keySet().contains(t)) {
					result.put(t, data);
				}
				
			}
		}
		
		
		
		List<Pair<DateTime, HisData>> list = new ArrayList<Pair<DateTime, HisData>>();

		for (Iterator<DateTime> i = result.keySet().iterator(); i.hasNext();) {
			DateTime item = i.next();
			
			Pair<DateTime, HisData> p = new ImmutablePair(item, result.get(item));
			list.add(p);
		}
		return Observable.from(list);
	}
	
	
	
} 