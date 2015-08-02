package com.example.pojo;

/*
 * Author: Alexander Pinkerton, Udeep Manchanda, Tianyi Xie
 */


public class StockAlert {

	String oldPrice, targetPrice, stockSymbol,stockState;
	
	public StockAlert() {
		super();
		
	}
	

	public StockAlert(String oldPrice, String targetPrice, String stockSymbol,String stockState) {
		super();
		this.oldPrice = oldPrice;
		this.targetPrice = targetPrice;
		this.stockSymbol = stockSymbol;
		this.stockState =  stockState;
	}

	public String getStockState() {
		return stockState;
	}

	public void setStockState(String stockState) {
		this.stockState = stockState;
	}

	public String getOldPrice() {
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		this.oldPrice = oldPrice;
	}

	public String getTargetPrice() {
		return targetPrice;
	}

	public void setTargetPrice(String targetPrice) {
		this.targetPrice = targetPrice;
	}

	public String getStockSymbol() {
		return stockSymbol;
	}

	public void setStockSymbol(String stockSymbol) {
		this.stockSymbol = stockSymbol;
	}
	
	
	
}
