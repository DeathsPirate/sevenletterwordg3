package seven.f10.g3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;

import org.apache.log4j.Logger;

import seven.ui.PlayerBids;

public class History
{
	private static ArrayList<BidLog> bidLogList=new ArrayList<BidLog>();
	private static final Logger l=Logger.getLogger(History.class);;
	private static double topWeightAtStdDev=0.3;
	
	public static double adjust(ArrayList<PlayerBids> cachedBids, int ourID)
	{
		if (cachedBids.size()==0)
			return 0;
		int lastRound=cachedBids.size()-1;
		PlayerBids bid=cachedBids.get(lastRound);
		int sum=0;
		int top=0;
		ListIterator<Integer> it=bid.getBidvalues().listIterator();
		while (it.hasNext())
		{
			int temp=it.next();
			if (temp>top)
				top=temp;
			sum+=temp;
		}
		double mean=1.000*sum/bid.getBidvalues().size();
		double median=getMedian(bid.getBidvalues());
		
		
		//store parameters in BidLog		
		BidLog bidLog=new BidLog();
		bidLog.setMean(mean);
		bidLog.setMedian(median);
		bidLog.setSum(sum);
		bidLog.setTop(top);
		
		//diffMean and Standard Deviation
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
		double devMean=getDevMean(bid.getBidvalues(), mean, stdDev);
		bidLog.setDevMean(devMean);
		
		bidLogList.add(bidLog);
		
		double topWeight;
		double topDevMeanDiff=top-devMean;
		double topWeightC=topWeightAtStdDev/(1-topWeightAtStdDev)*stdDev;
		topWeight=topWeightC/(topWeightC+topDevMeanDiff);
		
		double adjust=top*topWeight+devMean*(1-topWeight)-bid.getBidvalues().get(ourID);
		l.trace("Top="+top+",StdDev="+stdDev+",Mean="+mean+",DevMean="+devMean
			+",topWeight="+topWeight+",Adjust="+adjust);
		if (adjust>=0)
			return adjust+0.5;
		else
			return adjust-0.5;
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
	
	public static double getDevMean(ArrayList<Integer> arrayList, double mean, double stdDev)
	{
		double min=mean-stdDev;
		double max=mean+stdDev;
		ListIterator<Integer> it=arrayList.listIterator();
		double sum=0;
		double count=0;
		double val=0;
		while (it.hasNext())
		{
			val=it.next();
			l.trace("getDevMean() val="+val);
			if (val>=min && val<=max)
			{
				sum+=val;
				count++;
			}
		}
		l.trace("getDevMean() sum="+sum);
		if (count==0)
			return mean;
		return 1.000*sum/count;
	}
}
