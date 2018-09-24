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

package calypte.server;

public interface ServerConstants {

    public static final String BACKLOG				= "backlog";
    
    public static final String ADDRESS				= "address";
	
    public static final String PORT 				= "port";
    
    public static final String MAX_CONNECTIONS 		= "max_connections";
    
    public static final String TIMEOUT_CONNECTION 	= "timeout_connection";
    
    public static final String REUSE_ADDRESS 		= "reuse_address";
    
    public static final String WRITE_BUFFER_SIZE 	= "write_buffer_size";
    
    public static final String READ_BUFFER_SIZE 	= "read_buffer_size";
    
    public static final String AUTO_COMMIT 			= "auto_commit";

    public static final String TRANSACTION_SUPPORT	= "transaction_support";

    public static final String TRANSACTION_TIMEOUT	= "transaction_timeout";
    
    public static final String TRANSACTION_MANAGER	= "transaction_manager";

}
