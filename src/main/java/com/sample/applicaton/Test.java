package com.sample.applicaton;

import java.time.LocalDateTime;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime before = LocalDateTime.now().minusHours(4).minusMinutes(30);
		System.out.println(now.getHour()-before.getHour());
		System.out.println(now.getMinute()-before.getMinute());
	}

}
