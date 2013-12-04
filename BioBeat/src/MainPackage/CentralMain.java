package MainPackage;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Elements;
import udp.datatransmission.DataTransmission;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lenovo212
 */
public class CentralMain {

//    public final static String BASE_URL = "/home/pi/public/";
//    private final static String SONGS_XML_PATH = BASE_URL + "BioBeat_songs.xml";
//    private final static String MOODS_XML_PATH = BASE_URL + "BioBeat_moods.xml";
    private ArrayList<String> moodsList;
    private Playlist playlist;
    private final GUIRunnable guiRunnable;
    private DataTransmission dt;
    ArrayList<Playlist.Song> songsToBePlayed;
    int songCurrentIndex;
    //////////////// Variables for testing purposes only
    public final static String BASE_URL = "c:/";
    private final static String SONGS_XML_PATH = BASE_URL + "BioBeat_songs.xml";
    private final static String MOODS_XML_PATH = BASE_URL + "BioBeat_moods.xml";
    //////////////// END OF TESTING VARIABLES ////////////////////////

    CentralMain() {
        Document mDoc = CentralMain.loadXml(MOODS_XML_PATH);
        populateMoodsList(mDoc);
        this.playlist = new Playlist(SONGS_XML_PATH);


        /* Create and display the form */
        guiRunnable = new GUIRunnable(new GUIController(this));
        guiRunnable.run();

//        
//        //just testing
//        songsToBePlayed = playlist.getSongsWithMood(moodsList.get(0));
//            try {
//                playlist.setPlaySong(songsToBePlayed.get(0));
//            } catch (Exception ex) {
//                Logger.getLogger(CentralMain.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            playlist.overwriteXmlFile();
        //////////////////////////////////////////////////////////


        // Start UDP connection to recieve and send
        dt = new DataTransmission(new MyIncomingActionListener());
    }

    public static Document loadXml(String path) {
        Builder xBuilder = new Builder(false);
        Document xDoc = null;
        try {
            xDoc = xBuilder.build(new File(path));
        } catch (Exception ex) {
            CentralMain.displayErrorDialog("There was an error while trying to load the xml.");
            System.exit(1);
        }
        return xDoc;
    }

    private void populateMoodsList(Document moodsDoc) {
        this.moodsList = new ArrayList<String>();
        Elements moods = moodsDoc.getRootElement().getChildElements().get(0).getChildElements();
        for (int i = 0; i < moods.size(); i++) {
            this.moodsList.add(moods.get(i).getValue());
        }
    }

    public void addSong(String name, String mood) {
        try {
            this.playlist.addSong(CentralMain.BASE_URL + name, mood, false);
            this.playlist.overwriteXmlFile();
            this.guiRunnable.getSongListModel();
        } catch (Exception ex) {
            System.err.println(ex);
            CentralMain.displayErrorDialog("Unable to overwrite the XML.\nTry to restart the program.");

        }
    }

    public void removeSong(String song, String mood) {
        try {
            this.playlist.removeSong(song, mood);
            this.playlist.overwriteXmlFile();
            this.guiRunnable.getSongListModel();
        } catch (Exception ex) {
            System.err.println(ex);
            CentralMain.displayErrorDialog("Ops, there was an error while trying to remove the song.\nPlease try again.");
        }
    }

    protected void nextSong() {

        if (songsToBePlayed == null || songsToBePlayed.size() == 0) {
            return;
        }
        if (songCurrentIndex < songsToBePlayed.size() - 1) {
            songCurrentIndex++;
        } else {
            songCurrentIndex = 0;
        }
        updateSongtoPlay();
    }

    protected void prevSong() {
        if (songsToBePlayed == null || songsToBePlayed.size() == 0) {
            return;
        }
        if (songCurrentIndex == 0) {
            songCurrentIndex = songsToBePlayed.size() - 1;
        } else {
            songCurrentIndex--;
        }
        updateSongtoPlay();
    }

    protected void updateSongtoPlay() {
        try {
            Playlist.Song song = songsToBePlayed.get(songCurrentIndex);
            playlist.setPlaySong(song);
            playlist.overwriteXmlFile();
            this.guiRunnable.setSongonplayText(song.getName()+"<"+song.getMood()+">");
        } catch (Exception ex) {
            CentralMain.displayErrorDialog("There was an error while setting the song to be played in the XML.\n this will affect the player");
        }

    }

    protected static void copyFileUsingStream(File source, File dest) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            is.close();
            os.close();
        } catch (IOException ex) {
            CentralMain.displayErrorDialog("There was an error while trying to copy the song to the shared folder.\n please copy the song manually and try again.");
        }
    }
    ///////////////////////////////////////////////////
    //            GETTERS AND SETTERS 
    ///////////////////////////////////////////////////

    public ArrayList<String> getMoodsList() {
        return moodsList;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public static void displayErrorDialog(String msg) {

        JOptionPane.showMessageDialog(new Frame(),
                msg,
                "Error Loading Songs",
                JOptionPane.ERROR_MESSAGE);
    }

    ///////////////////////////////////////////////////
    //            MAIN 
    ///////////////////////////////////////////////////
    ////////////////////////////////////////////
    /////           INNER GUI MVC CLASS
    //////////////////////////////////////////
    // the VIEW is in a runnable
    class GUIRunnable implements Runnable {

        GUI gui;
        GUIController gc;

        GUIRunnable(GUIController gc) {
            super();
            this.gc = gc;
            SongListTreeModel loadSongsList = getSongListModel();
            this.gui = new GUI(loadSongsList, this.gc);
        }

        private SongListTreeModel getSongListModel() {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Moods Playlist");
            SongListTreeModel slTreeModel = new SongListTreeModel(rootNode, getMoodsList(), getPlaylist());
            return slTreeModel;
        }

        @Override
        public void run() {
            if (this.gui == null) {
                this.gui = new GUI(getSongListModel(), this.gc);
                getSongListModel();
            }

            this.gui.setVisible(true);
        }

        public void setSongonplayText(String txt) {
            this.gui.songonplaytext(txt);
        }

        public GUI getGUI() {
            return this.gui;
        }
    }

    //////////////////////////////////////////////////////
    //             INCOMING CONNECTION LISTENER
    ////////////////////////////////////////////////////
    void sendMsg(int cmd) {
        dt.sendCMD(cmd);
    }

    class MyIncomingActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            String actionCommand = e.getActionCommand();

            System.out.println("recieved >> " + Integer.parseInt(actionCommand));

            int mood_index = Integer.parseInt(actionCommand) - 1;

            //repeat will occur every 20 seconds.. timer is workign in the monitor 
            songsToBePlayed = playlist.getSongsWithMood(moodsList.get(mood_index));
            songCurrentIndex = (int) Math.random() * songsToBePlayed.size();
            updateSongtoPlay();
        }
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        CentralMain CM = new CentralMain();


    }
}
