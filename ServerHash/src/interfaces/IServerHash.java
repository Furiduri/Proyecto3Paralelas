/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public interface IServerHash extends Remote{
    boolean Connected(String UserName, int Port) throws RemoteException;    
    boolean Test() throws RemoteException;
}
