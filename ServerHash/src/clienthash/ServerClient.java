/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthash;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.Client;
import interfaces.ResponceHashServer;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.lang.reflect.Type;
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
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ServerClient implements interfaces.IClientHash{
    
    public int _PORT;
    public String _IP;    
    public String _Name;
    public ArrayList<ResponceHashServer> Responces; 
    private ArrayList<ServerInfo> ServersList;
     JTextArea _LogServer;
     ClientView View;

    public ArrayList<ServerInfo> getServersList() {
        return ServersList;
    }

    public ServerClient(int port, String Name, JTextArea logServer, ClientView view) throws UnknownHostException, RemoteException, AlreadyBoundException {        
        ServersList =  new ArrayList<ServerInfo>();
        Responces = new ArrayList<ResponceHashServer>();
        this.View = view;
        _PORT = port;
        InetAddress address = InetAddress.getLocalHost();
        _IP = address.getHostAddress();
        _Name = Name;
        _LogServer = logServer;
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

    @Override
    public void ReciveServerLog(String msg, String serverName) throws RemoteException {
        _LogServer.append(serverName +": "+ msg+"\n");
    }

    @Override
    public void ReciveResultHilo(String result) throws RemoteException {
        Gson json = new Gson();
        Type type = new TypeToken<ResponceHashServer>(){}.getType();
        ResponceHashServer res = json.fromJson(result, type);
        if(Responces.size() == 0){
            if(!res.HashFinding.isEmpty()){
                View.SotpServers();
                View.SetCorrectResult(res);
            }
        }
        Responces.add(res);        
        _LogServer.append("*********\nServer: "+res.ServerName+
                "\n StartKey: "+Arrays.toString(res.StartKey)+
                "\n Hash: "+res.HashFinding+
                "\n Key:"+Arrays.toString(res.key)+
                "\n "+res.TimerEnd + 
                "\n Cont: "+res.Count + " \n");
        if(Responces.size() == ServersList.size()){
            View.SotpServers();
        }
    }

}
