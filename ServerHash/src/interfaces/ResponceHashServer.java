/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Jorge Osvaldo perez Mendoza
 */
public class ResponceHashServer {
    public  String BaseText;
    public  String Criterial;
    public  String HashFinding;
    public  char[] key;
    public  int Count;
    public  int NThreads;
    public  String StartDate;    
    public  String EndDate;
    public  String TimerEnd;
    public  char[] StartKey;
    public String ServerName;

    public ResponceHashServer(String BaseText, String Criterial, String HashFinding, char[] key, int Count, int NThreads, LocalDateTime StartDate, LocalDateTime EndDate, String TimerEnd, char[] StartKey, String ServerName) {
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.HashFinding = HashFinding;
        this.key = key;
        this.Count = Count;
        this.NThreads = NThreads;
        this.StartDate = StartDate.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
        this.EndDate = EndDate.toLocalTime().format(DateTimeFormatter.ISO_LOCAL_TIME);
        this.TimerEnd = TimerEnd;
        this.StartKey = StartKey;
        this.ServerName = ServerName;
    }   
    
}
