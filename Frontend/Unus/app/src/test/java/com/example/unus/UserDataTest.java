package com.example.unus;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author Isaac Blandin
 */
public class UserDataTest {

    /**
     * checks if getInstance() returns the same object
     */
    @Test
    public void testGetInstance(){
        UserData userData1 = UserData.getInstance();
        userData1.setUserID(1);
        UserData userData2 = UserData.getInstance();
        //should set both to 2, as it is the same object
        userData2.setUserID(2);

        assertEquals(userData1, userData2);
        assertEquals(userData1.getUserID(), 2);
        assertEquals(userData2.getUserID(), 2);
        assertEquals(userData1.getUserID(), userData2.getUserID());
    }

    /**
     * tests the set and get methods for userID
     */
    @Test
    public void testUserID(){

        int[] testIds = {3, 0, 15};

        for (int id: testIds){
            UserData.getInstance().setUserID(id);
            assertEquals(UserData.getInstance().getUserID(), id);
        }

    }

    /**
     * test the set and get methods for username
     */
    @Test
    public void testUsername(){
        String[] testUsernames = {"Isaac", "Abe", "Sachin"};
        for (String username: testUsernames) {
            UserData.getInstance().setUsername(username);
            assertEquals(UserData.getInstance().getUsername(), username);
        }
    }

    /**
     * test the set and get methods for password
     */
    @Test
    public void testPassword(){
        String[] testPasswords = {"12345", "password", "goClones"};
        for (String password: testPasswords){
            UserData.getInstance().setPassword(password);
            assertEquals(UserData.getInstance().getPassword(), password);
        }
    }

    /**
     * test the set and get methods for role
     */
    @Test
    public void testRole(){
        String[] testRoles = {"guest", "user", "admin"};
        for (String role: testRoles){
            UserData.getInstance().setPassword(role);
            assertEquals(UserData.getInstance().getPassword(), role);
        }
    }

    /**
     * test the set, get, and increment methods for gamesPlayed
     */
    @Test
    public void testGamesPlayed(){
        UserData.getInstance().setGamesPlayed(3);
        assertEquals(UserData.getInstance().getGamesPlayed(), 3);
        UserData.getInstance().incrementGamesPlayed();
        assertEquals(UserData.getInstance().getGamesPlayed(), 4);
    }

    /**
     * test the set, get, and increment methods for gamesWon
     */
    @Test
    public void testGamesWon(){
        UserData.getInstance().setGamesWon(5);
        assertEquals(UserData.getInstance().getGamesWon(), 5);
        UserData.getInstance().incrementGamesWon();
        assertEquals(UserData.getInstance().getGamesWon(), 6);
    }

    /**
     * test the set and get methods for FriendsList
     */
    @Test
    public void testFriendsList(){
        Friend f1 = new Friend(1, "Isaac");
        Friend f2 = new Friend(2, "Abe");
        Friend f3 = new Friend(3, "Sachin");

        Friend[] friendList = {f1, f2, f3};
        UserData.getInstance().setFriendsList(friendList);

        Friend[] friendListCopy = UserData.getInstance().getFriendsList();
        for(int i = 0; i < friendListCopy.length; i++){
            assertEquals(friendListCopy[i], friendList[i]);
        }
    }

    /**
     * test the set and get methods for receivedRequests
     */
    @Test
    public void testReceivedRequests(){
        Friend f1 = new Friend(1, "Isaac");
        Friend f2 = new Friend(2, "Abe");
        Friend f3 = new Friend(3, "Sachin");

        Friend[] receivedRequests = {f1, f2, f3};
        UserData.getInstance().setReceivedRequestsList(receivedRequests);

        Friend[] receivedRequestsCopy = UserData.getInstance().getReceivedRequests();
        for(int i = 0; i < receivedRequestsCopy.length; i++){
            assertEquals(receivedRequestsCopy[i], receivedRequests[i]);
        }
    }

    /**
     * test the set and get methods for receivedRequests
     */
    @Test
    public void testSentRequests(){
        Friend f1 = new Friend(1, "Isaac");
        Friend f2 = new Friend(2, "Abe");
        Friend f3 = new Friend(3, "Sachin");

        Friend[] sentRequests = {f1, f2, f3};
        UserData.getInstance().setReceivedRequestsList(sentRequests);

        Friend[] sentRequestsCopy = UserData.getInstance().getReceivedRequests();
        for(int i = 0; i < sentRequestsCopy.length; i++){
            assertEquals(sentRequestsCopy[i], sentRequests[i]);
        }
    }



}
