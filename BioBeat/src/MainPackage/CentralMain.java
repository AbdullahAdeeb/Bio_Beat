package MainPackage;

import java.awt.Frame;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Elements;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lenovo212
 */
public class CentralMain {

    private final static String SONGS_XML_PATH = "res/BioBeat_songs.xml";
    private final static String MOODS_XML_PATH = "res/BioBeat_moods.xml";
    public final static String SONGS_BASE_URL = "d:/songs/";
    private ArrayList<String> moodsList;
    private Playlist playlist;
    private final GUIRunnable guiRunnable;

    CentralMain() {
        Document mDoc = CentralMain.loadXml(MOODS_XML_PATH);
        populateMoodsList(mDoc);

        this.playlist = new Playlist(SONGS_XML_PATH);
        if (!this.playlist.getErrors().equals("")) {
            displayErrorDialog("The following songs were not found:\n" + getPlaylist().getErrors());
            this.playlist.clearErrors();
        }

        /* Create and display the form */
        guiRunnable = new GUIRunnable(this);
        guiRunnable.run();
    }

    public static Document loadXml(String path) {
        Builder xBuilder = new Builder(false);
        Document xDoc = null;
        try {
            xDoc = xBuilder.build(new File(path));
        } catch (Exception ex) {
            Logger.getLogger(CentralMain.class.getName()).log(Level.SEVERE, null, ex);
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

    public void addSong(String name, String mood) throws Exception {
        this.playlist.addSong(CentralMain.SONGS_BASE_URL + name, mood);
        this.guiRunnable.reloadSongsList();
    }

    public void removeSong(String song, String mood) {
        this.playlist.removeSong(song, mood);
        this.guiRunnable.reloadSongsList();

    }

    protected static void copyFileUsingStream(File source, File dest) throws IOException {
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
        } finally {
            is.close();
            os.close();
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

    public void displayErrorDialog(String msg) {
        new ErrorDialog(msg).run();
    }

    ///////////////////////////////////////////////////
    //            MAIN 
    ///////////////////////////////////////////////////
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

    class ErrorDialog extends Thread {

        String errormsg;

        ErrorDialog(String msg) {
            this.errormsg = msg;
        }

        @Override
        public void run() {
            JOptionPane.showMessageDialog(new Frame(),
                    this.errormsg,
                    "Error Loading Songs",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    class GUIRunnable implements Runnable {

        GUI gui;
        CentralMain centralMain;

        GUIRunnable(CentralMain central) {
            super();
            this.centralMain = central;
        }

        private void reloadSongsList() {
            gui.initSongsList();
        }

        @Override
        public void run() {
            this.gui = new GUI(this.centralMain);
            this.gui.setVisible(true);
        }

        public GUI getGUI() {
            return this.gui;
        }
    }
}
