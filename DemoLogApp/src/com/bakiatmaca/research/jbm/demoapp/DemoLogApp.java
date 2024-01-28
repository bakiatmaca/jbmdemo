package com.bakiatmaca.research.jbm.demoapp;

import com.bakiatmaca.research.jbm.demoapp.workers.HealthChecker;
import com.bakiatmaca.research.jbm.demoapp.workers.ContentUpdater;
import com.bakiatmaca.research.jbm.demoapp.workers.Watcher;
import com.bakiatmaca.research.jbm.demoapp.workers.WebAPIWorker;

public class DemoLogApp {

	private final static Object lock = new Object();

	public static void main(String[] args) {
		System.out.println("Demo LogApp starting...");

		createWorker(new WebAPIWorker("Web API"));
		createWorker(new HealthChecker("Health Checker"));

		createWorker(new Watcher("File"));

		createWorker(new ContentUpdater("RSS Updater"));

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				try {
					System.out.println("Shutting down");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		synchronized (lock) {
			try {
				lock.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static void createWorker(Runnable worker) {
		Thread t = new Thread(worker);
		t.start();
	}

}

