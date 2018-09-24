/*
 * Calypte http://calypte.uoutec.com.br/
 * Copyright (C) 2018 UoUTec. (calypte@uoutec.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calypte.server.error;

import calypte.CacheError;
import calypte.CacheException;

public class ServerErrorException 
	extends CacheException{

	private static final long serialVersionUID = 8545856451576836405L;

	public ServerErrorException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(CacheError error, Object... params) {
		super(error, params);
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public ServerErrorException(Throwable thrwbl, CacheError error,
			Object... params) {
		super(thrwbl, error, params);
		// TODO Auto-generated constructor stub
	}


}
