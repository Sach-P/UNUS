package com.example.unus;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * tests all of the methods in the Friend object
 *
 * @author Isaac Blandin
 */
public class FriendTypeTest {

    /**
     * tests all of the constructors
     */
    @Test
    public void testConstructors(){
        Friend f1 = new Friend(12, "friend1");
        assertEquals(f1.getUsername(), "friend1");
        assertEquals(f1.getUserID(), 12);
        assertNull(f1.getPassword());
        assertEquals(f1.getGamesPlayed(), 0);
        assertEquals(f1.getGamesWon(), 0);

        Friend f2 = new Friend(14, "friend2", 3, 2);
        assertEquals(f2.getUserID(), 14);
        assertEquals(f2.getUsername(), "friend2");
        assertNull(f2.getPassword());
        assertEquals(f2.getGamesPlayed(), 3);
        assertEquals(f2.getGamesWon(), 2);

        Friend f3 = new Friend(16, "friend3", "password", 4, 3);
        assertEquals(f3.getUserID(), 16);
        assertEquals(f3.getUsername(), "friend3");
        assertEquals(f3.getPassword(), "password");
        assertEquals(f3.getGamesPlayed(), 4);
        assertEquals(f3.getGamesWon(), 3);
    }

    /**
     * tests the getUsername method
     */
    @Test
    public void testGetUserName(){
        Friend f = new Friend(16, "alex", "password", 4, 3);
        assertEquals(f.getUsername(), "alex");
    }

    /**
     * tests the getUserID method
     */
    @Test
    public void testGetUserID(){
        Friend f = new Friend(420, "alex", "password", 4, 3);
        assertEquals(f.getUserID(), 420);
    }

    /**
     * tests the getGamesPlayed method
     */
    @Test
    public void testGetGamesPlayer(){
        Friend f = new Friend(420, "alex", "password", 7, 3);
        assertEquals(f.getGamesPlayed(), 7);
    }

    /**
     * tests the getGamesWon method
     */
    @Test
    public void testGamesWon(){
        Friend f = new Friend(420, "alex", "password", 7, 23);
        assertEquals(f.getGamesWon(), 23);
    }

    /**
     * tests the getGamesWon method
     */
    @Test
    public void testGetPassword(){
        Friend f = new Friend(420, "alex", "12345", 7, 23);
        assertEquals(f.getPassword(), "12345");
    }
}