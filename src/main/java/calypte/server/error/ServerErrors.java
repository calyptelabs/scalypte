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
import calypte.CacheErrors;

public class ServerErrors extends CacheErrors {

	public static final CacheError ERROR_1001 = new CacheError(1001, "The command %s not recognized!");
	
	public static final CacheError ERROR_1002 = new CacheError(1002, "Command not informed!");

	public static final CacheError ERROR_1003 = new CacheError(1003, "%s is invalid!");

	public static final CacheError ERROR_1004 = new CacheError(1004, "Bad command syntax error!");

	public static final CacheError ERROR_1005 = new CacheError(1005, "internal error!");

	public static final CacheError ERROR_1006 = new CacheError(1006, "Unknow error!");

	public static final CacheError ERROR_1009 = new CacheError(1009, "Transaction not supported!");
	
}
