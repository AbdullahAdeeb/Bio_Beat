/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.io.File;
import java.util.ArrayList;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

/**
 *
 * @author lenovo212
 */
public class Playlist {

    ArrayList<Song> songsList;
    Document xSongs;

    Playlist(Document songsXml) {
        songsList = new ArrayList<Song>();
        this.xSongs = songsXml;
        loadSongs();
    }

    private void loadSongs() {
        Elements rootElements = xSongs.getRootElement().getChildElements();
        Elements songs = rootElements.get(0).getChildElements();      // 0 to get the <songs>    otherwise use 1 to get <lights>
        for (int i = 0; i < songs.size(); i++) {
            Element s = songs.get(i);
            this.songsList.add(new Song(s.getValue(),s.getAttributeValue("mood")));
            
        }
        
        //TODO add for loop to load the lights
    }
    
    public ArrayList<Song> getSongsWithMood(String mood){
        ArrayList<Song> filteredArray = new ArrayList<Song>();
        for (int i = 0; i < this.songsList.size(); i++) {
            Song s = this.songsList.get(i);
            String m = s.getMood();
            if (m.equals(mood)) {
                System.out.println("heloooo");
                filteredArray.add(s);
            }
        }
        return filteredArray;
    }


    class Song {

        private File file;
        private String mood;

        Song(File f, String m) {
            this.file = f;
            this.mood = m;
        }
        
        Song(String f, String m) {
            this.file = new File(f);
            this.mood = m;
            System.out.println("song is read with <"+f+"> and mood <"+m+">");

        }

        public File getFile() {
            return file;
        }

        public String getMood() {
            return mood;
        }
    }
}
