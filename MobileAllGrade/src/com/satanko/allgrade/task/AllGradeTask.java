/************************************************************
 *  Copyright (c) 2013 Philipp Sommersguter, Julian Tropper *
 *                                                          *
 *  See the file LICENCE.txt for copying permission.        *
 ***********************************************************/
package com.satanko.allgrade.task;

/**
 * A framework for a task.
 * 
 * @param <Params>
 * @author Philipp Sommersguter (philipp.sommersguter@satanko.at)<p>
 * ATTENTION: If not otherwise specified, the author(s) of the class is/are also responsible for all methods.
 * @version 1.0
 */
public abstract class AllGradeTask<Params> {
	
	public AllGradeTask()
	{
		
	}
	
	public void preExecute() throws TaskException {}
	
	abstract void doWork(Params... params) throws TaskException;
	
	/**
	 * Calls <code>preExecute()</code> and <code>exedoWork()</code> automatically.
	 * 
	 * @return AllGradeTask
	 * @throws TaskException 
	 */
	public void execute(Params... params) throws TaskException{
		this.preExecute();
		this.doWork(params);
		
	}
	
}
