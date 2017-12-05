package quant.test;

import static org.junit.Assert.*;

import java.text.ParseException;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import quant.config.MyConfiguration;
import quant.domain.Foo;
import quant.domain.Foos;
import quant.domain.HisData;
import quant.service.CrossRule;
import quant.serviceImpl.CrossDownRule;
import quant.serviceImpl.CrossUpRule;
import quant.serviceImpl.MathOperatorService2;
import quant.serviceImpl.RESTdataReader;
import quant.serviceImpl.RuleImplService;
import rx.Observable;
import rx.functions.Action1;

@RunWith(SpringRunner.class)
@SpringBootTest
@Import(MyConfiguration.class)
public class MyTest {
	final private int n = 0;

    @Test
    public void exampleTest() throws ParseException {
//    	Foos o = new RestTemplate().getForObject("http://localhost:9000", Foos.class);
//    	for(Foo f :o.getProperties().get("2017-12-04") )
//    	{System.out.println(f.getSymbol());}
    	RESTdataReader reader = new RESTdataReader();
    	Observable<Pair<DateTime, HisData>> data = reader.readOneSymbol(null, null);
    	RuleImplService util  = new RuleImplService();
    	int n = 0;
    	
    	
    	
    	MathOperatorService2 mathService = new MathOperatorService2();
    	
    	Observable<Pair<DateTime, Double>> close = mathService.CLOSE(data).takeLast(100);
    	Observable<Pair<DateTime, Double>> sma = mathService.SMA(close, 3).takeLast(100);
    	
    	CrossUpRule smaRule = new CrossUpRule();
    	
    	smaRule.setN(3);

		smaRule.setClose(close);

		smaRule.setBase(sma);

		Observable<Pair<DateTime, Double>> smaSignal = smaRule.runRule2(util );
		
		close.subscribe(new Action1<Pair<DateTime, Double>>() {

            @Override
            public void call(Pair<DateTime, Double> s) {
                System.out.println("close " + s.getKey().toString() + ":        "+s.getValue().toString());
                //n+=1;
            }

        });
    	
    	
    	
    	
		sma.subscribe(new Action1<Pair<DateTime, Double>>() {

            @Override
            public void call(Pair<DateTime, Double> s) {
                System.out.println("sma " + s.getKey().toString() + ":        "+s.getValue().toString());
                //n+=1;
            }

        });
		
		
		smaSignal.subscribe(new Action1<Pair<DateTime, Double>>() {

            @Override
            public void call(Pair<DateTime, Double> s) {
                System.out.println("smaSignal" + s.getKey().toString() + ":        "+s.getValue().toString());
                //n+=1;
            }

        });
    	
    	assertEquals(n,0);
        
    }

}