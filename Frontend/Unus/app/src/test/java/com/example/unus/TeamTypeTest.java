package com.example.unus;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Abe Demo
 */
public class TeamTypeTest {
    @Test
    public void TeamConstructorCorrect() {
        Team temp = new Team("team1", 1, 0);
        assertEquals(temp.getWins(), 0);
    }

    @Test
    public void TeamConstructorCorrectAlt() {
        Team temp = new Team(1, "team2", null, null);
        assertEquals(temp.getName(), "team2");
    }

}