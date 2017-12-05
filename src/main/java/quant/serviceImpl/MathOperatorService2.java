package quant.serviceImpl;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

import org.apache.commons.lang3.tuple.*;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import quant.domain.*;

import rx.Observable;
import rx.functions.*;
import rx.math.operators.OperatorMinMax;
import rx.observables.MathObservable;

public class MathOperatorService2 {

	public static Observable<Pair<DateTime, Double>> getClose(String symbol, DateTime begin) {
		return null;

	}

	public static Observable<Double> movingAverage(Observable<Double> o, int N) {
		return o.window(N, 1).flatMap(new Func1<Observable<Double>, Observable<Double>>() {
			public Observable<Double> call(Observable<Double> window) {
				return MathObservable.averageDouble(window);
			}
		});
	}

	public static Observable<Double> avg(Observable<Double> o, int N) {
		return o.window(N, 1).flatMap((win) -> {
			return win.reduce(0.0, (s, e) -> (s + e))

					.map(s -> s / N);

		}).skipLast(N - 1);
	}

	public static Observable<Pair<DateTime, Double>> STD(Observable<Pair<DateTime, Double>> data, int N) {
		Observable<DateTime> timeStamp = data.map(x -> x.getLeft()).skip(N - 1);
		Observable<Double> data_v = data.map(x -> x.getRight());
		Observable<Double> avg_v = MathOperatorService2.avg(data_v, N);
		Observable<Double> o = Observable.zip(data_v.window(N, 1).skipLast(N - 1),

				avg_v, (Observable<Double> a, Double b) -> {
					return a.map(item -> (item - b) * (item - b)).reduce((s, c) -> (s + c))
							.map(x -> Math.sqrt(x / (N - 1))).first().toBlocking().last();
				});
		return Observable.zip(timeStamp, o, (a1, a2) -> new ImmutablePair(a1, a2));
	}

	public static Observable<Pair<DateTime, Double>> MeanDeviation(Observable<Pair<DateTime, Double>> data, int N) {
		Observable<DateTime> timeStamp = data.map(x -> x.getLeft()).skip(N - 1);
		Observable<Double> data_v = data.map(x -> x.getRight());
		Observable<Double> avg_v = MathOperatorService2.avg(data_v, N);
		Observable<Double> o = Observable.zip(data_v.window(N, 1).skipLast(N - 1),

				avg_v, (Observable<Double> a, Double b) -> {
					return a.map(item -> Math.abs(item - b)).reduce((s, c) -> (s + c)).map(x -> x / N).first()
							.toBlocking().last();
				});
		return Observable.zip(timeStamp, o, (a1, a2) -> new ImmutablePair(a1, a2));
	}

	public static Observable<Double> highOfHigh(Observable<Double> o, int N) {
		return o.window(N, 1).flatMap(new Func1<Observable<Double>, Observable<Double>>() {
			public Observable<Double> call(Observable<Double> window) {
				return OperatorMinMax.max(window);
			}
		});
	}

	public static Observable<Double> lowOfLow(Observable<Double> o, int N) {
		return o.window(N, 1).flatMap(new Func1<Observable<Double>, Observable<Double>>() {
			public Observable<Double> call(Observable<Double> window) {
				return OperatorMinMax.min(window);
			}
		});
	}

//	// improve hard coding weights.
//	public static double calculateUNDERVALUEIndicator(FunData data) {
//		return data.getTrillingPE() * 0.3 + data.getPB() * 0.3 + data.getEV() * 0.2 + data.getPriceToSales() * 0.2;
//	}
//
//	public static double calculateGROWTHIndicator(FunData data) {
//		if ((data.getEarningsQuarterlyGrowth() > 0.1) && (data.getRevenueGrowth() > 0.1)) {
//			return 1.0;
//		} else {
//			return 0.0;
//		}
//	}

//	public static double calculateCASHFLOWIndicator(FunData data) {
//		if (data.getOperatingCashflow() > data.getNetIncomeToCommon()) {
//			return 1.0;
//		} else {
//			return 0.0;
//		}
//	}
//
//	public static double calculateYIELDSIndicator(FunData data) {
//		if (data.getYield() > 0.05) {
//			return 1.0;
//		} else {
//			return 0.0;
//		}
//	}

	// UNDERVALUE indicator value.
//	public static Observable<Pair<DateTime, Double>> UNDERVALUE(Observable<Pair<DateTime, FunData>> data) {
//		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> calculateUNDERVALUEIndicator(p.getRight())),
//				(t, p) -> new ImmutablePair(t, p));
//	}
//
//	// GROWTH Indicator
//	public static Observable<Pair<DateTime, Double>> GROWTH(Observable<Pair<DateTime, FunData>> data) {
//		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> calculateGROWTHIndicator(p.getRight())),
//				(t, p) -> new ImmutablePair(t, p));
//	}
//
//	// CASHFLOW Indicator
//	public static Observable<Pair<DateTime, Double>> CASHFLOW(Observable<Pair<DateTime, FunData>> data) {
//		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> calculateCASHFLOWIndicator(p.getRight())),
//				(t, p) -> new ImmutablePair(t, p));
//	}
//
//	// YIELDS Indicator
//	public static Observable<Pair<DateTime, Double>> YIELDS(Observable<Pair<DateTime, FunData>> data) {
//		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> calculateYIELDSIndicator(p.getRight())),
//				(t, p) -> new ImmutablePair(t, p));
//	}

	// use adjclose instead of close.
	public static Observable<Pair<DateTime, Double>> CLOSE(Observable<Pair<DateTime, HisData>> data) {
		return Observable.zip(data.map(p -> p.getLeft()),
				data.map(p -> Double.parseDouble(p.getRight().getClosePrice())), (t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> HIGH(Observable<Pair<DateTime, HisData>> data) {
		return Observable.zip(data.map(p -> p.getLeft()),
				data.map(p -> Double.parseDouble(p.getRight().getHighPrice())), (t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> LOW(Observable<Pair<DateTime, HisData>> data) {
		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> Double.parseDouble(p.getRight().getLowPrice())),
				(t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> VOLUME(Observable<Pair<DateTime, HisData>> data) {
		return Observable.zip(data.map(p -> p.getLeft()), data.map(p -> Double.parseDouble(p.getRight().getVolume())),
				(t, p) -> new ImmutablePair(t, p));
	}

	// SMA function
	public static Observable<Pair<DateTime, Double>> SMA(Observable<Pair<DateTime, Double>> o, int N) {
		return Observable.zip(o.map(t -> t.getLeft()).skip(N - 1),
				MathOperatorService2.movingAverage(o.map(t -> t.getRight()), N).skipLast(N - 1),
				(t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> HOH(Observable<Pair<DateTime, Double>> o, int N) {
		return Observable.zip(o.map(t -> t.getLeft()).skip(N - 1),
				MathOperatorService2.highOfHigh(o.map(t -> t.getRight()), N).skipLast(N - 1),
				(t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> LOL(Observable<Pair<DateTime, Double>> o, int N) {
		return Observable.zip(o.map(t -> t.getLeft()).skip(N - 1),
				MathOperatorService2.lowOfLow(o.map(t -> t.getRight()), N).skipLast(N - 1),
				(t, p) -> new ImmutablePair(t, p));
	}

	public static Observable<Pair<DateTime, Double>> WILLIAM(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> hoh, Observable<Pair<DateTime, Double>> lol, int N) {

		Observable<DateTime> time = close.map(x -> x.getLeft()).skip(N - 1);
		Observable<Double> close_value = close.map(x -> x.getRight()).skip(N - 1);
		Observable<Double> hoh_value = hoh.map(x -> x.getRight());
		Observable<Double> lol_value = lol.map(x -> x.getRight());
		Observable<Double> result = Observable.zip(close_value, hoh_value, lol_value,
				(c, hn, ln) -> MathOperatorService2.william_func(c, hn, ln));
		return Observable.zip(time, result, (t, p) -> new ImmutablePair(t, p * (-1)));

	}

	public static double william_func(double c, double hn, double ln) {
		return 100 - (c - ln) / (hn - ln) * 100;
	}

	public static EMAStateClass updateEMAState(EMAStateClass ema, Pair<DateTime, Double> e, double multiplier) {
		EMAStateClass newEMA = new EMAStateClass(e.getLeft(), 0.0, 0);

		if (ema.n == 0) {
			newEMA.n += 1;
			return newEMA;
		} else {
			newEMA.n = ema.n + 1;
			newEMA.value = (e.getRight() - ema.value) * multiplier + ema.value;
			return newEMA;
		}
	}

	public static Observable<Pair<DateTime, Double>> EMATEST(Observable<Pair<DateTime, Double>> close, int N) {
		double multiplier = 2.0 / (N + 1);
		Observable<Pair<DateTime, Double>> sma = MathOperatorService2.SMA(close, N).first();
		double smaValue = sma.toBlocking().last().getRight();
		Observable<EMAStateClass> result = close.skip(N - 1).scan(new EMAStateClass(DateTime.now(), smaValue, 0),
				(s, e) -> MathOperatorService2.updateEMAState(s, e, multiplier)).skip(1);

		return result.map((EMAStateClass x) -> new ImmutablePair(x.t, x.value));
	}

	public static Observable<Pair<DateTime, Double>> EMA(Observable<Pair<DateTime, Double>> close, int N) {
		double multiplier = 2.0 / (N + 1);
		Observable<Pair<DateTime, Double>> sma = MathOperatorService2.SMA(close, N).first();
		double smaValue = sma.toBlocking().last().getRight();
		Observable<EMAStateClass> result = close.skip(N - 1).scan(new EMAStateClass(DateTime.now(), smaValue, 0), (s, e) -> {
			s.t = e.getLeft();
			if (s.n < 1)
				s.value = smaValue;
			else {
				s.value = (e.getRight() - s.value) * multiplier + s.value;
			}

			s.n += 1;
			return s;
		}).skip(1);
		return result.map((EMAStateClass x) -> new ImmutablePair(x.t, x.value));
	}

	// need s< N1 < N2 !!!!
	public static Observable<Pair<DateTime, Triple<Double, Double, Double>>> MACD(
			Observable<Pair<DateTime, Double>> close, int N1, int N2, int S) {

		Observable<Pair<DateTime, Double>> ema1 = MathOperatorService2.EMA(close, N1).skip(N2 - N1);

		Observable<Pair<DateTime, Double>> ema2 = MathOperatorService2.EMA(close, N2);

		Observable<Pair<DateTime, Double>> macd_v = Observable.zip(ema1, ema2, (x1, x2) -> {

			return new ImmutablePair(x1.getLeft(), x1.getRight() - x2.getRight());
		});

		List<Pair<DateTime, Double>> listMACD = macd_v.toList().toBlocking().single();

		Observable<Pair<DateTime, Double>> signal = MathOperatorService2.EMA(Observable.from(listMACD), S);

		List<Pair<DateTime, Double>> listSignal = signal.toList().toBlocking().single();// .skip(N2-S);

		return Observable.zip(Observable.from(listMACD).skip(S - 1), Observable.from(listSignal),
				(t1, s) -> (new ImmutablePair(t1.getLeft(), new ImmutableTriple<Double, Double, Double>(t1.getRight(),
						s.getRight(), t1.getRight() - s.getRight()))));
	}

	public static Observable<Pair<DateTime, Double>> VMA(Observable<Pair<DateTime, Double>> close, int N, double VI) {
		double alpha = 2.0 / (N + 1);
		return close
				.scan(new ImmutablePair(DateTime.now(), 0.0), (Pair<DateTime, Double> s, Pair<DateTime, Double> e) -> {
					return new ImmutablePair(e.getLeft(),
							(alpha * VI * e.getRight() + (1 - alpha * VI) * s.getRight()));
				}).skip(1);
	}

	// need N1 < N2 !!!!
	public static Observable<Pair<DateTime, Double>> MAOSC(Observable<Pair<DateTime, Double>> close, int N1, int N2) {
		Observable<Pair<DateTime, Double>> sma1 = MathOperatorService2.SMA(close, N1).skip(N2 - N1);
		Observable<Pair<DateTime, Double>> sma2 = MathOperatorService2.SMA(close, N2);
		return Observable.zip(sma1, sma2, (s1, s2) -> new ImmutablePair(s1.getLeft(), (s1.getRight() - s2.getRight())));
	}

	public static Observable<Pair<DateTime, Triple<Double, Double, Double>>> BOLL(
			Observable<Pair<DateTime, Double>> close, int N, double alpha) {
		Observable<Pair<DateTime, Double>> middleLine = MathOperatorService2.SMA(close, N);
		Observable<Pair<DateTime, Double>> std = MathOperatorService2.STD(close, N);
		Observable<Pair<DateTime, Double>> upperBound = Observable.zip(middleLine, std,
				(m, d) -> new ImmutablePair(m.getLeft(), m.getRight() + d.getRight() * alpha));
		Observable<Pair<DateTime, Double>> lowerBound = Observable.zip(middleLine, std,
				(m, d) -> new ImmutablePair(m.getLeft(), m.getRight() - d.getRight() * alpha));

		return Observable.zip(lowerBound, middleLine, upperBound, (a, b, c) -> new ImmutablePair(a.getLeft(),
				new ImmutableTriple<Double, Double, Double>(a.getRight(), b.getRight(), c.getRight())));
	}

	// need N1 < N2 !!!!
	public static Observable<Pair<DateTime, Double>> GOC(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, int N1, int N2) {
		Observable<Pair<DateTime, Double>> hoh_N1 = MathOperatorService2.HOH(high, N1);
		Observable<Pair<DateTime, Double>> hoh_N2 = MathOperatorService2.HOH(high, N2);
		Observable<Pair<DateTime, Double>> lol_N1 = MathOperatorService2.LOL(low, N1);
		Observable<Pair<DateTime, Double>> lol_N2 = MathOperatorService2.LOL(low, N2);

		Observable<Pair<DateTime, Double>> conversionLine = Observable.zip(hoh_N1, lol_N1,
				(h, l) -> new ImmutablePair(h.getLeft(), (h.getRight() + l.getRight()) / 2));

		Observable<Pair<DateTime, Double>> baseLine = Observable.zip(hoh_N2, lol_N2,
				(h, l) -> new ImmutablePair(h.getLeft(), (h.getRight() + l.getRight()) / 2));

		// N1 < N2,so skip (N2-N1) items

		Observable<Pair<DateTime, Double>> diff = Observable.zip(conversionLine.skip(N2 - N1), baseLine,
				(h, l) -> new ImmutablePair(h.getLeft(), h.getRight() - l.getRight()));

		Observable<DateTime> t = diff.map(a -> a.getLeft()).skip(1);
		Observable<Double> v = diff.map(a -> a.getRight()).window(2, 1)
				.flatMap(win -> win.reduce(1.0, (s, c) -> s * c));

		return Observable.zip(t, v, (a, b) -> new ImmutablePair(a, b));

	}

	// one order diff of timeseries
	public static Observable<Pair<DateTime, Double>> DIFF(Observable<Pair<DateTime, Double>> o) {

		Observable<DateTime> t = o.map(x -> x.getLeft()).skip(1);
		Observable<Double> v = o.map(x -> x.getRight()).window(2, 1).skipLast(1)
				.flatMap(win -> win.reduce(0.0, (s, c) -> {
					return ((-1) * s + c);
				}));

		return Observable.zip(t, v, (a, b) -> new ImmutablePair(a, b));

	}

	public static double upperLadderFunction(double a) {
		if (a >= 0)
			return a;
		else {
			return 0.0;
		}

	}

	public static double lowerLadderFunction(double a) {
		if (a <= 0)
			return a;
		else {
			return 0.0;
		}

	}

	public static Observable<Pair<DateTime, Double>> RSI(Observable<Pair<DateTime, Double>> close, int N) {
		Observable<Pair<DateTime, Double>> diff = MathOperatorService2.DIFF(close);

		Observable<Pair<DateTime, Double>> uSeries = diff
				.map(x -> new ImmutablePair(x.getLeft(), upperLadderFunction(x.getRight())));

		Observable<Pair<DateTime, Double>> lSeries = diff
				.map(x -> new ImmutablePair(x.getLeft(), lowerLadderFunction(x.getRight())));

		Observable<Pair<DateTime, Double>> emaU = MathOperatorService2.SMA(uSeries, N);

		Observable<Pair<DateTime, Double>> emaL = MathOperatorService2.SMA(lSeries, N);

		return Observable.zip(emaU, emaL,
				(h, l) -> new ImmutablePair(h.getLeft(), 100 - 100 / (1 - h.getRight() / l.getRight())));
	}

	// TODO need to test...
	public static Observable<Pair<DateTime, Double>> CMCI(Observable<Pair<DateTime, Double>> tp, int N,
			double multiplier) {

		Observable<Pair<DateTime, Double>> meanDeviation = MathOperatorService2.MeanDeviation(tp, N);

		Observable<Pair<DateTime, Double>> avg = MathOperatorService2.SMA(tp, N);

		return Observable.zip(tp.skip(N - 1), avg, meanDeviation, (a, b, c) -> new ImmutablePair(a.getLeft(),
				(a.getRight() - b.getRight()) / (multiplier * c.getRight())));

	}

	public static double compareUpMoveAndDownMove(double u, double d) {
		if (u > d && u > 0) {
			return u;
		} else {
			return 0.0;
		}
	}

	public static Observable<Pair<DateTime, Double>> crossUP(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> benchmark) {

		Observable<Pair<DateTime, Double>> diff = close.zipWith(benchmark,
				(c, i) -> new ImmutablePair(c.getLeft(), (c.getRight() - i.getRight())));

		Observable<Pair<DateTime, Double>> diff_v = diff.map(b -> {
			double v = (b.getRight() > 0) ? 1.0 : (-1.0);
			return new ImmutablePair(b.getRight(), v);
		});

		Observable<Pair<DateTime, Double>> diff_map = diff_v.window(2, 1).skipLast(1)
				.flatMap(win -> win.reduce((s, c) -> new ImmutablePair(c.getLeft(), c.getRight() - s.getRight())));

		return diff_map.filter(x -> x.getRight() > 0);

	}

	public static Observable<Triple<DateTime, Double, Double>> SHIFT(Observable<Pair<DateTime, Double>> close) {
		Observable<DateTime> t = close.map(x -> x.getLeft()).skip(1);
		Observable<Double> v = close.map(x -> x.getRight());
		Observable<Pair<Double, Double>> twoItems = v.window(2, 1).skipLast(1)
				.flatMap(win -> win.reduce(new ImmutablePair<Double, Double>(Double.NaN, Double.NaN), (s, c) -> {
					return new ImmutablePair(s.getRight(), c);
				}));

		return Observable.zip(t, twoItems,
				(a, b) -> new ImmutableTriple<DateTime, Double, Double>(a, b.getLeft(), b.getRight()));
	}

	public static Observable<Triple<DateTime, Double, Double>> KDJ(Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> high, Observable<Pair<DateTime, Double>> low, int N, int A) {

		Observable<Pair<DateTime, Double>> hoh = MathOperatorService2.HOH(high, N);
		Observable<Pair<DateTime, Double>> lol = MathOperatorService2.LOL(low, N);

		Observable<Pair<DateTime, Double>> k = Observable.zip(close.skip(N - 1), hoh, lol,
				(c, h, l) -> new ImmutablePair(c.getLeft(),
						100 * (c.getRight() - l.getRight()) / (h.getRight() - l.getRight())));

		Observable<Pair<DateTime, Double>> d = MathOperatorService2.SMA(k, A);

		Observable<Triple<DateTime, Double, Double>> result = Observable.zip(k.skip(A - 1), d,
				(k_, d_) -> new ImmutableTriple(k_.getLeft(), k_.getRight(), d_.getRight()));
		return result;

	}

	public static Observable<Pair<DateTime, Double>> TR(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, Observable<Pair<DateTime, Double>> close) {
		// automatically skip 1 item.
		Observable<Triple<DateTime, Double, Double>> close_v = MathOperatorService2.SHIFT(close);

		return Observable.zip(high.skip(1), low.skip(1), close_v,
				(h, l, c) -> new ImmutablePair(h.getLeft(), Math.max((h.getRight() - l.getRight()),
						Math.max((h.getRight() - c.getMiddle()), (l.getRight() - c.getMiddle())))));

	}

	// TODO need test heavily...
	public static Observable<Pair<DateTime, Double>> DMI(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, Observable<Pair<DateTime, Double>> close, int N) {

		// 1.upmove
		Observable<Pair<DateTime, Double>> upMove = MathOperatorService2.DIFF(high);

		// 2.DownMove
		Observable<Pair<DateTime, Double>> downMove = MathOperatorService2.DIFF(low);

		Observable<Pair<DateTime, Double>> plusDM = Observable.zip(upMove, downMove,
				(u, d) -> (new ImmutablePair(u.getLeft(),
						MathOperatorService2.compareUpMoveAndDownMove(u.getRight(), d.getRight()))));

		Observable<Pair<DateTime, Double>> minusDM = Observable.zip(upMove, downMove,
				(u, d) -> (new ImmutablePair(u.getLeft(),
						MathOperatorService2.compareUpMoveAndDownMove(d.getRight(), u.getRight()))));

		Observable<Pair<DateTime, Double>> tr = MathOperatorService2.TR(high, low, close);

		Observable<Double> tr_v = tr.map(x -> x.getRight());
		Observable<DateTime> tr_t = tr.skip(N - 1).map(x -> x.getLeft());
		double initial_v = MathOperatorService2.SMA(tr, N).first().map(x -> x.getRight()).toBlocking().last();

		Observable<Double> atr_v = tr_v.window(N, 1).skipLast(N - 1).flatMap(win -> win.reduce(initial_v, (s, c) -> {
			return (s * (N - 1) + c) / N;
		}));

		// TODO the timestamp matches?
		Observable<Pair<DateTime, Double>> atr = Observable.zip(tr_t, atr_v, (t, v) -> new ImmutablePair(t, v));

		Observable<Pair<DateTime, Double>> plusADM = MathOperatorService2.SMA(plusDM, N);

		Observable<Pair<DateTime, Double>> minusADM = MathOperatorService2.SMA(minusDM, N);

		Observable<Pair<DateTime, Double>> plusDL = Observable.zip(plusADM, atr,
				(adm, r) -> new ImmutablePair(adm.getLeft(), (adm.getRight() / r.getRight())));

		Observable<Pair<DateTime, Double>> minusDL = Observable.zip(minusADM, atr,
				(adm, r) -> new ImmutablePair(adm.getLeft(), (adm.getRight() / r.getRight())));

		Observable<Pair<DateTime, Double>> dx = Observable.zip(plusDL, minusDL, (a, b) -> new ImmutablePair(a.getLeft(),
				((a.getRight() - b.getRight()) / (a.getRight() + b.getRight()))));

		double initial_dx = MathOperatorService2.SMA(dx, N).first().map(x -> x.getRight()).toBlocking().last();

		Observable<DateTime> dx_t = dx.skip(N - 1).map(x -> x.getLeft());

		Observable<Double> dx_v = dx.map(x -> x.getRight());

		Observable<Double> adx_v = dx_v.window(N, 1).skipLast(N - 1).flatMap(win -> win.reduce(initial_dx, (s, c) -> {
			return (s * (N - 1) + c) / N;
		}));

		return Observable.zip(dx_t, adx_v, (t, v) -> new ImmutablePair(t, v));

	}

	public static Observable<Pair<DateTime, Double>> KBAND(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, Observable<Pair<DateTime, Double>> close, int N) {
		return null;
	}

	// TODO need test....
	public static Observable<Pair<DateTime, Double>> TMA(Observable<Pair<DateTime, Double>> close, int N) {
		Observable<Pair<DateTime, Double>> sum = MathOperatorService2.SMA(close, N);
		for (int i = 1; i < N; i++) {
			sum.zipWith(MathOperatorService2.SMA(close, N - i).skip(i),
					(a, b) -> (new ImmutablePair(a.getLeft(), (a.getRight() + b.getRight()))));
		}

		return sum.map(x -> new ImmutablePair(x.getLeft(), x.getRight() / N));
	}

	// TODO need test....
	public static Observable<Pair<DateTime, Double>> REX(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> open, int N1, int N2) {
		Observable<Pair<DateTime, Double>> tvb = Observable.zip(close, high, low, open, (c, h, l,
				o) -> new ImmutablePair(c.getLeft(), (3 * c.getRight() - h.getRight() - l.getRight() - o.getRight())));

		return MathOperatorService2.EMA(MathOperatorService2.SMA(tvb, N1), N2);
	}

	// TODO not implement lack the ADL initial value. need test
	public static Observable<Pair<DateTime, Double>> ADO(Observable<Pair<DateTime, Double>> high,
			Observable<Pair<DateTime, Double>> low, Observable<Pair<DateTime, Double>> close,
			Observable<Pair<DateTime, Double>> volume, int N1, int N2) {
		Observable<Pair<DateTime, Double>> moneyMultiplier = Observable.zip(high, low, close,
				(h, l, c) -> new ImmutablePair(h.getLeft(),
						((c.getRight() - l.getRight()) - (h.getRight() - c.getRight()))
								/ (h.getRight() - l.getValue())));

		Observable<Pair<DateTime, Double>> moneyMultiplierVolume = Observable.zip(moneyMultiplier, volume,
				(m, v) -> new ImmutablePair(m.getLeft(), (m.getRight() * v.getRight())));
		// need skip 1?
		Observable<DateTime> t = moneyMultiplierVolume.skip(1).map(x -> x.getLeft());
		Observable<Double> v = moneyMultiplierVolume.map(x -> x.getRight());

		Observable<Double> adl = v.window(2, 1).skipLast(1).flatMap(win -> win.reduce(0.0, (s, c) -> (s + c)));

		return Observable.zip(t, adl, (a, b) -> new ImmutablePair(a, b));

	}

}
