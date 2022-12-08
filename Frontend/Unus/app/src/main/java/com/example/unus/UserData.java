package com.example.unus;

/**
 * Singleton class designed to store user data which is accessible to all activities and fragments
 *
 * @author Isaac Blandin
 */
public class UserData {

    private static UserData singleInstance;

    private int userID;
    private String username;
    private String password;

    private Friend[] friendsList;
    private Friend[] sentRequests;
    private Friend[] receivedRequests;

    private String role;
    private int gamesPlayed;
    private int gamesWon;

    /**
     * empty constructor called by getInstance() internally
     */
    private UserData(){

    }

    /**
     * allows user to access the instance storing the user data. creates an instance if not called before
     * @return instance containing user data
     */
    public static UserData getInstance(){
        if (singleInstance == null){
            singleInstance = new UserData();
        }
        return singleInstance;
    }

    /**
     * returns the current user's id number
     * @return user's id number
     */
    public int getUserID(){
        return userID;
    }

    /**
     * sets the current user's id number to a given int value
     * @param userID user's id number
     */
    public void setUserID(int userID){
        this.userID = userID;
    }

    /**
     * returns the current user's username
     * @return user's username
     */
    public String getUsername (){
        return username;
    }

    /**
     * sets the current user's username to given String value
     * @param username user's username
     */
    public void setUsername(String username){
        this.username = username;
    }

    /**
     * returns the current user's password
     * @return user's password
     */
    public String getPassword(){
        return password;
    }

    /**
     * sets the current user's password to a given String value
     * @param password user's password
     */
    public void setPassword(String password){
        this.password = password;
    }

    /**
     * sets the current user's role to a given role
     * @param role string describing the user's role
     */
    public void setRole(String role) {this.role = role;}

    /**
     * returns the current user's role
     * @return String describing the user's role
     */
    public String getRole() { return role;}

    /**
     * returns current user's games played stat
     * @return games played
     */
    public int getGamesPlayed(){
        return gamesPlayed;
    }

    /**
     * sets the current user's games played stat to a given int value
     * @param gamesPlayed games played by user
     */
    public void setGamesPlayed(int gamesPlayed){
        this.gamesPlayed = gamesPlayed;
    }

    /**
     * adds a game played to the current user's games played stat
     */
    public void incrementGamesPlayed(){
        gamesPlayed++;
    }

    /**
     * returns the current user's games won stat
     * @return games won
     */
    public int getGamesWon(){
        return gamesWon;
    }

    /**
     * sets the current user's games won stat to a given int value
     * @param gamesWon games won by user
     */
    public void setGamesWon(int gamesWon){
        this.gamesWon = gamesWon;
    }

    /**
     * adds a game won to the current user's games won stat
     */
    public void incrementGamesWon(){
        gamesWon++;
    }

    /**
     * copys over a given list of Friend objects into the friendsList array
     * @param friendsList list of Friends
     */
    public void setFriendsList(Friend[] friendsList){
        this.friendsList = new Friend[friendsList.length];
        for (int i = 0; i < friendsList.length; i++){
            this.friendsList[i] = friendsList[i];
        }
    }

    /**
     * returns the friendsList array of Friend objects
     * @return friends list
     */
    public Friend[] getFriendsList(){
        return friendsList;
    }

    /**
     * Returns the sent friend requests arraylist
     * @return sentFriends list
     */
    public Friend[] getSentRequests() { return sentRequests; }


    /**
     * Returns the received friend requests arraylist
     * @return receivedFriends list
     */
    public Friend[] getReceivedRequests() { return receivedRequests; }

    /**
     * copys over a given list of Friend objects into the sentRequests array
     * @param requestsList list of sent requests
     */
    public void setSentRequestsList(Friend[] requestsList){
        this.sentRequests = new Friend[requestsList.length];
        for (int i = 0; i < requestsList.length; i++){
            this.sentRequests[i] = requestsList[i];
        }
    }

    /**
     * copys over a given list of Friend objects into the recievedRequests array
     * @param requestsList list of received requests
     */
    public void setReceivedRequestsList(Friend[] requestsList){
        this.receivedRequests = new Friend[requestsList.length];
        for (int i = 0; i < requestsList.length; i++){
            this.receivedRequests[i] = requestsList[i];
        }
    }
}
