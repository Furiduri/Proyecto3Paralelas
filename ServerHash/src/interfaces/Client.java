/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class Client {
     public  String UserName;
    public String IP;
    public int PORT;

    public Client(String UserName, String IP, int PORT){
        this.UserName = UserName;
        this.IP = IP;
        this.PORT = PORT;
    } 
    
    
    private IClientHash StartClient() throws RemoteException, NotBoundException {        
            Registry registry = LocateRegistry.getRegistry(this.IP, this.PORT);
            return  (IClientHash) registry.lookup(this.UserName); 
    }
    
    public boolean TestConect(String Name, int Port) throws RemoteException, NotBoundException{        
       IClientHash clientS = StartClient();
        return clientS.Test(Name, Port);
    }
}
