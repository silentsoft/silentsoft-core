package org.silentsoft.core.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.silentsoft.core.CommonConst;
import org.silentsoft.core.exception.ActionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionUtil {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ActionUtil.class);
	
	private static final int MAX_THREAD_POOL = 5;
	
	private static <T> boolean isValidAction(Runnable action) {
		boolean result = true;
		
		if (action == null) {
			result = false;
		}
		
		return result;
	}
	
	private static <T> boolean isNotValidAction(Runnable action) {
		return !isValidAction(action);
	}
	
	public static <T> void doAction(Runnable action) throws ActionException, ExecutionException {
		if (isNotValidAction(action)) {
			throw new ActionException("Action is not valid !");
		}
		
		ExecutorService executorService = null;
		
		try {
			executorService = Executors.newFixedThreadPool(MAX_THREAD_POOL);
			executorService.submit(action).get();
		} catch (ExecutionException e) {
			throw new ExecutionException(e);
		} catch (Exception e) {
			throw new ActionException(e);
		} finally {
			if (executorService != null) {
				executorService.shutdown();
			}
		}
	}
	
	public static <T> boolean doAction(Runnable action, int retryCount) throws ActionException {
		if (retryCount < CommonConst.SIZE_EMPTY) {
			throw new ActionException("Retry count cannot be negative number !");
		}
		
		boolean result = true;
		
		int errorCount = 0;
		boolean workAction = true;
		while(workAction) {
			boolean errorOccur = false;
			
			try {
				doAction(action);
			} catch (ExecutionException e) {
				errorOccur = true;
				errorCount++;
				
				if (errorCount > retryCount) {
					result = false;
					workAction = false;
					
					LOGGER.error("Unfortunately, I got catch an error during action with retry for <{}> times.", new Object[]{ retryCount, e });
				}
			}
			
			if (!errorOccur) {
				result = true;
				workAction = false;
			}
		}
		
		return result;
	}
}
