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
 * @author lenovo212
 */
class GUIController implements ActionListener {

    CentralMain central;

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

    private void jPlayButtonActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jAddButtonActionPerformed(java.awt.event.ActionEvent evt) {
//        File f = new File(jPathField.getText());
//        if (!f.isFile() || !f.exists()) {
//            this.controller.displayErrorDialog("The song file path specified is invalid. \nmake sure the song is still there");
//            return;
//        }
//
//        this.controller.copyFileUsingStream(f, new File(CentralMain.BASE_URL + f.getName()));
//        this.controller.addSong(f.getName(), (String) jMoodsList.getSelectedItem());
    }

    public CentralMain getCentral() {
        return this.central;
    }

    public void sendMsg(int cmd) {
        this.central.sendMsg(cmd);
    }

    private void jNextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        this.central.
        sendMsg(DataTransmission.CMD_NEXT);
    }

    private void jPrevButtonActionPerformed(java.awt.event.ActionEvent evt) {
        sendMsg(DataTransmission.CMD_PREVIOUS);
    }

    private void jVolUpActionPerformed(java.awt.event.ActionEvent evt) {
        sendMsg(DataTransmission.CMD_VOLINC);
    }

    private void jVolDownActionPerformed(java.awt.event.ActionEvent evt) {
        sendMsg(DataTransmission.CMD_VOLDEC);
    }

    void removeSong(String song, String mood) {
        this.central.removeSong(song, mood);
    }

    void addSong(String name, String string) {
        this.central.addSong(name, string);
    }
}
