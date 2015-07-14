package org.itrgroup.itr.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleExecutorService {
	
	
	private static ExecutorService pool = null;
	private static int threadCount = Runtime.getRuntime().availableProcessors() * 2;
	
	public static ExecutorService getInstance(){
		if(pool == null) {
			pool = Executors.newCachedThreadPool();
		}
		return pool;
	}
}
