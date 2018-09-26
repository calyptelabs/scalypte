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

package br.com.uoutec.installer.action;

import br.com.uoutec.installer.entity.AbstractInstallerAction;
import calypte.installer.action.entity.ServerInstance;

/**
 *
 * @author Ribeiro
 */
public class ServerInstanceInstallerAction 
        extends AbstractInstallerAction{
    
    private ServerInstance serverInstance;

    public ServerInstance getServerInstance() {
        return serverInstance;
    }

    public void setServerInstance(ServerInstance serverInstance) {
        this.serverInstance = serverInstance;
    }
    
}
