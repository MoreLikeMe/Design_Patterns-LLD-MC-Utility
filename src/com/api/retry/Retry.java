package com.api.retry;

import com.api.exception.NonRetryableException;
import com.api.exception.RetryableException;

import java.util.concurrent.Callable;

public class Retry {
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 1000;

    public <T> T executeWithRetry(Callable<T> task) throws Exception {
        int currentRetryCount = 0;
        while (true) {
            try {
                return task.call();
            } catch (Exception e) {
                if(e instanceof RetryableException){
                    currentRetryCount++;
                    if (isRetryLimitExceeded(currentRetryCount)) {
                        throw e;
                    }
                    Thread.sleep(RETRY_DELAY_MS);
                    System.out.println("Retrying... Attempt " + currentRetryCount);
                }
                else if(e instanceof NonRetryableException){
                    throw e;
                }
                else{
                    throw e;
                }
            }
        }
    }

    private boolean isRetryLimitExceeded(int currentRetryCount) {
        return currentRetryCount > MAX_RETRIES;
    }
}
