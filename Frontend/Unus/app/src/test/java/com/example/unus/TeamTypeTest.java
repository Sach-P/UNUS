package com.example.unus;

import org.junit.Test;

import static org.junit.Assert.*;

import java.util.ArrayList;

/**
 * tests the methods in the Team Object
 *
 * @author Isaac Blandin
 */
public class TeamTypeTest {

    /**
     * tests the primary constructor
     */
    @Test
    public void testConstructor() {
        Team t = new Team("team1", 1, 0);
        assertEquals(t.getName(), "team1");
        assertEquals(t.getId(), 1);
        assertEquals(t.getWins(), 0);
    }

    /**
     * tests the alternative constructor
     */
    @Test
    public void testConstructorAlt() {
        Friend f1 = new Friend(1, "leader");
        Friend f2 = new Friend(2, "notLeader");
        ArrayList<Friend> friends = new ArrayList<>();
        friends.add(f1);
        friends.add(f2);

        Team t = new Team(1, "weFeastWeEat", f1, friends);
        assertEquals(t.getName(), "weFeastWeEat");
        assertEquals(t.getId(), 1 );
        assertEquals(t.getLeader(), f1);
        assertEquals(t.getPlayers(), friends);
    }

    /**
     * tests the getName method
     */
    @Test
    public void testGetName(){
        Team t = new Team("daBoys", 23, 7000);
        assertEquals(t.getName(), "daBoys");
    }

    /**
     * tests the getId method
     */
    @Test
    public void testGetId(){
        Team t = new Team("daBoys", 23, 7000);
        assertEquals(t.getId(), 23);
    }

    /**
     * tests the getWins method
     */
    @Test
    public void testGetWins(){
        Team t = new Team("daBoys", 23, 7000);
        assertEquals(t.getWins(), 7000);
    }

    /**
     * tests the getLeader method
     */
    @Test
    public void testGetLeader(){
        Friend f1 = new Friend(1, "leader");

        Team t = new Team(1, "backAtItAgain", f1, null);
        assertEquals(t.getLeader(), f1);
    }

    /**
     * test getPlayers method
     */
    @Test
    public void testGetPlayers(){

        ArrayList<Friend> friends = new ArrayList<>();
        friends.add(new Friend(1, "leader"));
        friends.add(new Friend(2, "notLeader"));

        Team t = new Team(1, "runningOutOfFunnyNames", null, friends);
        assertEquals(t.getPlayers(), friends);
    }
}