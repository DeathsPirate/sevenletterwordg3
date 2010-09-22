package seven.f10.g3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import seven.ui.Letter;
import seven.ui.PlayerBids;

public class History
{
	private static ArrayList<BidLog> bidLogList=new ArrayList<BidLog>();
	private static final Logger l=Logger.getLogger(History.class);
	private static double overallAdjustSum=0;
	private static double[] frequencyValue={9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 
		1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
	private static ArrayList[] marketValue=new ArrayList[26];
	
	public static int adjust(String bidStrategy, Letter bidLetter,  
		ArrayList<PlayerBids> cachedBids, int ourID)
	{
		double bid=0;
		double overallAdjust=0;
		int bidLetterIndex=bidLetter.getAlphabet()-'A';
		
		// first round
		if (cachedBids.size()!=0)
		{
			int lastRound=cachedBids.size()-1;
			PlayerBids lastBids=cachedBids.get(lastRound);
			int ourLastBid=lastBids.getBidvalues().get(ourID);
			
			// overall adjust
			double radiusMedian=setRadiusMedian(lastBids, ourID);
			if (radiusMedian!=-1)
				overallAdjust=0.5*(radiusMedian-ourLastBid);
			
			// store last letter market value
			Letter lastLetter=lastBids.getTargetLetter();
			int lastLetterIndex=lastLetter.getAlphabet()-'A';
			
			if (radiusMedian!=-1)
			{
				if (marketValue[lastLetterIndex]==null)
					marketValue[lastLetterIndex]=new ArrayList<Integer>();
				ListIterator<Integer> it=lastBids.getBidvalues().listIterator();
				while (it.hasNext())
					marketValue[lastLetterIndex].add(it.next());
			}
		}
		
		// strategy
		if (bidStrategy.equals("L"))
		{
			bid=0;
		}
		else if (bidStrategy.equals("M"))
		{
			if (marketValue[bidLetterIndex]==null)
				bid=frequencyValue[bidLetterIndex];
			else
			{
				int index=(int)0.5*marketValue[bidLetterIndex].size();
				Collections.sort(marketValue[bidLetterIndex]);
				bid=(Integer)marketValue[bidLetterIndex].get(index);
			}			
			overallAdjustSum+=overallAdjust;
			bid+=overallAdjustSum;
			l.error("bid M: overallAdjustSum="+overallAdjustSum);
		}		
		else if (bidStrategy.equals("H"))
		{
			if (marketValue[bidLetterIndex]==null)
				bid=frequencyValue[bidLetterIndex];
			else
			{
				int index=(int)0.9*marketValue[bidLetterIndex].size();
				Collections.sort(marketValue[bidLetterIndex]);
				bid=(Integer)marketValue[bidLetterIndex].get(index);
			}
			overallAdjustSum+=overallAdjust;
			bid+=overallAdjustSum;
			l.error("bid H: overallAdjustSum="+overallAdjustSum);
		}
		else if (bidStrategy.equals("7th"))
		{
			// Do not update meanAdjustSum
			double riskRate=1.5;
			bid=riskRate*(bidLetter.getValue()+50);
			//TODO if > current money <56
		}
		return (int)(bid+0.5);
	}
	
	public static double setRadiusMedian(PlayerBids lastBids, int ourID)
	{
		int sum=0;
		int top=0;
		ListIterator<Integer> it=lastBids.getBidvalues().listIterator();
		while (it.hasNext())
		{
			int temp=it.next();
			if (temp>top)
				top=temp;
			sum+=temp;
		}
		double mean=1.000*sum/lastBids.getBidvalues().size();
		double median=getMedian(lastBids.getBidvalues());		
		
		//store parameters in BidLog		
		BidLog bidLog=new BidLog();
		bidLog.setMean(mean);
		bidLog.setMedian(median);
		bidLog.setSum(sum);
		bidLog.setTop(top);
		
		//diffMean and Standard Deviation
		/*
		ArrayList<Double> diffMean=new ArrayList<Double>();
		bidLog.setDiffMean(diffMean);
		it=bid.getBidvalues().listIterator();
		double diff=0;
		double diffSum=0;
		while (it.hasNext())
		{
			diff=it.next()-mean;
			diffMean.add(diff);
			diffSum+=diff*diff;
		}
		double stdDev=Math.sqrt((diffSum/(diffMean.size()-1)));
		bidLog.setStdDev(stdDev);
		double devMean=getDevMean(bid.getBidvalues(), mean, stdDev, devRange);
		bidLog.setDevMean(devMean);
		*/
		double radiusMedian=getRadiusMean(lastBids.getBidvalues(), median);
		bidLog.setRadiusMedian(radiusMedian);
		bidLogList.add(bidLog);
		
		return radiusMedian;
	}
	
	public static double getMedian(ArrayList<Integer> arrayList)
	{
		ArrayList<Integer> list=(ArrayList<Integer>)arrayList.clone();
		Collections.sort(list);
		int len=list.size();
		if (len%2==0)
			return (list.get(len/2)+list.get(len/2-1))*1.000/2;
		else
			return list.get(len/2);
	}
	
	public static double getDevMean(ArrayList<Integer> arrayList, double mean, double stdDev, double range)
	{
		double min=mean-stdDev*range;
		double max=mean+stdDev*range;
		ListIterator<Integer> it=arrayList.listIterator();
		double sum=0;
		double count=0;
		double val=0;
		while (it.hasNext())
		{
			val=it.next();
			if (val>=min && val<=max)
			{
				sum+=val;
				count++;
			}
		}
		if (count==0)
			return -1;
		return 1.000*sum/count;
	}
	
	public static int getRadius(double mean)
	{
		return (int)(Math.sqrt(mean)+0.5);
	}
	
	public static double getRadiusMean(ArrayList<Integer> arrayList, double mean)
	{
		int radius=getRadius(mean);
		double min=mean-radius;
		double max=mean+radius;
		ListIterator<Integer> it=arrayList.listIterator();
		double sum=0;
		double count=0;
		double val=0;
		while (it.hasNext())
		{
			val=it.next();
			if (val>=min && val<=max)
			{
				sum+=val;
				count++;
			}
		}
		if (count==0)
			return -1;
		return 1.000*sum/count;
	}
}