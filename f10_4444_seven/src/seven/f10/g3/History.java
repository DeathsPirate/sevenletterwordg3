package seven.f10.g3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import org.apache.log4j.Logger;
import seven.ui.Letter;
import seven.ui.PlayerBids;

public class History {

	private ArrayList<BidLog> bidLogList;
	private double[] frequencyValue = {8, 2, 3, 4, 10, 1, 
		3, 3, 8, 0, 1, 5, 3, 6, 6, 3, 0, 7, 8, 5, 4, 1, 1, 0, 2, 0};
	private ArrayList[] marketValue;
	private ArrayList<Integer> allBids;
	protected Logger l = Logger.getLogger(this.getClass());

	public History() {
		bidLogList = new ArrayList<BidLog>();
		frequencyValue = new double[26];
		marketValue = new ArrayList[26];
		allBids = new ArrayList();
	}

	public int adjust(String bidStrategy, Letter bidLetter,
			ArrayList<PlayerBids> cachedBids, int ourID) {
		double bid = 0;
		int bidLetterIndex = bidLetter.getAlphabet() - 'A';
		
		// round other than first round
		if (cachedBids.size() != 0) {
			l.trace("Not the first round!!!!!!");
			int lastRound = cachedBids.size() - 1;
			PlayerBids lastBids = cachedBids.get(lastRound);
			int ourLastBid = lastBids.getBidvalues().get(ourID);
			for(int i = 0; i < lastBids.getBidvalues().size(); i++){
				l.trace("just added: " + lastBids.getBidvalues().get(i));
				allBids.add(lastBids.getBidvalues().get(i));
			}

			// overall adjust
			double radiusMedian = setRadiusMedian(lastBids, ourID);
			/*
			 * if (radiusMedian!=-1)
			 * overallAdjust=0.5*(radiusMedian-ourLastBid);
			 */

			// store last letter market value
			Letter lastLetter = lastBids.getTargetLetter();
			int lastLetterIndex = lastLetter.getAlphabet() - 'A';

			if (radiusMedian != -1) {
				if (marketValue[lastLetterIndex] == null)
					marketValue[lastLetterIndex] = new ArrayList<Integer>();
				ListIterator<Integer> it = lastBids.getBidvalues()
						.listIterator();
				while (it.hasNext())
					marketValue[lastLetterIndex].add(it.next());
			}

		// strategy
		double strength = 0;
		if (bidStrategy.equals("L")) {
			strength = 0;
		} else if (bidStrategy.equals("M")) {
			strength = .5;

		} else if (bidStrategy.equals("H")) {
			strength = .9;
		}

		double overallAffect = .33;
		int indexm = -1;
		double m = 0;
		if (marketValue[bidLetterIndex] == null)
			overallAffect = 1;
		else {
			indexm = (int) strength * marketValue[bidLetterIndex].size();
			Collections.sort(marketValue[bidLetterIndex]);
			m = (1 - overallAffect)
					* (Integer) marketValue[bidLetterIndex].get(indexm);
		}
		int indexa = (int) strength * allBids.size();
		Collections.sort(allBids);
		bid = m + overallAffect * (Integer) allBids.get(indexa);

		}
		
		else return (int) (frequencyValue[(bidLetter.getValue() - 'A')]);
		
		return (int) (bid);
	}

	public double setRadiusMedian(PlayerBids lastBids, int ourID) {
		int sum = 0;
		int top = 0;
		ListIterator<Integer> it = lastBids.getBidvalues().listIterator();
		while (it.hasNext()) {
			int temp = it.next();
			if (temp > top)
				top = temp;
			sum += temp;
		}
		double mean = 1.000 * sum / lastBids.getBidvalues().size();
		double median = getMedian(lastBids.getBidvalues());

		// store parameters in BidLog
		BidLog bidLog = new BidLog();
		bidLog.setMean(mean);
		bidLog.setMedian(median);
		bidLog.setSum(sum);
		bidLog.setTop(top);

		// diffMean and Standard Deviation
		/*
		 * ArrayList<Double> diffMean=new ArrayList<Double>();
		 * bidLog.setDiffMean(diffMean); it=bid.getBidvalues().listIterator();
		 * double diff=0; double diffSum=0; while (it.hasNext()) {
		 * diff=it.next()-mean; diffMean.add(diff); diffSum+=diff*diff; } double
		 * stdDev=Math.sqrt((diffSum/(diffMean.size()-1)));
		 * bidLog.setStdDev(stdDev); double
		 * devMean=getDevMean(bid.getBidvalues(), mean, stdDev, devRange);
		 * bidLog.setDevMean(devMean);
		 */
		double radiusMedian = getRadiusMean(lastBids.getBidvalues(), median);
		bidLog.setRadiusMedian(radiusMedian);
		bidLogList.add(bidLog);

		return radiusMedian;
	}

	public double getMedian(ArrayList<Integer> arrayList) {
		ArrayList<Integer> list = (ArrayList<Integer>) arrayList.clone();
		Collections.sort(list);
		int len = list.size();
		if (len % 2 == 0)
			return (list.get(len / 2) + list.get(len / 2 - 1)) * 1.000 / 2;
		else
			return list.get(len / 2);
	}

	public double getDevMean(ArrayList<Integer> arrayList, double mean,
			double stdDev, double range) {
		double min = mean - stdDev * range;
		double max = mean + stdDev * range;
		ListIterator<Integer> it = arrayList.listIterator();
		double sum = 0;
		double count = 0;
		double val = 0;
		while (it.hasNext()) {
			val = it.next();
			if (val >= min && val <= max) {
				sum += val;
				count++;
			}
		}
		if (count == 0)
			return -1;
		return 1.000 * sum / count;
	}

	public int getRadius(double mean) {
		return (int) (Math.sqrt(mean) + 0.5);
	}

	public double getRadiusMean(ArrayList<Integer> arrayList, double mean) {
		int radius = getRadius(mean);
		double min = mean - radius;
		double max = mean + radius;
		ListIterator<Integer> it = arrayList.listIterator();
		double sum = 0;
		double count = 0;
		double val = 0;
		while (it.hasNext()) {
			val = it.next();
			if (val >= min && val <= max) {
				sum += val;
				count++;
			}
		}
		if (count == 0)
			return -1;
		return 1.000 * sum / count;
	}
}
