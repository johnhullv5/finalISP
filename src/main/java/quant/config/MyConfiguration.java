package quant.config;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import quant.domain.*;
//import com.COMP313.ISP.service.CrossRule;
//import com.COMP313.ISP.service.Rule;
//import com.COMP313.ISP.serviceImpl.CrossDownRule;
//import com.COMP313.ISP.serviceImpl.CrossUpRule;
//import com.COMP313.ISP.serviceImpl.LargerThanRule;
//import com.COMP313.ISP.serviceImpl.MathOperatorService2;
////import com.COMP313.ISP.serviceImpl.LoadCSVService;
////import com.COMP313.ISP.serviceImpl.MathOperatorService;
//import com.COMP313.ISP.serviceImpl.RuleImplService2;
import quant.service.CrossRule;
import quant.serviceImpl.CrossDownRule;
import quant.serviceImpl.CrossUpRule;
import quant.serviceImpl.LargerThanRule;
import quant.serviceImpl.MathOperatorService2;
import quant.serviceImpl.RuleImplService;
import rx.Observable;

@Configuration
public class MyConfiguration {
	@Value("${rule.sma.n}")
	private int smaN1;

	@Bean
	public int getSMAN1() {
		return smaN1;
	}

	@Bean
	public CrossRule getCrossUPRule() {
		return new CrossUpRule();

	}

	@Bean
	public CrossRule getCrossDownRule() {
		return new CrossDownRule();

	}

	@Bean
	public LargerThanRule getLargerThanRule() {
		return new LargerThanRule();

	}

	@Bean
	public RuleImplService getRuleImplService() {
		return new RuleImplService();
	}

//	@Bean
//	public LoadCSVService getLoadCSVService() {
//		LoadCSVService service = new LoadCSVService();
//
//		service.setDataPath("data/histData/", "data/funData/");
//
//		return service;
//	}

	@Bean
	public MathOperatorService2 getMathOperatorService() {
		return new MathOperatorService2();
	}

	@Bean
	public InputParameterSet getInputParameterSet() {
		return new InputParameterSet();
	}

	@Bean
	public Map<String, Map<String, Map<String, String>>> initData() {
		return new HashMap<String, Map<String, Map<String, String>>>();
	}

	@Bean
	public DateTimeFormatter getDateTimeFormatter() {
		return DateTimeFormat.forPattern("yyyy-MM-dd");
	}

	@Bean
	public List<String> getSymbols() throws IOException {

//		LoadCSVService service = new LoadCSVService();
//
//		String path = "data/";
//		
//		return service.loadSymbols(path);
		return null;
	}
	
	@Bean
	public DateFormat getDateFormat()
	{
		return new SimpleDateFormat("yyyy-MM-dd");
	}

	

}