/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import udp.datatransmission.DataTransmission;

/**
 *
 * @author Abdullah Adeeb
 */
class GUIController implements ActionListener {

    CentralMain central;
/*
 * this class will need an instance of the main central to create a bridge between the GUI and the central
 * the purpose of this class is to make it clear where View is from the Controller which used to be the Central
 */
    public GUIController(CentralMain cent) {
        this.central = cent;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == GUI.NEXT_BUT) {
            jNextButtonActionPerformed(e);
        }else if (e.getActionCommand() == GUI.PREV_BUT) {
            jPrevButtonActionPerformed(e);
        }else if (e.getActionCommand() == GUI.VOL_DWN) {
            jVolDownActionPerformed(e);
        }else if (e.getActionCommand() == GUI.VOL_UP) {
            jVolUpActionPerformed(e);
        }
    }
    ////////////////////////////////////////////
    // CALLBACK METHODS CALLED FROM THE GUI
    ////////////////////////////////////////////
    private void jPlayButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // nothing needed for now in the callback method
        // all the work is handeled by the GUI, because there is GUI changes that should run on the EDT
        
    }

    private void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
        // nothing needed for now in the callback method
        // all the work is handeled by the GUI, because there is GUI changes that should run on the EDT
    }

    private void jNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.central.nextSong();
        sendMsg(DataTransmission.CMD_NEXT);
    }

    private void jPrevButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.central.prevSong();
        sendMsg(DataTransmission.CMD_PREVIOUS);
    }

    private void jVolUpActionPerformed(java.awt.event.ActionEvent evt) {
        sendMsg(DataTransmission.CMD_VOLINC);
    }

    private void jVolDownActionPerformed(java.awt.event.ActionEvent evt) {
        sendMsg(DataTransmission.CMD_VOLDEC);
    }

    ///////////////////////////////////////
    //  MEHTODS TO CENTRAL
    //////////////////////////////////////
    public CentralMain getCentral() {
        return this.central;
    }

    public void sendMsg(int cmd) {
        this.central.sendMsg(cmd);
    }


    void removeSong(String song, String mood) {
        this.central.removeSong(song, mood);
    }

    void addSong(String name, String string) {
        this.central.addSong(name, string);
    }
}
