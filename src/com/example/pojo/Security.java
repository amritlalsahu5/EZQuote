package com.example.pojo;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */


import java.io.Serializable;



public class Security implements Serializable{
	
	String companyName, symbol,marketCapitalization,changePercentage;
	String daysLow,daysHigh,yearLow,yearHigh,volume,peRatio,avgVolume,yield;
	String askPrice,  change;


public String getAvgVolume() {
	return avgVolume;
}

public void setAvgVolume(String avgVolume) {
	this.avgVolume = avgVolume;
}

public String getYield() {
	return yield;
}

public void setYield(String yield) {
	this.yield = yield;
}
	public String getMarketCapitalization() {
		return marketCapitalization;
	}

	public void setMarketCapitalization(String marketCapitalization) {
		this.marketCapitalization = marketCapitalization;
	}

	public String getChangePercentage() {
		return changePercentage;
	}

	public void setChangePercentage(String changePercentage) {
		this.changePercentage = changePercentage;
	}


	public String getAskPrice() {
		return askPrice;
	}

	public void setAskPrice(String askPrice) {
		this.askPrice = askPrice;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
/*	public double getCurrentPrice() {
		return askPrice;
	}
	public void setCurrentPrice(double currentPrice) {
		this.askPrice = currentPrice;
	}*/
	
	
	
	
	@Override
	public String toString() {
		return "Security [companyName=" + companyName + ", symbol=" + symbol
				+ ", askPrice=" + askPrice + ", change=" + change + "]";
	}


	public String getDaysLow() {
		return daysLow;
	}

	public void setDaysLow(String daysLow) {
		this.daysLow = daysLow;
	}

	public String getDaysHigh() {
		return daysHigh;
	}

	public void setDaysHigh(String daysHigh) {
		this.daysHigh = daysHigh;
	}

	public String getYearLow() {
		return yearLow;
	}

	public void setYearLow(String yearLow) {
		this.yearLow = yearLow;
	}

	public String getYearHigh() {
		return yearHigh;
	}

	public void setYearHigh(String yearHigh) {
		this.yearHigh = yearHigh;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getPeRatio() {
		return peRatio;
	}

	public void setPeRatio(String peRatio) {
		this.peRatio = peRatio;
	}

	
	

	
	
	

	
	
	

}
