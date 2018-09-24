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
 * Lançada caso ocorra algum falha ao tentar processar os parâmetros 
 * de uma solicitação.
 * 
 * @author Brandao
 */
public class ParameterException extends Exception{
    
	private static final long serialVersionUID = 9178738328569730473L;

	public ParameterException() {
        super();
    }

    public ParameterException(String string) {
        super(string);
    }

    public ParameterException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public ParameterException(Throwable thrwbl) {
        super(thrwbl);
    }

}
