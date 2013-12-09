/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.util.ArrayList;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Abdullah Adeeb
 */
public class SongListTreeModel extends DefaultTreeModel {

    private ArrayList<String> moods;
    private Playlist pl;

    /*
     * this constructor will create a songs tree model to be used in the GUI
     */
    SongListTreeModel(DefaultMutableTreeNode rootNode, ArrayList<String> moods, Playlist pl) {
        super(rootNode, true);
        this.moods = moods;
        this.pl = pl;
        for (int i = 0; i < moods.size(); i++) {
            DefaultMutableTreeNode moodNode = new DefaultMutableTreeNode(moods.get(i));
            ArrayList<Playlist.Song> songs = pl.getSongsWithMood(moods.get(i));
            for (int j = 0; j < songs.size(); j++) {
                DefaultMutableTreeNode songNode = new DefaultMutableTreeNode(songs.get(j).getFile().getName(), false);
                moodNode.add(songNode);
            }
            rootNode.add(moodNode);
        }

    }
    /////////////////////
    // GETTER METHODS
    //////////////////
    public ArrayList<String> getMoods() {
        return moods;
    }

    public Playlist getPl() {
        return pl;
    }
}
