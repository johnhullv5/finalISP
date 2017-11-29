package quant.domain;

import org.joda.time.DateTime;

public class EMAStateClass {
	
	public DateTime t;
	
	public double value;
	
	public int n;
	
	public EMAStateClass(DateTime t,double value,int n)
	{
		this.t= t;
		this.value = value;
		this.n = n;
		
	}
	

}