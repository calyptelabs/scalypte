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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TerminalVars 
	extends HashMap<String, Object>{

	private static final long serialVersionUID = 3659583990707468630L;
	
	private TerminalVars parent;
	
	private Map<String, TerminalInfoListener> listeners;
	
	public TerminalVars(){
		this(null, null);
	}

	public TerminalVars(TerminalVars parent, Map<String, Object> defaultValues){
		this.parent    = parent;
		this.listeners = new HashMap<String, TerminalVars.TerminalInfoListener>();

		if(defaultValues != null){
			this.putAll(defaultValues);
		}
	}
	
	public void setListener(String key, TerminalInfoListener value){
		this.listeners.put(key, value);
	}

	public void removeListener(String key){
		this.listeners.remove(key);
	}
	
	@Override
	public synchronized Object put(String key, Object value) {
		
		Object old = super.put(key, value);
		TerminalInfoListener listener = this.listeners.get(key);
		
		if(listener != null){
			listener.actionPerformed(key, old, value, this);
		}
		
		return old;
	}

	public Object set(String key, Object value) {
		return super.put(key, value);
	}
	
	@Override
	public Object get(Object key) {
		Object v = super.get(key);
		return v == null && this.parent != null? this.parent.get(key) : v; 
	}

	@Override
	public boolean containsKey(Object key) {
		boolean k = super.containsKey(key);
		return !k && this.parent != null? this.parent.containsKey(key) : k;
	}

	@Override
	public Set<String> keySet() {
		Set<String> set = new HashSet<String>();
		
		if(this.parent != null){
			set.addAll(this.parent.keySet());
		}
		
		set.addAll(super.keySet());
		
		return set;
	}

	@Override
	public boolean containsValue(Object value) {
		boolean k = super.containsValue(value);
		return !k && this.parent != null? this.parent.containsValue(value) : k;
	}

	public static interface TerminalInfoListener{
		
		void actionPerformed(String key, Object oldValue, Object newValue, TerminalVars terminalInfo);
		
	}
}
