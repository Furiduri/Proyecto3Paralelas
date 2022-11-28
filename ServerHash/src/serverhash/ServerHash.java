/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverhash;

import com.google.gson.Gson;
import interfaces.Client;
import interfaces.IServerHash;
import interfaces.ResponceHashServer;
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
import java.time.LocalDateTime;
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
    public String _Name;
    private String _Ip;
    public Client _Client = null;
    private SearchHashConcurrente _Hilo = null;
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
        if(Port.isEmpty())
            return;
        String name = JOptionPane.showInputDialog(null, "Ingrese Nombre a usar");
        if(name.isEmpty())
            return;
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
        try {
           String ip = RemoteServer.getClientHost();
            Client cl = new Client(UserName, ip, Port);                 
        return cl.TestConect(_Name, _Port);
        } catch (Exception ex) {
            Logger.getLogger(ServerHash.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
    }

    @Override
    public boolean Test() throws RemoteException {
        System.out.println("Test");
        return true;
    }

    @Override
    public void StartSearh(String UserName, int Port, String BaseText, String Criterial, char[] starKey,  int intentosMax, String nameTread, int nThreads ) throws RemoteException {
        System.out.println("StartSearh "+UserName );
        try {
            if(_Client != null){
                if(!_Client.UserName.equals(UserName))
                    new Exception("El Server esta ocupado");
            }else{
                String ip = RemoteServer.getClientHost(); 
                _Client = new Client(UserName, ip, Port);                
            }
            _Client.SendMSG("Iniciando...", _Name);
            if(_Hilo == null){
                _Hilo = new SearchHashConcurrente(BaseText, Criterial, starKey, intentosMax, nameTread, nThreads, this);
                _Hilo.start();
            }else{                        
                if(!_Hilo.flagTread){
                    try {
                        _Hilo.finalize();
                    } catch (Throwable ex) {
                        Logger.getLogger(ServerHash.class.getName()).log(Level.SEVERE, null, ex);
                    } finally{
                        _Hilo = null;
                         _Hilo = new SearchHashConcurrente(BaseText, Criterial, starKey, intentosMax, nameTread, nThreads, this);
                        _Hilo.start();
                    }                    
                }else{
                    _Client.SendMSG("No fue posible inicar el hilo, vulva a intentarlo", _Name);     
                }
            }
            
        } catch (Exception ex) {
            Logger.getLogger(ServerHash.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }

    public void SendFin(ResponceHashServer res) throws RemoteException, NotBoundException {
        Gson json = new Gson();        
        res.ServerName = _Name;
        _Client.SendResult(json.toJson(res));
        System.out.println("Se enviaron los resultado y se reinicio el usuario y hilo");
    }    

    @Override
    public void EndHilo() throws RemoteException {
        if(_Hilo != null){
            _Hilo.flagTread = false;          
        }
        System.out.println("Termino Hilo");
    }
     
}
