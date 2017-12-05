package quant.serviceImpl;
//import com.COMP313.ISP.service.*;
//import com.COMP313.ISP.service.Rule;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import quant.service.*;

import rx.Observable;

public class LargerThanRule implements CrossRule {
	
	

	private Observable<Pair<DateTime, Double>> first;

	private Observable<Pair<DateTime, Double>> second;
	
	private double line1;
	
	private double line2;

	
	@Override
	public Observable<Pair<DateTime, Double>> runRule(RuleImplService ruleUtils) {

		Observable<DateTime> timeStamp = this.first.map(x->x.getLeft());
		Observable<Double> o = this.first.map(x->this.line1);
		Observable<Double> o2 = this.first.map(x->this.line2);
		Observable<Pair<DateTime, Double>> line1 = Observable.zip(timeStamp, o,(a1,a2)->new ImmutablePair(a1,a2));
		
		// need to test!!!!
		return ruleUtils.crossUP2(this.first, line1,10);
		

	}
	

	
	public Observable<Pair<DateTime, Double>> run2Rule(RuleImplService ruleUtils) {

		Observable<DateTime> timeStamp = this.first.map(x->x.getLeft());
		Observable<Double> o = this.first.map(x->this.line1);
		Observable<Double> o2 = this.first.map(x->this.line2);
		Observable<Pair<DateTime, Double>> line1 = Observable.zip(timeStamp, o,(a1,a2)->new ImmutablePair(a1,a2));
		Observable<Pair<DateTime, Double>> line2 = Observable.zip(timeStamp, o2,(a1,a2)->new ImmutablePair(a1,a2));
		
		// need to test!!!! 0 is hard coded.
		Observable<Pair<DateTime, Double>> result = ruleUtils.crossUP2(this.first, line1,0).mergeWith(ruleUtils.crossUP2(this.first, line2,0));
		

		
		return result;
		

	}

	@Override
	public Rule andRule(Rule rule) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClose(Observable<Pair<DateTime, Double>> close) {
		this.first = close;

	}

	@Override
	public void setBase(Observable<Pair<DateTime, Double>> base) {
		this.second = base;
	}

	@Override
	public Observable<Pair<DateTime, Double>> runRule2(RuleImplService ruleUtils) {
		// TODO Auto-generated method stub
		return null;
	}

	public double getLine1() {
		return line1;
	}

	public void setLine1(double line1) {
		this.line1 = line1;
	}

	public double getLine2() {
		return line2;
	}

	public void setLine2(double line2) {
		this.line2 = line2;
	}

}
