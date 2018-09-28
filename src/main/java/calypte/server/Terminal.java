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

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import calypte.Cache;
import calypte.tx.CacheTransaction;
import calypte.tx.CacheTransactionManager;
import calypte.tx.TXCache;
import calypte.server.command.BeginTransactionCommand;
import calypte.server.command.CommitTransactionCommand;
import calypte.server.command.ExitCommand;
import calypte.server.command.FlushCommand;
import calypte.server.command.GetCommand;
import calypte.server.command.PutCommand;
import calypte.server.command.RemoveCommand;
import calypte.server.command.ReplaceCommand;
import calypte.server.command.RollbackTransactionCommand;
import calypte.server.command.SetCommand;
import calypte.server.command.SetVarCommand;
import calypte.server.command.ShowVarCommand;
import calypte.server.command.ShowVarsCommand;
import calypte.server.error.ServerErrorException;
import calypte.server.error.ServerErrors;
import calypte.server.io.StreamFactory;
import calypte.server.util.ArraysUtil;

/**
 *
 * @author Ribeiro
 */
@SuppressWarnings("unused")
public class Terminal {
    
	public static final Command PUT    		= new PutCommand();

	public static final Command SET    		= new SetCommand();
	
	public static final Command REPLACE   	= new ReplaceCommand();
	
	public static final Command GET    		= new GetCommand();

	public static final Command BEGIN_TX  	= new BeginTransactionCommand();
	
	public static final Command COMMIT_TX	= new CommitTransactionCommand();

	public static final Command ROLLBACK_TX	= new RollbackTransactionCommand();
	
	public static final Command REMOVE 		= new RemoveCommand();

	public static final Command SHOW_VARS	= new ShowVarsCommand();

	public static final Command SET_VAR		= new SetVarCommand();

	public static final Command SHOW_VAR	= new ShowVarCommand();
	
	public static final Command EXIT   		= new ExitCommand();
	
	public static final Command FLUSH  		= new FlushCommand();
	
	public static final byte SEPARATOR_CHAR = TerminalConstants.SEPARATOR_CHAR;
	
    private Cache cache;
    
    private Socket socket;
    
    private boolean run;
    
    private TerminalReader reader;
    
    private TerminalWriter writer;
    
    private TerminalVars terminalVars;
    
    private int readBufferSize;
    
	private int writeBufferSize;
    
    public Terminal(){
        this.run    = false;
    }

    protected void init(Socket socket, Cache cache, 
            StreamFactory streamFactory,
            int readBufferSize, int writeBufferSize, TerminalVars terminalVars) throws IOException{
        try{
            this.socket          = socket;
            this.cache           = cache;
            this.readBufferSize  = readBufferSize;
            this.writeBufferSize = writeBufferSize;
            this.reader          = new TextTerminalReader(this.socket, streamFactory, readBufferSize);
            this.writer          = new TextTerminalWriter(this.socket, streamFactory, writeBufferSize);
            this.run             = true;
            this.terminalVars    = terminalVars;
        }
        catch(Throwable e){
            if(this.socket != null)
                this.socket.close();
        }
        finally{
            if(this.socket == null || this.socket.isClosed())
                this.run = false;
        }
    }
    
    public TerminalVars getTerminalVars() {
		return this.terminalVars;
	}

    private void closeTransaction(){
		try{
			if(this.cache instanceof TXCache){
				TXCache txCache = (TXCache)this.cache;
				CacheTransactionManager txManager = txCache.getTransactionManager();
				CacheTransaction currentTx = txManager.getCurrrent(false);
				if(currentTx != null){
					currentTx.rollback();
				}
			}
		}
		catch(Throwable e){
			e.printStackTrace();
		}
    }
    
    private void closeConnection(){
        try{
            if(this.socket != null)
                this.socket.close();
		}
		catch(Throwable e){
			e.printStackTrace();
		}
        finally{
            this.run    = false;
            this.cache  = null;
            this.reader = null;
            this.writer = null;
        }
    }
    
	public void destroy() throws IOException{
		this.closeTransaction();
		this.closeConnection();
    }
    
    public void execute() throws Throwable{
    	byte[] message    = new byte[cache.getConfig().getMaxSizeKey() + 30];
    	byte[] cmdBuffer  = new byte[256];
        Parameters params = new Parameters();
        params.setSeparator(SEPARATOR_CHAR);
        int readMessage   = -1;
        int lenRead       = 0;
        int cmdBufferLen  = cmdBuffer.length;
        int messageLen    = message.length;
        
        params.setData(message, 0);
        
        while(run){
            try{
            	//marca com 0 o primeiro byte do buffer. Se não for lido nenhum byte, cai no default do switch
            	message[0] = 0;
            	
            	//reinicia o deslocamento do buffer
                params.reset();
                
                //lê o cabeçalho do comando
                readMessage = reader.readMessage(message, 0, messageLen);
                
                //Define a quantidade de bytes que representam os parâmetros.
                params.setLen(readMessage);
                
                //lê o nome do comando
                lenRead     = params.readNext(cmdBuffer, 0, readMessage > cmdBufferLen? cmdBufferLen : readMessage);
                
                switch (message[0]) {
				case 'g':
					
	               	if(ArraysUtil.equals(TerminalConstants.GET_CMD_DTA, message, 0, lenRead)){
	        			GET.execute(this, cache, reader, writer, params);
	            	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 'p':
	               	if(ArraysUtil.equals(TerminalConstants.PUT_CMD_DTA, message, 0, lenRead)){
	            		PUT.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
					break;
				case 'r':
					
	               	if(ArraysUtil.equals(TerminalConstants.REPLACE_CMD_DTA, message, 0, lenRead)){
	        			REPLACE.execute(this, cache, reader, writer, params);
	               	}
	               	else
	               	if(ArraysUtil.equals(TerminalConstants.REMOVE_CMD_DTA, message, 0, lenRead)){
	        			REMOVE.execute(this, cache, reader, writer, params);
	               	}
	               	else
	               	if(ArraysUtil.equals(TerminalConstants.ROLLBACK_CMD_DTA, message, 0, lenRead)){
	        			ROLLBACK_TX.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 's':
					
	               	if(ArraysUtil.equals(TerminalConstants.SET_CMD_DTA, message, 0, lenRead)){
	        			SET.execute(this, cache, reader, writer, params);
	               	}
	               	else
	               	if(ArraysUtil.equals(TerminalConstants.SHOW_VAR_CMD_DTA, message, 0, lenRead)){
	        			SHOW_VAR.execute(this, cache, reader, writer, params);
	               	}
	               	else
	               	if(ArraysUtil.equals(TerminalConstants.SET_VAR_CMD_DTA, message, 0, lenRead)){
	        			SET_VAR.execute(this, cache, reader, writer, params);
	               	}
	               	else
	               	if(ArraysUtil.equals(TerminalConstants.SHOW_VARS_CMD_DTA, message, 0, lenRead)){
	        			SHOW_VARS.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 'b':
					
	               	if(ArraysUtil.equals(TerminalConstants.BEGIN_CMD_DTA, message, 0, lenRead)){
	        			BEGIN_TX.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 'c':
	               	if(ArraysUtil.equals(TerminalConstants.COMMIT_CMD_DTA, message, 0, lenRead)){
	        			COMMIT_TX.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 'e':
	               	if(ArraysUtil.equals(TerminalConstants.EXIT_CMD_DTA, message, 0, lenRead)){
	        			EXIT.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				case 'f':
	               	if(ArraysUtil.equals(TerminalConstants.FLUSH_CMD_DTA, message, 0, lenRead)){
	        			FLUSH.execute(this, cache, reader, writer, params);
	               	}
	                else{
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
                    					ArraysUtil.toString(message, 0, lenRead)
            					)
	            		);
	                    writer.flush();
	                }
	               	
	               	break;
				default:
					if(readMessage < 0){
	               		//se readMessage for menor que zero, indica que que a conexão com o servidor foi 
						// encerrada pelo usuário e while deve ser também encerrado.
                   		this.run = false;
               		}
               		else{
               			//informa ao usuário que não existe o comando executado.
	                    writer.sendMessage(
	                    		ServerErrors.ERROR_1001.getString(
	                    				lenRead <= 0? "empty" : 
	                					ArraysUtil.toString(message, 0, lenRead)
	        					)
	            		);
	                    writer.flush();
               		}
               		
					break;
				}
            }
            catch (ArrayIndexOutOfBoundsException ex) {
            	ex.printStackTrace();
                writer.sendMessage(ServerErrors.ERROR_1002.getString());
                writer.flush();
            }
            catch (ServerErrorException ex) {
            	ex.printStackTrace();
            	if(ex.getCause() instanceof EOFException && !"premature end of data".equals(ex.getCause().getMessage()))
        			throw ex;
            	
                writer.sendMessage(ex.getMessage());
                writer.flush();
            }
            catch(Throwable ex){
            	ex.printStackTrace();
                throw ex;
            }
        }
    }

	public TerminalReader getReader() {
		return reader;
	}

	public TerminalWriter getWriter() {
		return writer;
	}

	public Cache getCache() {
		return cache;
	}

}
