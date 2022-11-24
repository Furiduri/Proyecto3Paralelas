/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhash;

import interfaces.Client;
import interfaces.IServerHash;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utils.Json;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ServerHash implements IServerHash{

    private int _Port;
    private String _Name;
    private String _Ip;
    public ServerHash(int Port, String Name, String ip){
        _Port = Port;     
        _Name = Name;
        _Ip = ip;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RemoteException, UnknownHostException, AlreadyBoundException {
        String Port = JOptionPane.showInputDialog(null, "Ingrese Puerto a usar");
        String name = JOptionPane.showInputDialog(null, "Ingrese Nombre a usar");
        int PORT = Integer.valueOf(Port);
        InetAddress address = InetAddress.getLocalHost();
        Remote remote = UnicastRemoteObject.exportObject(new ServerHash(PORT, name,address.getHostAddress()), PORT);
        Registry registry = LocateRegistry.createRegistry(PORT);
        System.out.println("Servidor ["+name+"] escuchando en " +address.getHostAddress()+":" + String.valueOf(PORT));        
        registry.bind(name, remote); // Registrar server        
    }

    @Override
    public boolean Connected(String UserName, int Port) throws RemoteException {
        System.out.println("New connec: "+UserName);
        String ip;
        try {
            ip = RemoteServer.getClientHost();
            Client cl = new Client(UserName, ip, Port);                 
        return cl.TestConect(_Name, _Port);
        } catch (Exception ex) {
            Logger.getLogger(ServerHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
    }

    @Override
    public boolean Test() throws RemoteException {
        return true;
    }
    
}
