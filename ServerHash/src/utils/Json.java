/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import interfaces.Client;
import java.util.ArrayList;
import java.lang.reflect.Type;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class Json {
   public static String toJSON(ArrayList list) {
        Gson gson = new Gson();
        return gson.toJson(list);
    }    
   
   public static ArrayList<Client> toArray(String json){
       Gson gson = new Gson(); 

        Type clientListType = new TypeToken<ArrayList<Client>>(){}.getType();
        ArrayList<Client> clientArrary = gson.fromJson(json, clientListType);           
        return clientArrary;
   }
}
