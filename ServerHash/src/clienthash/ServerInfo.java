/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthash;

import interfaces.IServerHash;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
class ServerInfo {
     private int _Port;
    private String _Name;
    private String _Ip;
    public ServerInfo(int Port, String ip, String Name){
        _Port = Port;     
        _Name = Name;
        _Ip = ip;
    }

    public int getPort() {
        return _Port;
    }

    public void setPort(int _Port) {
        this._Port = _Port;
    }

    public String getName() {
        return _Name;
    }

    public void setName(String _Name) {
        this._Name = _Name;
    }

    public String getIp() {
        return _Ip;
    }

    public void setIp(String _Ip) {
        this._Ip = _Ip;
    }
    
    private IServerHash StartServer() throws RemoteException, NotBoundException{
        Registry registry = LocateRegistry.getRegistry(_Ip, _Port);
        return (IServerHash) registry.lookup(_Name);          
    }
    
    public boolean Connected(String clientName, int portClient) throws RemoteException, NotBoundException{        
        return StartServer().Connected(clientName, portClient);
    }
    
    public boolean Test() throws RemoteException, NotBoundException{
        return StartServer().Test();
    }
    
    public void StartSearh(String UserName, int Port, String BaseText, String Criterial, char[] starKey,String nameTread, int nThreads) throws RemoteException, NotBoundException{
        StartServer().StartSearh(UserName, Port, BaseText, Criterial, starKey, 0, nameTread, nThreads);
    }
    
    public void Stop() throws RemoteException, NotBoundException{
        StartServer().EndHilo();
    }
}
