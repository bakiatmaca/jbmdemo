package com.bakiatmaca.research.jbm.demoapp.workers;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Watcher implements Runnable {

	protected static final int UPPERBOUND = 9999;

	protected static final Logger log = Logger.getLogger(Watcher.class.getName());

	protected static Random rand;

	protected String name;

	static {
		rand = new Random();
	}

	public Watcher(String name) {
		this.name = name;
		log.log(Level.INFO, String.format("Watcher [%s] has been created" , name));
	}

	public void run() {
		while (true) {
			int val = rand.nextInt(UPPERBOUND);

			log.log(Level.INFO, String.format("Watcher [%s] changed file count:%s", name, val));

			try {
				Thread.sleep(1500 + (val / 5));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}