package org.silentsoft.core.parallel;

import java.util.ArrayList;

import org.junit.Test;

public class ParallelTest {

	@Test
	public void normal() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0, j=10000; i<j; i++) {
			list.add(i, i);
		}
		
		long startTime = System.currentTimeMillis();
		// speed test
		long mySum = 0;
		for (int i=0, j=list.size(); i<j; i++) {
			mySum += list.get(i);
		}
		long endTime = System.currentTimeMillis();
		
		System.out.println("normal sum is " + mySum + " and " + (endTime-startTime) + " ms");
	}
	
	@Test
	public void parallel() {
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i = 0, j=10000; i<j; i++) {
			list.add(i, i);
		}
		
		long startTime = System.currentTimeMillis();
		// speed test
		Parallel.For(list, (i) -> {
			sum(i);
		});
		long endTime = System.currentTimeMillis();
		
		System.out.println("parallel sum is " + totalSum + " and " + (endTime-startTime) + " ms");
	}
	
	long totalSum = 0;
	private void sum(int i) {
		totalSum += i;
	}
}
