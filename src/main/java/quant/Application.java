package quant;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		
		//String json = gson.toJson(data);
		return null;
	}
    
    @Bean
	InitializingBean compute() {
		return () -> {
			
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