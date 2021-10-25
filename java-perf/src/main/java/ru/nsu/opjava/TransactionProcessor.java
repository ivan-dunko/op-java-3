package ru.nsu.opjava;

/**
 * Hello world!
 *
 */
public class TransactionProcessor 
{
    public void processTransaction(int txNum) throws Exception{
		System.err.println("Processing tx: " + txNum);
		int sleep = (int) (Math.random() * 1000);
		Thread.sleep(sleep);
		System.err.println(String.format("tx: %d completed", txNum));
	}



	public static void main(String[] args) throws Exception{
		TransactionProcessor tp = new TransactionProcessor();
		for (int i = 0; i < 10; ++i) {
			tp.processTransaction(i);
		}
	}
}

/*
class Ext {

	public void processTransaction(int txNum) throws Exception{
		long st = System.currentTimeMillis();
		super.processTransaction(txNum);
		long et = System.currentTimeMillis() - st;
		//Aggregator.updateStats(et);
		// update  max,mint
	}
}
*/