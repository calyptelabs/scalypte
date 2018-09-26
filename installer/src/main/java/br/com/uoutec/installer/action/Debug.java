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

import br.com.uoutec.installer.Main;

/**
 *
 * @author Ribeiro
 */
public class Debug {

    public static void main(String[] args) throws Throwable{
        
        System.setProperty("CreateInstallerJarFile",    "./target/scalypte-installer-1.0.0.0-jar-with-dependencies.jar");
        System.setProperty("ExecuteInstallerJarFile",   "C:\\projetos\\calypte\\scalypte\\installer\\installer.jar");
        System.setProperty("ExecuteUninstallerJarFile", "C:/Program Files (x86)/uoutec/uninstaller.jar");
        System.setProperty("debugType", "createinstaller");
        
        Main.setDebug(true);
        //Main.setDebugType(System.getProperty("debugType"));
        //Main.setDebugType("installer");
        Main.main(new String[]{
            "--installer-config=C:/projetos/calypte/scalypte/installer/installer.properties"}
        );
        
    }
    
}
