/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MainPackage;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author lenovo212
 */
public class PlaylistTest {
    
    public static String TESTCreate_SONG_XML_PATH = "res/TestCreate_BioBeat_songs.xml";


    @Test
    public void testPlayListConstructor() throws Exception {
        System.out.println("testing playlist constructor >>");
        
        Playlist instance = new Playlist(TESTCreate_SONG_XML_PATH);
        String expectedString = "<songs>\n"
                + "        <song name=\"beethoven\" mood=\"relax\" id=\"1\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"resting\" id=\"3\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"resting\" id=\"4\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"excited\" id=\"5\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"excited\" id=\"6\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"angry\" id=\"7\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"angry\" id=\"8\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"cooldown\" id=\"9\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"cooldown\" id=\"10\">d:/songs/beethoven.mp3</song>\n"
                + "        <song mood=\"resting\">D:/songs/22.mp3</song>\n"
                + "        <song mood=\"excited\">d:/songs/5.mp3</song>\n"
                + "    </songs>";
        
        assertNotNull(instance.toString());
        assertEquals(expectedString, instance.toString());
        
        ArrayList<String> expectedArray = new ArrayList<>();
        expectedArray.add("d:\\songs\\beethoven.mp3(relax)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("D:\\songs\\22.mp3(resting)");
        expectedArray.add("d:\\songs\\5.mp3(excited)");
        
        ArrayList<String> actualArray = instance.toArray();
        assertArrayEquals(expectedArray.toArray(), actualArray.toArray());
    }

    /**
     * Test of addSong method, of class Playlist.
     */
    @Test
    public void testAddSong() {
        System.out.println("testing playlist addSong() >>");
        
        String newSongPath = "d:\\songs\\2.mp3";
        String newSongMood = "angry";
        
        Playlist instance = new Playlist(TESTCreate_SONG_XML_PATH);
        try {
            instance.addSong(newSongPath, newSongMood,false);
        } catch (Exception ex) {
            fail("add song threw an exception");
        }
        System.out.println(instance.toString());
        
        String expectedString = "<songs>\n"
                + "        <song name=\"beethoven\" mood=\"relax\" id=\"1\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"resting\" id=\"3\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"resting\" id=\"4\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"excited\" id=\"5\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"excited\" id=\"6\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"angry\" id=\"7\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"angry\" id=\"8\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"cooldown\" id=\"9\">d:/songs/beethoven.mp3</song>\n"
                + "        <song name=\"foobar\" mood=\"cooldown\" id=\"10\">d:/songs/beethoven.mp3</song>\n"
                + "        <song mood=\"resting\">D:/songs/22.mp3</song>\n"
                + "        <song mood=\"excited\">d:/songs/5.mp3</song>\n"
                + "    <song mood=\"angry\">d:\\songs\\2.mp3</song></songs>";
        
        assertNotNull(instance.toString());
        assertEquals(expectedString, instance.toString());
        
        ArrayList<String> expectedArray = new ArrayList<>();
        expectedArray.add("d:\\songs\\beethoven.mp3(relax)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("D:\\songs\\22.mp3(resting)");
        expectedArray.add("d:\\songs\\5.mp3(excited)");
        expectedArray.add(newSongPath + "(" + newSongMood + ")"); //new added song

        ArrayList<String> actualArray = instance.toArray();
        assertArrayEquals(expectedArray.toArray(), actualArray.toArray());
        
    }


    /**
     * Test of removeSong method, of class Playlist.
     */
    @Test
    public void testRemoveSong() {
        System.out.println("testing playlist removeSong() >>");
        
        String newSongName = "22.mp3";
        String newSongMood = "resting";
        
        Playlist instance = new Playlist(TESTCreate_SONG_XML_PATH);
        try {
            instance.removeSong(newSongName, newSongMood);
        } catch (Exception ex) {
            fail("removeSong() failed and threw an exception");
        }
        System.out.println(instance.toString());
        
        String expectedString = "<songs>\n" +
"        <song name=\"beethoven\" mood=\"relax\" id=\"1\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"resting\" id=\"3\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"resting\" id=\"4\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"excited\" id=\"5\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"excited\" id=\"6\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"angry\" id=\"7\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"angry\" id=\"8\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"cooldown\" id=\"9\">d:/songs/beethoven.mp3</song>\n" +
"        <song name=\"foobar\" mood=\"cooldown\" id=\"10\">d:/songs/beethoven.mp3</song>\n" +
"        \n" +
"        <song mood=\"excited\">d:/songs/5.mp3</song>\n" +
"    </songs>";
        
        assertNotNull(instance.toString());
        assertEquals(expectedString, instance.toString());
        
        ArrayList<String> expectedArray = new ArrayList<>();
        expectedArray.add("d:\\songs\\beethoven.mp3(relax)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(resting)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(excited)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(angry)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("d:\\songs\\beethoven.mp3(cooldown)");
        expectedArray.add("d:\\songs\\5.mp3(excited)");

        ArrayList<String> actualArray = instance.toArray();
        assertArrayEquals(expectedArray.toArray(), actualArray.toArray());
    }
}