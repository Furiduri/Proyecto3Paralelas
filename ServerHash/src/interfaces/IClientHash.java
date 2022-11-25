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
public interface IClientHash extends Remote{
    boolean Test(String Name, int Port)throws RemoteException;
    void ReciveServerLog(String msg, String serverName) throws RemoteException;
    void ReciveResultHilo(String result) throws RemoteException;
}
