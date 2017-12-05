package quant.serviceImpl;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import quant.service.CrossRule;
import quant.service.Rule;

import rx.Observable;

public class CrossUpRule implements CrossRule {
	
	private Observable<Pair<DateTime, Double>> close;
	
	private Observable<Pair<DateTime, Double>> base;
	
	private int N;
	
	
	

	public int getN() {
		return N;
	}


	public void setN(int n) {
		N = n;
	}


	@Override
	public Observable<Pair<DateTime, Double>> runRule(RuleImplService ruleUtils) {
		// TODO Auto-generated method stub
		return ruleUtils.crossUP2(this.close, this.base,N);
	}
	
	
	@Override
	public Observable<Pair<DateTime, Double>> runRule2(RuleImplService ruleUtils) {
		// TODO Auto-generated method stub
		return ruleUtils.crossUP2(this.close, this.base,N);
	}

	@Override
	public void setClose(Observable<Pair<DateTime, Double>> close) {
		this.close = close;
		
	}

	@Override
	public void setBase(Observable<Pair<DateTime, Double>> base) {
		this.base = base;
		
	}


	@Override
	public Rule andRule(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	public Observable<Pair<DateTime, Double>> runRule(com.COMP313.ISP.service.RuleImplService ruleUtils) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//	@Override
//	public Observable<Pair<DateTime, Double>> runRule(com.COMP313.ISP.service.RuleImplService ruleUtils) {
//		// TODO Auto-generated method stub
//		return null;
//	}

}
