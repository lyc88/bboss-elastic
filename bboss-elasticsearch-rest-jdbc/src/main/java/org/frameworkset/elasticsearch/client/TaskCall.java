package org.frameworkset.elasticsearch.client;
/**
 * Copyright 2008 biaoping.yin
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Description: </p>
 * <p></p>
 * <p>Copyright (c) 2018</p>
 * @Date 2018/8/29 21:27
 * @author biaoping.yin
 * @version 1.0
 */
public class TaskCall implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(TaskCall.class);
	private String refreshOption;
	private ClientInterface clientInterface;
	private String datas;
	private ErrorWrapper errorWrapper;
	private int taskNo;
	public TaskCall(String refreshOption ,String datas,ErrorWrapper errorWrapper,int taskNo){
		this.refreshOption = refreshOption;
		this.clientInterface = errorWrapper.getClientInterface();
		this.datas = datas;
		this.errorWrapper = errorWrapper;
		this.taskNo = taskNo;
	}


	@Override
	public void run()   {
		if(!errorWrapper.assertCondition()) {
			if(logger.isWarnEnabled())
				logger.warn(new StringBuilder().append("Task[").append(this.taskNo).append("] Assert Execute Condition Failed, Ignore").toString());
			return;
		}
		try {
			if (refreshOption == null)
				clientInterface.executeHttp("_bulk", datas, ClientUtil.HTTP_POST);
			else
				clientInterface.executeHttp("_bulk?" + refreshOption, datas, ClientUtil.HTTP_POST);
		}
		catch (Exception e){
			errorWrapper.setError(e);
			throw new TaskFailedException(new StringBuilder().append("Task[").append(this.taskNo).append("] Assert Execute Failed").toString(),e);
		}

	}
}