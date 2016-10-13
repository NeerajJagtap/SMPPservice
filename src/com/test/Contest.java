package com.test;

import java.io.*;
import java.util.*;

/* Do not declare this class `public` */
/* Do not add a namespace declaration */
class Contest {

	private ArrayList<ArrayList<String>> input_data;
	ArrayList<PairOfCities> listofPairOfCities = new ArrayList<PairOfCities>();
	
	public static void main(String[] args) {
		Contest c = new Contest();
		/*
		 * ArrayList<PairOfCities> pairOfCities = new ArrayList<PairOfCities>();
		 * 
		 * PairOfCities p1 = new PairOfCities("Pune", "Bombay"); PairOfCities p2
		 * = new PairOfCities("Pune", "Delhi"); PairOfCities p3 = new
		 * PairOfCities("Bangalore", "Bombay"); PairOfCities p4 = new
		 * PairOfCities("Bombay", "Bangalore"); PairOfCities p5 = new
		 * PairOfCities("Bombay", "Delhi"); PairOfCities p6 = new
		 * PairOfCities("Bangalore", "Pune"); PairOfCities p7 = new
		 * PairOfCities("Bangalore", "Nashik");
		 */

		String s1 = "Pune,Bombay";
		String s2 = "Pune,Delhi";
		String s3 = "Bangalore,Bombay";
		String s4 = "Bombay,Bangalore";
		String s5 = "Bombay,Delhi";
		String s6 = "Bangalore,Pune";
		String s7 = "Bangalore,Nashik";
		ArrayList<String> pairOfCities = new ArrayList<String>();
		pairOfCities.add(s1);
		pairOfCities.add(s2);
		pairOfCities.add(s3);
		pairOfCities.add(s4);
		pairOfCities.add(s5);
		pairOfCities.add(s6);
		pairOfCities.add(s7);

		ArrayList<ArrayList<String>> input_data = new ArrayList<ArrayList<String>>();
		input_data.add(pairOfCities);

		c.initialize(input_data);
		c.process("Pune", "Nashik");
	}

	void initialize(ArrayList<ArrayList<String>> input_data) {
		this.input_data = input_data;
		
		int i = 0;
		if (input_data != null) {
			for (ArrayList<String> second : input_data) {
				for (String first : second) {
					String[] city = first.split(",");
					PairOfCities e = new PairOfCities(city[0], city[1]);
					listofPairOfCities.add(e);
					System.out.println(" list of pair 1 : " + listofPairOfCities.toString());
					
					i++;
				}
			}
		}
	}

	int process(String s1, String s2) {
		
		ArrayList<Integer> pathcount = new ArrayList<Integer>(); 
		HashMap<String,Integer> path = new HashMap<String,Integer>(); 
		
		String source = s1;
		String destination = s2;
		int i=0;
		
		if(source != null && destination != null){
			
			for (PairOfCities c : listofPairOfCities)
			{
				int count = 0;
				if(source.equalsIgnoreCase(c.c1)){
					count =count +1;
					path.put("p"+i, count);
					i++;
					
					System.out.println(" count "+ count);
					
					if(destination.equalsIgnoreCase(c.c2)){
						for (PairOfCities b : listofPairOfCities)
						{
							if(source.equalsIgnoreCase(b.c1)){
								
							}
						}
						
					}
					
					
				}
			}
			
		}
		
		
		return this.input_data.size();
	}
	
	
	HashMap<String,Integer> possibility(String s1, String s2) {
		HashMap<String,Integer> path = null; 
		int i=0;
		
		for (PairOfCities c : listofPairOfCities)
		{
			int count = 0;
			if(s1.equalsIgnoreCase(c.c1)){
				count =count +1;
				path.put("p"+i, count);
				i++;
				
				System.out.println(" count "+ count);
			}
		}
		
		
		 return path;
	 }
	
	

	class PairOfCities {
		String c1, c2;

		PairOfCities(String c1, String c2) {
			this.c1 = c1;
			this.c2 = c2;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "PairOfCities [c1=" + c1 + ", c2=" + c2 + "]";
		}
	}
}
