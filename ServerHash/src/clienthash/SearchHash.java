/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthash;

import interfaces.ResponceHashServer;
import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import javax.swing.*;
import utils.Hashing;
import utils.Utils;

/**
 *
 * @author furid
 */
public class SearchHash extends Thread {

    String BaseText;
    String Criterial;
    String HashFinding;
    char[] key = {0};
    int Count = 0;
    boolean flagTread = true;
    int MaxIntent = 0;
    LocalDateTime StartDate;    
    LocalDateTime EndDate;
    String TimerEnd;
    JButton btnFinish;
    public ResponceHashServer resItem;
    JTextArea LogArea;
    boolean  IsCancel = false;
    
    public SearchHash(String BaseText, String Criterial, int intentosMax, String nameTread, JButton btnFinsh, JTextArea logArea) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent =intentosMax;
        this.btnFinish = btnFinsh;
        this.LogArea = logArea;
    }

    @Override
    public void run() {
        try {
            while (flagTread && (MaxIntent >= Count || MaxIntent == 0)) {
                this.OtroIntento();
               HashFinding = Hashing.sha256(BaseText + Arrays.toString(key));
                if (HashFinding.matches(Criterial)) {
                    break;
                }
                key = Utils.GetNextAscii(key, 0);
            }
            PrintLog("Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        } catch (Exception e) {
            System.err.println(e);
            PrintLog("ERROR! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        }
        EndDate = LocalDateTime.now();
        long res = Utils.GetMiliTime(StartDate, EndDate); 
        PrintLog(" Time ms: "+res);
        TimerEnd = "Tiempo:  ms: "+res;
        resItem = new ResponceHashServer(BaseText, Criterial, HashFinding, key, Count, 1, StartDate, EndDate, TimerEnd, new char[] {0}, this.getName());
        btnFinish.dispatchEvent(new MouseEvent(btnFinish, MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,10,10,1,false));
    }

    private void OtroIntento() {
        Count++;
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            flagTread = false;
        } finally {
            super.finalize();
        }
    }

    public void destroy() {
        flagTread = false;
    }
    
    private void PrintLog(String msg){
        LogArea.append(this.getName() + ": "+ msg +"\n");

    }
}
