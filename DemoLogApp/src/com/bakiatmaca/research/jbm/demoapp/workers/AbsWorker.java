package com.bakiatmaca.research.jbm.demoapp.workers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AbsWorker implements Runnable {

	protected static final int UPPERBOUND = 9999;

	protected static final Logger log = Logger.getLogger(AbsWorker.class.getName());

	protected static Random rand;

	protected String name;

	static {
		rand = new Random();
	}

	public AbsWorker(String name) {
		this.name = name;
		log.log(Level.INFO, String.format("Worker [%s] has been created" , name));
	}

	public void run() {
		while (true) {
			int val = rand.nextInt(UPPERBOUND);

			log.log(Level.INFO, String.format("Worker [%s] current worker count:%s", name, val));

			try {
				Thread.sleep(1000 + (val / 10));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
