package quant.config;

import org.springframework.beans.factory.annotation.Value;

public class InputParameterSet {
	
	@Value("${rule.sma.n}")
	private int smaN1;
	
	
	@Value("${config.take.n}")
	private int takeN;
	
	@Value("${rule.ema.n}")
	private int emaN1;
	
			
	@Value("${rule.macd.n1}")
	private int macdN1;
			
	@Value("${rule.macd.n2}")
	private int macdN2;
	
	@Value("${rule.macd.s}")
	private int macdS;
	
	
	@Value("${rule.macd.line1}")
	private double macdLine1;
	
	
	
	@Value("${rule.bband.n}")
	private int bbandN;
	
	@Value("${rule.bband.a}")
	private double bbanda;
	
	@Value("${rule.rsi.n}")
	private int rsiN;
	
	@Value("${rule.rsi.line1}")
	private double rsiLine1;
	
	@Value("${rule.rsi.line2}")
	private double rsiLine2;
	
	@Value("${rule.william.n}")
	private int willN;
	
	@Value("${rule.william.line1}")
	private double willLn1;
	
	@Value("${rule.william.line2}")
	private double willLn2;
	
	@Value("${rule.adosc.n1}")
	private int adoscN1;
	
	@Value("${rule.adosc.n2}")
	private int adoscN2;
	
	@Value("${rule.tma.n}")
	private int tmaN;
	
	@Value("${rule.kdj.n}")
	private int kdjN;
	
	@Value("${rule.kdj.a}")
	private int kdjA;
	
	@Value("${database}")
	private String  database;
	
	@Value("${dbusername}")
	private String  dbusername;
	
	@Value("${dbpasswd}")
	private String  dbpasswd;
	
	public String getDataBase()
	{
		return database;
	}
	
	public String getDBusername()
	{
		return dbusername;
	}
	
	public String getDBPasswd()
	{
		return dbpasswd;
	}
	
	
	public int getKDJ_N()
	{
		return kdjN;
	}
	
	public int getKDJ_A()
	{
		return kdjA;
	}
	
	public int getBBAND_N()
	{
		return bbandN;
	}
	
	
	public double getBBAND_A()
	{
		return bbanda;
	}
	
	public int getRSI_N()
	{
		return rsiN;
	}
	
	public double getRSI_Line1()
	{
		return rsiLine1;
	}
	
	public double getRSI_Line2()
	{
		return rsiLine2;
	}
	
	
	public int getWILL_N()
	{
		return willN;
	}
	
	
	public double getWillLn1()
	{
		return willLn1;
	}
	
	public double getWillLn2()
	{
		return willLn2;
	}
	
	public int getADOSC_N1()
	{
		return adoscN1;
	}
	
	public int getADOSC_N2()
	{
		return adoscN2;
	}
	
	public int getTMA_N()
	{
		return tmaN;
	}
	
	
	
	
	 
	
	public int getSMA_N()
	{
		return smaN1;
	}
	
	public int getEMA_N()
	{
		return emaN1;
	}
	
	public int getMACDN1()
	{
		return macdN1;
	}
	
	public int getMACDN2()
	{
		return macdN2;
	}
	
	public int getMACDS()
	{
		return macdS;
	}
	
	public double getMacdLine1()
	{
		return macdLine1;
	}
	
	public int getTake_N()
	{
		return takeN;
	}
	
	

}