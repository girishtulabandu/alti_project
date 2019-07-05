package com.alti.data;

public interface DbInterface {
	
	public  void sendToDb(String inoviceNum, String inDate, String cPo, String addressTo, String total);
	public void displayDb();
	

}
