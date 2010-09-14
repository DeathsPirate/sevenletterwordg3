package seven.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GameEngine.java
 *
 * Created on Nov 1, 2009, 6:49:08 PM
 */

/**
 *
 * @author Satyajeet
 */
public class GameEngine extends javax.swing.JFrame {

    public static IOController iocontroller;
    public static GameConfig gameconfig;
    public static GameController gamecontroller;
    public static JTable GameScreen;
    Thread grunner;
    static Boolean play;
    public static GameEngine thisGameEngine;
    /** Creates new form GameEngine */
    public static javax.swing.Timer memUpdate;
    private Logger logger = Logger.getLogger(GameEngine.class);

    static {
		PropertyConfigurator.configure("logger.properties");
    }
    public GameEngine() {
        initComponents();
        myInit();
    }

        private static class GameRunner implements Runnable
    {

        public void run() {
            try {
                Boolean play = true;
                int score;
                while(play == true)
                {
                    score = gamecontroller.GamePlay(gameconfig).retValue;
                    thisGameEngine.updateUI();
                if(score ==-1)
                {
                    // Game must have ended, since you have a score now
                   play = false;
                   String gameover = "Game Over.";
                   JOptionPane.showMessageDialog(GameScreen, gameover);
                }

                // Update user interface!

                Thread.sleep(gameconfig.gameDelay);

                }
            } catch (InterruptedException ex) {
                System.out.println("Game runner interrupted");
            }
        }


    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ScreenTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        PlayerListbox = new javax.swing.JList();
        input_combo = new javax.swing.JComboBox();
        AddPlayerButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        secretBox = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        GameDelaySlider = new javax.swing.JSlider();
        jLabel3 = new javax.swing.JLabel();
        RoundsBox = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        NewGameButton = new javax.swing.JButton();
        StepButton = new javax.swing.JButton();
        PlayButton = new javax.swing.JButton();
        PauseButton = new javax.swing.JButton();
        TournamentButton = new javax.swing.JButton();
        LetterLabel = new javax.swing.JLabel();
        RoundsLabel = new javax.swing.JLabel();
        StatusLabel = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtMemUsage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Seven Letter Word - Game Simulator");

        ScreenTable.setFont(new java.awt.Font("Courier", 0, 14)); // NOI18N
        ScreenTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Player Name", "Score", "Secret Letters", "Open Letters", "Bid", "Word Returned", "Points", "Letters used"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ScreenTable.setRowHeight(20);
        ScreenTable.setRowMargin(5);
        jScrollPane1.setViewportView(ScreenTable);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jScrollPane2.setViewportView(PlayerListbox);

        input_combo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        AddPlayerButton.setText("Add Player");
        AddPlayerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddPlayerButtonActionPerformed(evt);
            }
        });

        jButton1.setText("Remove");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Hidden Letters: ");

        secretBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "1", "2", "3", "4", "5", "6", "7" }));

        jLabel2.setText("Game Delay (0-1000ms):");

        GameDelaySlider.setMaximum(1000);
        GameDelaySlider.setValue(800);
        GameDelaySlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                GameDelaySliderStateChanged(evt);
            }
        });

        jLabel3.setText("Number of Rounds:");

        RoundsBox.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100", "101", "102", "103", "104", "105", "106", "107", "108", "109", "110", "111", "112", "113", "114", "115", "116", "117", "118", "119", "120", "121", "122", "123", "124", "125", "126", "127", "128", "129", "130", "131", "132", "133", "134", "135", "136", "137", "138", "139", "140", "141", "142", "143", "144", "145", "146", "147", "148", "149", "150", "151", "152", "153", "154", "155", "156", "157", "158", "159", "160", "161", "162", "163", "164", "165", "166", "167", "168", "169", "170", "171", "172", "173", "174", "175", "176", "177", "178", "179", "180", "181", "182", "183", "184", "185", "186", "187", "188", "189", "190", "191", "192", "193", "194", "195", "196", "197", "198", "199", "200", "201", "202", "203", "204", "205", "206", "207", "208", "209", "210", "211", "212", "213", "214", "215", "216", "217", "218", "219", "220", "221", "222", "223", "224", "225", "226", "227", "228", "229", "230", "231", "232", "233", "234", "235", "236", "237", "238", "239", "240", "241", "242", "243", "244", "245", "246", "247", "248", "249", "250", "251", "252", "253", "254", "255", "256", "257", "258", "259", "260", "261", "262", "263", "264", "265", "266", "267", "268", "269", "270", "271", "272", "273", "274", "275", "276", "277", "278", "279", "280", "281", "282", "283", "284", "285", "286", "287", "288", "289", "290", "291", "292", "293", "294", "295", "296", "297", "298", "299", "300", "301", "302", "303", "304", "305", "306", "307", "308", "309", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "322", "323", "324", "325", "326", "327", "328", "329", "330", "331", "332", "333", "334", "335", "336", "337", "338", "339", "340", "341", "342", "343", "344", "345", "346", "347", "348", "349", "350", "351", "352", "353", "354", "355", "356", "357", "358", "359", "360", "361", "362", "363", "364", "365", "366", "367", "368", "369", "370", "371", "372", "373", "374", "375", "376", "377", "378", "379", "380", "381", "382", "383", "384", "385", "386", "387", "388", "389", "390", "391", "392", "393", "394", "395", "396", "397", "398", "399", "400", "401", "402", "403", "404", "405", "406", "407", "408", "409", "410", "411", "412", "413", "414", "415", "416", "417", "418", "419", "420", "421", "422", "423", "424", "425", "426", "427", "428", "429", "430", "431", "432", "433", "434", "435", "436", "437", "438", "439", "440", "441", "442", "443", "444", "445", "446", "447", "448", "449", "450", "451", "452", "453", "454", "455", "456", "457", "458", "459", "460", "461", "462", "463", "464", "465", "466", "467", "468", "469", "470", "471", "472", "473", "474", "475", "476", "477", "478", "479", "480", "481", "482", "483", "484", "485", "486", "487", "488", "489", "490", "491", "492", "493", "494", "495", "496", "497", "498", "499", "500" }));

        jLabel4.setText("Program Parameters:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(input_combo, 0, 223, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(AddPlayerButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(GameDelaySlider, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)
                    .addComponent(jLabel2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(secretBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(RoundsBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel4))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(input_combo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddPlayerButton)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(secretBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(RoundsBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(GameDelaySlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        NewGameButton.setText("Begin New Game");
        NewGameButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NewGameButtonActionPerformed(evt);
            }
        });

        StepButton.setText("Step");
        StepButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StepButtonActionPerformed(evt);
            }
        });

        PlayButton.setText("Play");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        PauseButton.setText("Pause");
        PauseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PauseButtonActionPerformed(evt);
            }
        });

        TournamentButton.setText("Play Tournament");
        TournamentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TournamentButtonActionPerformed(evt);
            }
        });

        LetterLabel.setBackground(new java.awt.Color(255, 102, 102));
        LetterLabel.setFont(new java.awt.Font("Tahoma", 0, 14));
        LetterLabel.setText("Current Letter:");

        RoundsLabel.setBackground(new java.awt.Color(255, 102, 102));
        RoundsLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        RoundsLabel.setText("Current Round:");
        RoundsLabel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NewGameButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(StepButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PlayButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PauseButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TournamentButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LetterLabel)
                .addGap(18, 18, 18)
                .addComponent(RoundsLabel)
                .addContainerGap(247, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NewGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(StepButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(PlayButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(PauseButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(TournamentButton, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(LetterLabel)
                    .addComponent(RoundsLabel))
                .addContainerGap())
        );

        StatusLabel.setBackground(new java.awt.Color(153, 153, 255));
        StatusLabel.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        StatusLabel.setText("Status:");

        jLabel5.setText("Program Controls:");

        txtMemUsage.setText("MEM");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 770, Short.MAX_VALUE)
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(StatusLabel)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 878, Short.MAX_VALUE)
                        .addComponent(txtMemUsage)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel5))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(txtMemUsage)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 374, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(StatusLabel))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void AddPlayerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddPlayerButtonActionPerformed
        // TODO add your handling code here:
        String player = (String)input_combo.getSelectedItem();
        //PlayerListbox.setModel(new DefaultListModel());
        DefaultListModel dlm = (DefaultListModel) PlayerListbox.getModel();
        dlm.add(dlm.getSize(),(Object)player);


    }//GEN-LAST:event_AddPlayerButtonActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         DefaultListModel dlm = (DefaultListModel) PlayerListbox.getModel();
         int index;
         do{
             index = PlayerListbox.getSelectedIndex();
             try{
             dlm.remove(index);
             } catch(Exception e)
             {
                 // Do nothing!
             }

         }while(PlayerListbox.getSelectedIndices().length != 0);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void NewGameButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NewGameButtonActionPerformed
        // TODO add your handling code here:

        gamecontroller = new GameController();
        // Lets create the playerstate object
        DefaultListModel dlm = (DefaultListModel) PlayerListbox.getModel();
        ArrayList<String> plist = new ArrayList<String>();
        for(int loop=0;loop<dlm.getSize();loop++)
        {
            plist.add((String)dlm.get(loop));
        }
        int numR = Integer.parseInt((String)RoundsBox.getSelectedItem());
        int secret_objs = Integer.parseInt((String)secretBox.getSelectedItem());



        gameconfig = new GameConfig(plist, secret_objs, iocontroller,numR);
        updateUI();
    }//GEN-LAST:event_NewGameButtonActionPerformed

    private void GameDelaySliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_GameDelaySliderStateChanged
        // TODO add your handling code here:
        if(gameconfig != null)
        {
            gameconfig.gameDelay = GameDelaySlider.getValue();
        }
    }//GEN-LAST:event_GameDelaySliderStateChanged

    private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayButtonActionPerformed
        // TODO add your handling code here:

        grunner = new Thread(new GameRunner());
        grunner.start();

    }//GEN-LAST:event_PlayButtonActionPerformed

    private void PauseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PauseButtonActionPerformed
        // TODO add your handling code here:
        if(grunner != null)
        {
            grunner.interrupt();
        }

    }//GEN-LAST:event_PauseButtonActionPerformed

    private void StepButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StepButtonActionPerformed
        // TODO add your handling code here:
        if(gameconfig != null)
        {
            int score = gamecontroller.GamePlay(gameconfig).retValue;
            updateUI();
            if(score == -1)
            {
                String gameover = "Game Over";
                JOptionPane.showMessageDialog(GameScreen, gameover);
            }
            else
            {

                // Update user interface
            }
        }


    }//GEN-LAST:event_StepButtonActionPerformed

    private void TournamentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TournamentButtonActionPerformed
        // TODO add your handling code here:
        int number_of_games = 10;
        String filename = JOptionPane.showInputDialog(null, "Enter Filename!");
        ArrayList<GameResult> gameresults = new ArrayList<GameResult>();

        for(int loop1=0;loop1<number_of_games;loop1++)
        {
            gamecontroller = new GameController();
            // Lets create the playerstate object
            DefaultListModel dlm = (DefaultListModel) PlayerListbox.getModel();
            ArrayList<String> plist = new ArrayList<String>();
            for(int loop=0;loop<dlm.getSize();loop++)
            {
                plist.add((String)dlm.get(loop));
            }
            int numR = Integer.parseInt((String)RoundsBox.getSelectedItem());
            int secret_objs = Integer.parseInt((String)secretBox.getSelectedItem());

            int returnValue = 0;

            gameconfig = new GameConfig(plist, secret_objs, iocontroller,numR);
            GameResult gameresult;
            do{
                gameresult = gamecontroller.GamePlay(gameconfig);
                returnValue = gameresult.retValue;
            }while(returnValue != -1);



            // Now we are done, so save the gameresult

            gameresults.add(gameresult);
        }
        try{
         BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filename));
         for(int loop=0;loop<gameresults.size();loop++)
         {
             String output = "";
             bufferedWriter.write(Integer.toString(loop));
             for(int loop2=0;loop2<gameresults.get(loop).scoreList.size();loop2++)
             {
                 output += "," + gameresults.get(loop).scoreList.get(loop2).toString();
             }
             System.out.println(output);
             bufferedWriter.write(output);
             bufferedWriter.newLine();
         }

        bufferedWriter.close();
        }catch(Exception ex)
        {
            System.out.println("Could not write tournament output");
        }

        System.out.println("Tournament done!");
    }//GEN-LAST:event_TournamentButtonActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
    	System.setOut(
    		    new PrintStream(new OutputStream() {
					@Override
					public void write(int b) throws IOException {						
					}
				})); 
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GameEngine().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddPlayerButton;
    private javax.swing.JSlider GameDelaySlider;
    private javax.swing.JLabel LetterLabel;
    private javax.swing.JButton NewGameButton;
    private javax.swing.JButton PauseButton;
    private javax.swing.JButton PlayButton;
    private javax.swing.JList PlayerListbox;
    private javax.swing.JComboBox RoundsBox;
    private javax.swing.JLabel RoundsLabel;
    private javax.swing.JTable ScreenTable;
    private javax.swing.JLabel StatusLabel;
    private javax.swing.JButton StepButton;
    private javax.swing.JButton TournamentButton;
    private javax.swing.JComboBox input_combo;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JComboBox secretBox;
    private javax.swing.JLabel txtMemUsage;
    // End of variables declaration//GEN-END:variables


    public void myInit()
    {
        iocontroller = new IOController();
        populatePlayerList();
        PlayerListbox.setModel(new DefaultListModel());
        GameScreen = ScreenTable;
        thisGameEngine = this;
        Runtime r = Runtime.getRuntime();
		long free = r.freeMemory();
		txtMemUsage.setText(((r.totalMemory() - free)/1024/1024) + "/"+(r.maxMemory()/1024/1024)+"MB");
        memUpdate= new Timer(500, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Runtime r = Runtime.getRuntime();
				long free = r.freeMemory();
//				logger.debug("Currently using " +
//						(r.totalMemory() - free) + " bytes(?) -- free " + free
//				);
				txtMemUsage.setText(((r.totalMemory() - free)/1024/1024) + "/"+(r.maxMemory()/1024/1024)+"MB");

			}
		});
        memUpdate.start();
        this.validate();
    }

    public void populatePlayerList()
    {
        input_combo.removeAllItems();
        ArrayList<String> playernames = iocontroller.getPlayerList();
        input_combo.setModel(new DefaultComboBoxModel());
        DefaultComboBoxModel dcm = (DefaultComboBoxModel)input_combo.getModel();
        for(int loop=0;loop<playernames.size();loop++)
        {
            dcm.addElement((Object)playernames.get(loop));
        }
    }

    public void updateUI()
    {
        // First remove the current rows


        String bidletter = "";
        DefaultTableModel dtm = (DefaultTableModel)ScreenTable.getModel();

        while(dtm.getRowCount() != 0)
        {
            dtm.removeRow(0);

        }
        // Now populate the table room by room:
        for(int loop=0;loop<gameconfig.PlayerList.size();loop++)
        {
            String playername = gameconfig.PlayerList.get(loop);
            int score = gameconfig.secretstateList.get(loop).score;
            String secretLetters = "   ";
            for(int letterloop=0;letterloop<gameconfig.secretstateList.get(loop).secretLetters.size();letterloop++)
            {
                secretLetters += " " + gameconfig.secretstateList.get(loop).secretLetters.get(letterloop).alphabet;
            }
            String openLetters = "   ";
            for(int letterloop=0;letterloop<gameconfig.openstateList.get(loop).openLetters.size();letterloop++)
            {
                openLetters += " " + gameconfig.openstateList.get(loop).openLetters.get(letterloop).alphabet;
            }
            int lastbidIndex = gameconfig.BidList.size() - 1;
            int bidvalue = 0;
            try{
            if(gameconfig.BidList.get(lastbidIndex) != null)
            {
                bidvalue = gameconfig.BidList.get(lastbidIndex).bidvalues.get(loop);
                bidletter = gameconfig.BidList.get(lastbidIndex).TargetLetter.alphabet.toString();
                LetterLabel.setText("Current Letter: " + bidletter);
                PlayerBids l = gameconfig.BidList.get(lastbidIndex);
                String formatString = "<html>Status: Letter won by player %d [%s]<br>"
							+ "Winning bid: %d (Actually paid: %d)</html>";
                int winner_id = l.getWinnerID();
				Object formatargs[] = {
						winner_id + 1, l.getWonBy(), l.getBidvalues().get(winner_id),l.getWinAmmount()
				};
                StatusLabel.setText(String.format(formatString, formatargs));
            }
            }catch(Exception ex)
            {
                // Do nothing
            }
            String wordReturned = "";
            try{
            if(gameconfig.PlayerWords.get(loop) != null)
            {
                wordReturned = gameconfig.PlayerWords.get(loop);
                if(wordReturned.length() == 7)
                	wordReturned = "<html><b>"+wordReturned+"</b></html>";
                else if(wordReturned.length() == 0)
                	wordReturned = " ";
            }
            } catch(Exception ex)
            {
                // Do nothing
            }
            String wbstring = "";
            String points = "";
            if(gameconfig.wordbag.size() !=0)
            {
                wbstring = gameconfig.wordbag.get(loop);

            }

            if(gameconfig.lasPoints.size() != 0)
            {
                points = gameconfig.lasPoints.get(loop).toString();

            }
            Object[] UIData = new Object[]{playername.substring(playername.indexOf(".")+1),score + "   ",secretLetters,openLetters,bidvalue+ "   ",wordReturned,points,wbstring};
            dtm.addRow(UIData);
            RoundsLabel.setText("Current round: " + (gameconfig.current_round + 1));

        }

    }

}
