package com.example.SchoolApp.dto;

public class DateTransferDto{
	private int day;
	private int month;
	private int year;
	DateTransferDto(int day, int month, int year){
		this.month = month;
		this.day = day;
		this.year = year;
	}
	public int getDay() {return day;}
	public int getMonth() {return month;}
	public int getYear() {return year;}
	
}