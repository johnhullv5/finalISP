package quant.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;

import rx.Observable;

public class RuleImplService {

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	public static Boolean isUP(double twoDayAgoPriceDiff, double lastDayPriceDiff, double todayPriceDiff) {
		if (todayPriceDiff > 0) {
			if (lastDayPriceDiff < 0) {
				return true;
			} else {
				if (lastDayPriceDiff == 0 && twoDayAgoPriceDiff < 0)
					return true;
				else
					return false;
			}

		} else {
			return false;
		}
	}

	public static Boolean isDown(double twoDayAgoPriceDiff, double lastDayPriceDiff, double todayPriceDiff) {
		if (todayPriceDiff < 0) {
			if (lastDayPriceDiff > 0) {
				return true;
			} else {
				if (lastDayPriceDiff == 0 && twoDayAgoPriceDiff > 0)
					return true;
				else
					return false;
			}

		} else {
			return false;
		}
	}

	public static Observable<Pair<DateTime, Double>> crossUP2(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> benchmark,int N) {

		Observable<Pair<DateTime, Double>> diff = close.skip(N-1).filter(x -> !x.getRight().isNaN()).zipWith(
				benchmark.filter(x -> !x.getRight().isNaN()),
				(c, i) -> new ImmutablePair(c.getLeft(), (c.getRight() - i.getRight())));
		//return diff;

		Observable<Pair<DateTime, Double>> diff_v = diff.map(b -> {
			double v = (b.getRight() > 0) ? 1.0 : (-1.0);
			return new ImmutablePair(b.getLeft(), v);
		});
		
		//return diff_v;
//
		Observable<Pair<DateTime, Double>> diff_map = diff_v.window(2, 1).skipLast(1).flatMap(win -> win.reduce((s, c) -> new ImmutablePair(c.getLeft(), c.getRight() - s.getRight())));
//		return diff_map;
		return diff_map.filter(x -> x.getRight() > 0);

	}

	public static Observable<Pair<DateTime, Double>> crossDown2(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> benchmark) {

		Observable<Pair<DateTime, Double>> diff = close.filter(x -> !x.getRight().isNaN()).zipWith(
				benchmark.filter(x -> !x.getRight().isNaN()),
				(c, i) -> new ImmutablePair(c.getLeft(), (c.getRight() - i.getRight())));


		Observable<Pair<DateTime, Double>> diff_v = diff.map(b -> {
			double v = (b.getRight() <= 0) ? 1.0 : (-1.0);
			return new ImmutablePair(b.getLeft(), v);
		});


		Observable<Pair<DateTime, Double>> diff_map = diff_v.window(2, 1).skipLast(1)
				.flatMap(win -> win.reduce((s, c) -> new ImmutablePair(c.getLeft(), c.getRight() - s.getRight())));
		return diff_map.filter(x -> x.getRight() > 0);

	}

	public static Observable<Pair<DateTime, Double>> largerThan(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> benchmark) {

		Observable<Pair<DateTime, Double>> diff = Observable.zip(close, benchmark,
				(c, b) -> new ImmutablePair(c.getLeft(), (c.getRight() - b.getRight())));

		Observable<Pair<DateTime, Double>> diff_v = diff.map(b -> {
			double v = (b.getRight() > 0) ? 1.0 : (0.0);
			return new ImmutablePair(b.getLeft(), v);
		});

		return diff_v;

	}

	public static Observable<Pair<DateTime, Double>> lessThan(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> benchmark) {

		Observable<Pair<DateTime, Double>> diff = Observable.zip(close, benchmark,
				(c, b) -> new ImmutablePair(c.getLeft(), (c.getRight() - b.getRight())));

		Observable<Pair<DateTime, Double>> diff_v = diff.map(b -> {
			double v = (b.getRight() < 0) ? 1.0 : (0.0);
			return new ImmutablePair(b.getLeft(), v);
		});

		return diff_v;

	}

	public static Observable<Pair<DateTime, Double>> crossUP(Observable<Pair<DateTime, Double>> o,
			Observable<Pair<DateTime, Double>> bench) {
		Observable<Pair<DateTime, Double>> diff = o.zipWith(bench,
				(c, b) -> new ImmutablePair(c.getLeft(), (c.getRight() - b.getRight())));

		Observable<Double> diff_v = diff.map(a -> a.getRight());

		Observable<DateTime> diff_t = diff.skip(2).map(a -> a.getLeft());

		Observable<Triple<Double, Double, Double>> signs = diff_v.window(3, 1).skipLast(2)
				.flatMap(win -> win.reduce(
						new ImmutableTriple<Double, Double, Double>(Double.NaN, Double.NaN, Double.NaN), (s,
								c) -> new ImmutableTriple<Double, Double, Double>(s.getMiddle(), s.getRight(), c)));

		Observable<Double> result = signs.map(x -> RuleImplService.isUP(x.getLeft(), x.getMiddle(), x.getRight()))
				.map(x -> {
					return (x) ? 1.0 : 0.0;
				});

		return Observable.zip(diff_t, result, (t, v) -> new ImmutablePair(t, v));

	}

	// need to test the cross down logic
	public static Observable<Pair<DateTime, Double>> crossDown(Observable<Pair<DateTime, Double>> o,
			Observable<Pair<DateTime, Double>> bench) {
		Observable<Pair<DateTime, Double>> diff = o.zipWith(bench,
				(c, b) -> new ImmutablePair(c.getLeft(), (c.getRight() - b.getRight())));

		Observable<Double> diff_v = diff.map(a -> a.getRight());

		Observable<DateTime> diff_t = diff.skip(2).map(a -> a.getLeft());

		Observable<Triple<Double, Double, Double>> signs = diff_v.window(3, 1).skipLast(2)
				.flatMap(win -> win.reduce(
						new ImmutableTriple<Double, Double, Double>(Double.NaN, Double.NaN, Double.NaN), (s,
								c) -> new ImmutableTriple<Double, Double, Double>(s.getMiddle(), s.getRight(), c)));

		Observable<Double> result = signs.map(x -> RuleImplService.isDown(x.getLeft(), x.getMiddle(), x.getRight()))
				.map(x -> {
					return (x) ? 1.0 : 0.0;
				});

		return Observable.zip(diff_t, result, (t, v) -> new ImmutablePair(t, v));

	}

	// first and second should have the same length.
	public Observable<Pair<DateTime, Double>> diffTwoSeries(Observable<Pair<DateTime, Double>> first,
			Observable<Pair<DateTime, Double>> second) {
		return Observable.zip(first, second, (t, v) -> new ImmutablePair(t.getLeft(), t.getRight() - v.getRight()));
	}

}
