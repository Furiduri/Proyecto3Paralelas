/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthash;

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

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ServerClient implements interfaces.IClientHash{
    
    public int _PORT;
    public String _IP;    
    public String _Name;
    private ArrayList<ServerInfo> ServersList;

    public ArrayList<ServerInfo> getServersList() {
        return ServersList;
    }

    public ServerClient(int port, String Name) throws UnknownHostException, RemoteException, AlreadyBoundException {        
        ServersList =  new ArrayList<ServerInfo>();
        _PORT = port;
        InetAddress address = InetAddress.getLocalHost();
        _IP = address.getHostAddress();
        _Name = Name;
        Remote remote = UnicastRemoteObject.exportObject(this, _PORT);
        Registry registry = LocateRegistry.createRegistry(_PORT);
        System.out.println("Servidor escuchando en el puerto " + String.valueOf(_PORT));
        System.out.println("IP: " + _IP);
        registry.bind(_Name, remote); // Registrar ChatServer 
    }
    
    public boolean AddServer(String Name, String ip, int Port) throws RemoteException, NotBoundException{        
            ServerInfo server =  new ServerInfo(Port, ip, Name);
            return server.Connected(_Name, _PORT); 
    }
    
    public void TestConnectServers(){
        for (ServerInfo item : ServersList) {
            try {
                item.Test();
            } catch (Exception e) {
                ServersList.remove(item);
            }
        }
    }
    
    @Override
    public boolean Test(String Name, int Port) throws RemoteException {
        try {
            String ip = RemoteServer.getClientHost();
            ServerInfo server =  new ServerInfo(Port, ip, Name);
            Optional<ServerInfo> resuls = ServersList.stream().filter(e -> e.getName().equals(server.getName())).findFirst();
            if (!resuls.isPresent()) {
                ServersList.add(server);
            }
            return true;
        } catch (ServerNotActiveException ex) {
            Logger.getLogger(ServerClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
