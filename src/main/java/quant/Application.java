package quant;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import quant.config.InputParameterSet;
import quant.serviceImpl.LargerThanRule;
import quant.serviceImpl.MathOperatorService2;
import quant.serviceImpl.RESTdataReader;
import quant.serviceImpl.RuleImplService;
import rx.Observable;

import quant.config.InputParameterSet;
import quant.domain.HisData;
import quant.serviceImpl.CrossDownRule;
import quant.serviceImpl.CrossUpRule;

import quant.serviceImpl.RuleImplService;
import com.google.gson.Gson;

import quant.service.CrossRule;
import quant.config.MyConfiguration;

@SpringBootApplication
@Import(MyConfiguration.class)
@RestController
public class Application {
	
	
	@Autowired
	private CrossRule getCrossUPRule;
	
	@Autowired
	private CrossRule getCrossDownRule;

	@Autowired
	private RuleImplService getRuleImplService;


	@Autowired
	private MathOperatorService2 getMathOperatorService;

	@Autowired
	private InputParameterSet getInputParameterSet;

	@Autowired
	private Map<String, Map<String, Map<String, String>>> initData;

	@Autowired
	private DateTimeFormatter getDateTimeFormatter;

	@Autowired
	private LargerThanRule getLargerThanRule;

	@Autowired
	private List<String> getSymbols;

	@Autowired
	private DateFormat getDateFormat;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
    

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
    
    @RequestMapping("/GOOG")
	public String signal() {
    	
    		Gson gson = new Gson();
    		Map<String, String> data = initData.get("GOOG").get("SMA");
    		String json = gson.toJson(data);
    		return json;
    	
	}
    
    @Bean
	InitializingBean compute() {
		return () -> {
			DateFormat df = getDateFormat;
			//List<String> symbols = getSymbols;
			List<String> symbols = new ArrayList<String>();
			symbols.add("GOOG");
			InputParameterSet params = getInputParameterSet;
			DateTimeFormatter fmt = getDateTimeFormatter;
			RuleImplService util = getRuleImplService;
			RESTdataReader reader = new RESTdataReader();
	    	Observable<Pair<DateTime, HisData>> data = reader.readOneSymbol(null, null);
			MathOperatorService2 mathService = getMathOperatorService;

//			long startTime_ = System.nanoTime();
//
			for (String symbol : symbols) {
				long startTime = System.nanoTime();
				try {

					CrossRule bollRule = new CrossDownRule();
//
					Observable<Pair<DateTime, Double>> close = mathService.CLOSE(data).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> high = mathService.HIGH(data).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> low = mathService.LOW(data).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> vol = mathService.VOLUME(data).takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> hoh = mathService.HOH(high, params.getWILL_N())
							.takeLast(params.getTake_N());

					Observable<Pair<DateTime, Double>> lol = mathService.LOL(low, params.getWILL_N())
							.takeLast(params.getTake_N());

					
					
					Observable<Pair<DateTime, Double>> sma = mathService.SMA(close, params.getSMA_N()).takeLast(params.getTake_N());
					
					



					CrossUpRule smaRule = new CrossUpRule();

					smaRule.setClose(close);

					smaRule.setBase(sma);
					
					smaRule.setN(params.getSMA_N());

					Observable<Pair<DateTime, Double>> smaSignal = smaRule.runRule2(util );



					//List<Pair<DateTime, Double>> smaList = william.toList().toBlocking().single();

					List<Pair<DateTime, Double>> smaSignalList = smaSignal.toList().toBlocking().single();




					Map<String, String> SMACrossUpMap = new TreeMap<String, String>();


					


					for (int i = 0; i < smaSignalList.size(); i++) {
						Pair<DateTime, Double> pair = smaSignalList.get(i);
						SMACrossUpMap.put(pair.getKey().toString(fmt), String.format("%.2f", pair.getValue()));
					}


					Map<String, Map<String, String>> ruleMap = new HashMap<String, Map<String, String>>();


					ruleMap.put("SMA", SMACrossUpMap);

					initData.put(symbol, ruleMap);

				} catch (Exception e) {
					e.printStackTrace();
				}
				;

				long endtime = System.nanoTime();
				System.out.println(symbol + " run with " + (endtime - startTime));

			}
		};
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {

            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames) {
                System.out.println(beanName);
            }

        };
    }

}