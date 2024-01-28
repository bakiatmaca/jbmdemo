package com.bakiatmaca.research.jbm.demoapp.workers;

import java.util.Random;

public class ContentUpdater implements Runnable {

	protected static final int UPPERBOUND = 9999;

	protected static Random rand;

	protected String name;

	static {
		rand = new Random();
	}

	public ContentUpdater(String name) {
		this.name = name;
		System.out.println(String.format("ContentUpdater [%s] has been created" , name));
	}

	public void run() {
		while (true) {
			int val = rand.nextInt(UPPERBOUND);

			System.out.println(String.format("ContentUpdater [%s]. %s contents were updated ", name, val));
			
			try {
				Thread.sleep(900 + (val / 5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}