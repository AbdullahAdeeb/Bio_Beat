/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.io.File;
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
    private Element songsElement;
    private String songsXmlPath;
    private String errorSongs = "";
    
    Playlist(String xmlPath) {
        this.songsXmlPath = xmlPath;
        this.xSongs = CentralMain.loadXml(this.songsXmlPath);
        loadSongs();
    }

    private String loadSongs() {
        Element rootElement = xSongs.getRootElement();
        
        this.songsList = new ArrayList<Song>();
        this.songsElement = rootElement.getChildElements().get(0); // 0 to get the <songs>    otherwise use 1 to get <lights>

        Elements songs = songsElement.getChildElements();
        for (int i = 0; i < songs.size(); i++) {
            Element s = songs.get(i);
            try {
                this.songsList.add(new Song(s.getValue(), s.getAttributeValue("mood")));
            } catch (Exception ex) {
                errorSongs = errorSongs + s.getValue()+"\n";
            }

        }
        return errorSongs;
        //TODO add for loop to load the lights
    }

    public void clearErrors(){
        this.errorSongs = "";
    }
    
    public String getErrors(){
        return this.errorSongs;
    }
    
    private void addSong(String path, String mood) throws Exception {
        Song newSong = new Song(path, mood);
        this.songsList.add(newSong);
        this.songsElement.appendChild(newSong);

        try {
            Serializer serializer = new Serializer(System.out, "ISO-8859-1");
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(xSongs);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
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

    class Song extends Element{

        private File file;
        private int id;

        Song(String path, String m) throws Exception {
            super("song");
            this.appendChild(path);
                this.file = new File(path);
            if (!this.file.isFile()) {
                throw new Exception(path);
            }
            this.addAttribute(new Attribute("mood", m));
            System.out.println("song is read with <" + path + "> and mood <" + m + ">");

        }

        public File getFile() {
            return file;
        }

        public String getMood() {
            return this.getAttributeValue("mood");
        }
    }
}
