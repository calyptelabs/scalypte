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

/**
 *
 * @author Brandao
 */
public class TerminalConstants {

    public static final String CRLF 						= "\r\n";
    
	public static final String SEPARATOR_COMMAND 			= " ";
	
    public static final String BOUNDARY 					= "end";
    
    public static final String UNKNOW_COMMAND 				= "unknow commmand: %s";
    
    public static final String INVALID_NUMBER_OF_PARAMETERS = "invalid number of parameters";
    
    public static final String INVALID_TIME 				= "invalid exptime";

    public static final String INVALID_SIZE 				= "invalid size";
    
    public static final String SUCCESS 						= "ok";

    public static final String STORED 						= "stored";

    public static final String NOT_STORED 					= "not_stored";
    
    public static final String REPLACE_SUCCESS 				= "replaced";
    
    public static final String NOT_FOUND 					= "not_found";
    
    public static final String INSERT_ENTRY_FAIL 			= "insert entry fail";
    
    public static final String READ_ENTRY_FAIL 				= "read entry fail";
    
    public static final String SEND_MESSAGE_FAIL  			= "send message fail";
    
    public static final String STREAM_CLOSED 				= "stream closed";
    
    public static final String CANT_READ_COMMAND 			= "cant read command: %s";
    
    public static final String EMPTY_PARAMETER 				= "empty parameter: %s";

    public static final String CANT_READ_PARAMETER 			= "cant read parameter: %s";
    
    public static final String DISCONNECT 					= "goodbye!";

    public static final byte[] DISCONNECT_DTA				= "goodbye!".getBytes();
    
    public static final byte[] SUCCESS_DTA					= "ok".getBytes();
    
    public static final byte[] STORED_DTA 					= "stored".getBytes();
    
    public static final byte[] FULL_STORED_DTA				= "stored\r\n".getBytes();

    public static final byte[] NOT_STORED_DTA				= "not_stored".getBytes();
    
    public static final byte[] REPLACE_SUCCESS_DTA			= "replaced".getBytes();

    public static final byte[] FULL_REPLACE_SUCCESS_DTA		= "replaced\r\n".getBytes();
    
    public static final byte[] NOT_FOUND_DTA 				= "not_found".getBytes();
    
   	public static final byte[] PUT_CMD_DTA 					= "put".getBytes();
   	
	public static final byte[] GET_CMD_DTA 					= "get".getBytes();
	
	public static final byte[] REPLACE_CMD_DTA 				= "replace".getBytes();
	
	public static final byte[] SET_CMD_DTA 					= "set".getBytes();
	
	public static final byte[] REMOVE_CMD_DTA 				= "remove".getBytes();
	
	public static final byte[] BEGIN_CMD_DTA 				= "begin".getBytes();
	
	public static final byte[] COMMIT_CMD_DTA 				= "commit".getBytes();
	
	public static final byte[] ROLLBACK_CMD_DTA 			= "rollback".getBytes();
	
	public static final byte[] SHOW_VAR_CMD_DTA 			= "show_var".getBytes();
	
	public static final byte[] SET_VAR_CMD_DTA 				= "set_var".getBytes();
	
	public static final byte[] SHOW_VARS_CMD_DTA 			= "show_vars".getBytes();
	
	public static final byte[] EXIT_CMD_DTA 				= "exit".getBytes();
	
	public static final byte[] SEPARATOR_COMMAND_DTA 		= new byte[]{' '};
	
	public static final String[] EMPTY_STR_ARRAY 			= new String[]{};
	
    public static final byte[] CRLF_DTA 					= "\r\n".getBytes();
    
    public static final byte[] BOUNDARY_DTA 				= BOUNDARY.getBytes();

    public static final byte[] FULL_BOUNDARY_DTA 			= (BOUNDARY + CRLF) .getBytes();
    
	public static final byte SEPARATOR_CHAR 				= ' ';
    
}
