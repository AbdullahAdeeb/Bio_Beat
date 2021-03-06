/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import nu.xom.Attribute;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;
import nu.xom.Serializer;

/**
 *
 * @author lenovo212
 */
public class Playlist {
    
    ArrayList<Song> songsList;
    private Document xSongs;
    private Element songsElement;  // this is the <songs> wrapper tag that contains all the song elements
    private String songsXmlPath;
    
    Playlist(String xmlPath) {
        this.songsXmlPath = xmlPath;
        this.xSongs = CentralMain.loadXml(this.songsXmlPath);
        loadSongs();
    }
    
    private String loadSongs() {
        Element rootElement = xSongs.getRootElement();
        
        this.songsList = new ArrayList<Song>();
        this.songsElement = rootElement.getChildElements().get(0); // 0 to get the <songs>   

        Elements songs = songsElement.getChildElements();
        String errorSongs = "";
        for (int i = 0; i < songs.size(); i++) {
            Element s = songs.get(i);
            try {
                this.songsList.add(new Song(s.getValue(), s.getAttributeValue("mood"), false, true));
            } catch (Exception ex) {
                errorSongs = errorSongs + s.getValue() + "\n";
            }
        }
        String msg = "The following songs were not found:\n" + errorSongs;
        if (!errorSongs.equals("")) {
            CentralMain.displayErrorDialog(msg);
            
        }
        
        return errorSongs;
        //TODO add for loop to load the lights (if we ever decide to implement it)
    }
    
    protected void addSong(String path, String mood, boolean play) throws Exception {
        Song newSong = new Song(path, mood, play, false);
        this.songsList.add(newSong);
        this.songsElement.appendChild(newSong);
    }
    
    public ArrayList<Song> getSongsWithMood(String mood) {
        ArrayList<Song> filteredArray = new ArrayList<Song>();
        for (int i = 0; i < this.songsList.size(); i++) {
            Song s = this.songsList.get(i);
            String m = s.getMood();
            if (m.equals(mood)) {
                filteredArray.add(s);
            }
        }
        return filteredArray;
    }
    
    public void removeSong(String song, String mood) throws Exception {
        int songIndex = getSongIndex(song, mood);
        int songDocIndex = getSongIndexinDoc(song, mood);
        if (songIndex == -1 || songDocIndex == -1) {
            throw new Exception("unable to delete song Playlsit.removeSong()");
        }
        this.songsList.remove(songIndex);
        this.songsElement.getChildElements().get(songDocIndex).detach();
    }
    
    public void setPlaySong(Song song) throws Exception {
        System.out.println("setting all songs to false");
        int songInPlayIndex = findSongInPlay();
        if (songInPlayIndex == -1) {
            System.err.println("all songs were false");
            return;
        }
        Elements s = this.songsElement.getChildElements();
        s.get(songInPlayIndex).addAttribute(new Attribute("play", "false"));
        
        System.out.println("setting one song to true");
        int nextplaySongIndex = getSongIndexinDoc(song.getName(), song.getMood());
        s.get(nextplaySongIndex).addAttribute(new Attribute("play","true"));
    }

    private int findSongInPlay() {
        Elements s = this.songsElement.getChildElements();
        for (int i = 0; i < s.size(); i++) {
            System.out.println(s.get(i).getAttribute("play").getValue());
            if (s.get(i).getAttribute("play").getValue().equals("true")) {
                return i;
            }
        }
        return -1;
        
    }

    public void overwriteXmlFile() {
        xSongs = this.songsElement.getParent().getDocument();
        try {
            FileOutputStream fos = new FileOutputStream(new File(this.songsXmlPath));
            Serializer serializer = new Serializer(fos);
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(xSongs);
        } catch (IOException ex) {
            CentralMain.displayErrorDialog("There was an error while trying to update the songs list");
            System.out.println(ex);
        }
    }
    
    private int getSongIndex(String name, String mood) {
        System.out.println(name + "||" + mood);
        for (int i = 0; i < this.songsList.size(); i++) {
            if (this.songsList.get(i).getMood().equals(mood) && this.songsList.get(i).getFile().getName().equals(name)) {
                return i;
            }
        }
        
        return -1;
    }
    
    private int getSongIndexinDoc(String name, String mood) {
        Elements s = this.songsElement.getChildElements();
        for (int i = 0; i < s.size(); i++) {
            System.out.println(new File(s.get(i).getValue()).getName() + " || " + s.get(i).getAttribute("mood").getValue().equals(mood));
            if (new File(s.get(i).getValue()).getName().equals(name) && s.get(i).getAttribute("mood").getValue().equals(mood)) {
                return i;
            }
        }
        return -1;
    }
    
    public ArrayList<String> toArray() {
        ArrayList<String> out = new ArrayList<String>();
        for (int i = 0; i < this.songsList.size(); i++) {
            out.add(this.songsList.get(i).getFile() + "(" + this.songsList.get(i).getMood() + ")");
        }
        return out;
    }
    
    public String toString() {
        String out = this.songsElement.toXML();
        return out;
    }
/////////////////////////////////////////////////////
///       INTERNAL CLASSES
//////////////////////////////////////////////////////

    class Song extends Element {
        
        private File file;
        private int id;
        
        Song(String path, String m, boolean p, boolean exist) throws Exception {
            super("song");
            this.appendChild(path);
            this.file = new File(path);
            if (!this.file.isFile() && exist) {
                throw new Exception(path);
            }
            this.addAttribute(new Attribute("mood", m));
            this.addAttribute(new Attribute("play", String.valueOf(p)));
        }
        
        public File getFile() {
            return file;
        }
        
        public String getName() {
            return this.file.getName();
        }
        
        public Song(Element elmnt) {
            super(elmnt);
        }
        
        public String getMood() {
            return this.getAttributeValue("mood");
        }
    }
}
