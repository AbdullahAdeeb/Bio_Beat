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
import nu.xom.Node;
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
                this.songsList.add(new Song(s.getValue(), s.getAttributeValue("mood"), true));
            } catch (Exception ex) {
                errorSongs = errorSongs + s.getValue() + "\n";
                
            }

        }
        return errorSongs;
        //TODO add for loop to load the lights
    }

    public void clearErrors() {
        this.errorSongs = "";
    }

    public String getErrors() {
        return this.errorSongs;
    }

    protected void addSong(String path, String mood) throws Exception {
        Song newSong = new Song(path, mood, false);
        this.songsList.add(newSong);
        this.songsElement.appendChild(newSong);
        createNewXml();

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

    void removeSong(String song, String mood) {
        int songIndex = getSongIndex(song, mood);
        int songDocIndex = getSongIndexinDoc(song, mood);
        if (songIndex == -1 || songDocIndex == -1) {
            //TODO add an error dialog that can't remove the song
            return;
        }
        this.songsList.remove(songIndex);
        this.songsElement.getChildElements().get(songDocIndex).detach();
        createNewXml();
    }

    private void createNewXml() {


        xSongs = this.songsElement.getParent().getDocument();
        try {
            System.out.println("1");
            FileOutputStream fos = new FileOutputStream(new File(this.songsXmlPath));
            Serializer serializer = new Serializer(fos);
            serializer.setIndent(4);
            serializer.setMaxLength(64);
            serializer.write(xSongs);
            System.out.println("done");
        } catch (IOException ex) {
            System.err.println(ex);
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

    class Song extends Element {

        private File file;
        private int id;

        Song(String path, String m, boolean exist) throws Exception {
            super("song");
            this.appendChild(path);
            this.file = new File(path);
            if (!this.file.isFile() && exist) {
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
