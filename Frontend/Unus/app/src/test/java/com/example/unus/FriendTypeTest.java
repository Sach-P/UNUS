package com.example.unus;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Abe Demo
 */
public class FriendTypeTest {
    @Test
    public void FriendConstructorCorrect() {
        Friend friend = new Friend(12, "newFriend");
        assertEquals(friend.getUsername(), "newFriend");
        assertEquals(friend.getUserID(), 12);
    }
}