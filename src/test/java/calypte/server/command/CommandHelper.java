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

package calypte.server.command;

public class CommandHelper {

	/* PUT commands */
	
	public static final String[] put_keyN_valueN_not_exist = new String[] {
			">>put key 0 0 5 0",
			">>teste",
			"<<stored"
		};
	
	public static final String[] put_keyN_valueN_exist = new String[] {
			">>put key 0 0 5 0",
			">>teste",
			"<<replaced"
		};

	public static final String[] put_keyN_value1_not_exist = new String[] {
			">>put key 0 0 1 0",
			">>a",
			"<<stored"
		};
		
	public static final String[] put_keyN_value1_exist = new String[] {
			">>put key 0 0 1 0",
			">>a",
			"<<replaced"
		};

	public static final String[] put_key1_valueN_not_exist = new String[] {
			">>put k 0 0 5 0",
			">>teste",
			"<<stored"
		};
	
	public static final String[] put_key1_valueN_exist = new String[] {
			">>put k 0 0 5 0",
			">>teste",
			"<<replaced"
		};
	
	public static final String[] put_key1_value1_not_exist = new String[] {
			">>put k 0 0 1 0",
			">>a",
			"<<stored"
		};
		
	public static final String[] put_key1_value1_exist = new String[] {
			">>put k 0 0 1 0",
			">>a",
			"<<replaced"
		};

	/* REPLACE commands */

	public static final String[] replace_keyN_valueN_not_exist = new String[] {
			">>replace key 0 0 5 0",
			">>teste",
			"<<not_stored"
		};
	
	public static final String[] replace_keyN_valueN_exist = new String[] {
			">>replace key 0 0 5 0",
			">>teste",
			"<<replaced"
		};

	public static final String[] replace_keyN_value1_not_exist = new String[] {
			">>replace key 0 0 1 0",
			">>a",
			"<<not_stored"
		};
		
	public static final String[] replace_keyN_value1_exist = new String[] {
			">>replace key 0 0 1 0",
			">>a",
			"<<replaced"
		};

	public static final String[] replace_key1_valueN_not_exist = new String[] {
			">>replace k 0 0 5 0",
			">>teste",
			"<<not_stored"
		};
	
	public static final String[] replace_key1_valueN_exist = new String[] {
			">>replace k 0 0 5 0",
			">>teste",
			"<<replaced"
		};
	
	public static final String[] replace_key1_value1_not_exist = new String[] {
			">>replace k 0 0 1 0",
			">>a",
			"<<not_stored"
		};
		
	public static final String[] replace_key1_value1_exist = new String[] {
			">>replace k 0 0 1 0",
			">>a",
			"<<replaced"
		};	

	/* SET commands */

	public static final String[] set_keyN_valueN_not_exist = new String[] {
			">>set key 0 0 5 0",
			">>teste",
			"<<stored"
		};
	
	public static final String[] set_keyN_valueN_exist = new String[] {
			">>set key 0 0 5 0",
			">>teste",
			"<<not_stored"
		};

	public static final String[] set_keyN_value1_not_exist = new String[] {
			">>set key 0 0 1 0",
			">>a",
			"<<stored"
		};
		
	public static final String[] set_keyN_value1_exist = new String[] {
			">>set key 0 0 1 0",
			">>a",
			"<<not_stored"
		};

	public static final String[] set_key1_valueN_not_exist = new String[] {
			">>set k 0 0 5 0",
			">>teste",
			"<<stored"
		};
	
	public static final String[] set_key1_valueN_exist = new String[] {
			">>set k 0 0 5 0",
			">>teste",
			"<<not_stored"
		};
	
	public static final String[] set_key1_value1_not_exist = new String[] {
			">>set k 0 0 1 0",
			">>a",
			"<<stored"
		};
		
	public static final String[] set_key1_value1_exist = new String[] {
			">>set k 0 0 1 0",
			">>a",
			"<<not_stored"
		};	

	/* GET commands */

	public static final String[] get_keyN_valueN_not_exist = new String[] {
			">>get key 0",
			"<<value key 0 0",
			"<<end"
		};
	
	public static final String[] get_keyN_valueN_exist = new String[] {
			">>get key 0",
			"<<value key 5 0",
			"<<teste",
			"<<end",
		};

	public static final String[] get_keyN_value1_not_exist = new String[] {
			">>get key 0",
			"<<value key 0 0",
			"<<end"
		};
		
	public static final String[] get_keyN_value1_exist = new String[] {
			">>get key 0",
			"<<value key 1 0",
			"<<a",
			"<<end"
		};

	public static final String[] get_key1_valueN_not_exist = new String[] {
			">>get k 0",
			"<<value k 0 0",
			"<<end"
		};
	
	public static final String[] get_key1_valueN_exist = new String[] {
			">>get k 0",
			"<<value k 5 0",
			"<<teste",
			"<<end"
		};
	
	public static final String[] get_key1_value1_not_exist = new String[] {
			">>get k 0",
			"<<value k 0 0",
			"<<end"
		};
		
	public static final String[] get_key1_value1_exist = new String[] {
			">>get k 0",
			"<<value k 1 0",
			"<<a",
			"<<end"
		};	

	/* REMOVE commands */

	public static final String[] remove_keyN_valueN_not_exist = new String[] {
			">>remove key",
			"<<not_found"
		};
	
	public static final String[] remove_keyN_valueN_exist = new String[] {
			">>remove key",
			"<<ok"
		};

	public static final String[] remove_keyN_value1_not_exist = new String[] {
			">>remove key",
			"<<not_found"
		};
		
	public static final String[] remove_keyN_value1_exist = new String[] {
			">>remove key",
			"<<ok"
		};

	public static final String[] remove_key1_valueN_not_exist = new String[] {
			">>remove k",
			"<<not_found"
		};
	
	public static final String[] remove_key1_valueN_exist = new String[] {
			">>remove k",
			"<<ok"
		};
	
	public static final String[] remove_key1_value1_not_exist = new String[] {
			">>remove k",
			"<<not_found"
		};
		
	public static final String[] remove_key1_value1_exist = new String[] {
			">>remove k",
			"<<ok"
		};	

	/* begin */
	
	public static final String[] begin = new String[] {
			">>begin",
			"<<ok"
		};
	
	/* commit */
	
	public static final String[] commit = new String[] {
			">>commit",
			"<<ok"
		};

	/* rollback */
	
	public static final String[] rollback = new String[] {
			">>rollback",
			"<<ok"
		};

	/* flush */
	
	public static final String[] flush = new String[] {
			">>flush",
			"<<ok"
		};
	
}
