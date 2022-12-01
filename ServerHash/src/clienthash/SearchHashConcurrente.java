/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clienthash;

import interfaces.ResponceHashServer;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import utils.Hashing;
import utils.Utils;

/**
 *
 * @author furid
 */
public class SearchHashConcurrente extends Thread {

    String BaseText;
    String Criterial;
    String HashFinding = null;
    char[] key = {0};
    int Count = 0;
    boolean flagTread = true;
    int MaxIntent = 0;
    int NThreads;
    LocalDateTime StartDate;    
    LocalDateTime EndDate;
    JButton btnFinish;
    JTextArea txtLog;
    public ResponceHashServer resItem;
    private String TimerEnd;
    public boolean IsCancel = false;

    public SearchHashConcurrente(String BaseText, String Criterial, int intentosMax, String nameTread, int nThreads, JTextArea txtLog, JButton btnFinsh) {
        super(nameTread);
        StartDate = LocalDateTime.now();
        this.BaseText = BaseText;
        this.Criterial = Criterial;
        this.MaxIntent = intentosMax;
        this.NThreads = nThreads;
        this.btnFinish = btnFinsh;
        this.txtLog = txtLog;        
    }


    @Override
    @SuppressWarnings("empty-statement")
    public void run() {
        ExecutorService executor = Executors.newFixedThreadPool(NThreads);
        char[] startKey = {0};
        int step = Math.round(256 / NThreads);
        for (int i = 0; i < NThreads; i++) {            
            char MaxKey = (char)(step*(i+1));
            char[] NewstartKey = {0,0,MaxKey};
            if(NThreads < i+1)
                NewstartKey = null;
            executor.execute(new SearchHash(
                    this, startKey.clone(), "SubHilo"+String.valueOf(i), NewstartKey)
            );
            startKey =  NewstartKey;
        }
        while (flagTread && HashFinding == null && !executor.isTerminated());
        executor.shutdown();
        PrintLog("Main! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
        EndDate = LocalDateTime.now();
        long res = Utils.GetMiliTime(StartDate, EndDate); 
        TimerEnd = "Tiempo:  ms: "+res;
        PrintLog(TimerEnd);        
        resItem = new ResponceHashServer(BaseText, Criterial, HashFinding, key, Count, 1, StartDate, EndDate, TimerEnd, new char[] {0}, this.getName());
        btnFinish.dispatchEvent(new MouseEvent(btnFinish, MouseEvent.MOUSE_CLICKED,System.currentTimeMillis(),0,10,10,1,false));
    }

    /**
     *
     * @throws Throwable
     */
    @Override
    protected void finalize() throws Throwable {        
            flagTread = false;        
    }

    public void destroy() {
        flagTread = false;
    }
    
    private void PrintLog(String msg){
        txtLog.append(this.getName() + ": "+ msg +"\n");
    }
    
    private class SearchHash extends Thread {

        SearchHashConcurrente Context;
        String HashFinding;
        char[] key = {0};
        int Count = 0;
        char[] MaxKey;   

        public SearchHash(SearchHashConcurrente context, char[] StartKey, String nameTread, char[] MaxKey) {
            super(nameTread);
            this.Context = context;
            this.key = StartKey;
            this.MaxKey = MaxKey;
        }

        @Override
        public void run() {
            try {
                while (Context.flagTread 
                    && (Context.MaxIntent >= Count || Context.MaxIntent == 0)
                    && (!(MaxKey.equals(key)) || MaxKey == null)
                    ) {
                    this.OtroIntento();
                    HashFinding = Hashing.sha256(Context.BaseText + Arrays.toString(key));
                    if (HashFinding.matches(Context.Criterial)) {
                        Context.HashFinding = HashFinding;
                        Context.key = key;
                        Context.flagTread = false;
                        break;
                    }
                    key = Utils.GetNextAscii(key, 0);
                }
                Context.PrintLog(this.getName()+ " Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));    
            } catch (Exception e) {
                System.err.println(e);
                Context.PrintLog("ERROR! Hash:" + HashFinding + "  KEY:" + Arrays.toString(key) + " Count:" + String.valueOf(Count));
            }
           
        }

        private void OtroIntento() {
            Count++;
            Context.Count++;
        }

        public void destroy() {
        }
    }
}
