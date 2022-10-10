package com._an_5.UNUS.Users;


import com._an_5.UNUS.Friends.Friend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Iterator;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;

    public User login(String username, String password){
        User user = repo.findByUsernameAndPassword(username, password);
        return user;
    }

    public void sendFriendRequest(int id, Friend friend){
        User currUser = repo.findById(id);
        User friendUser = repo.findById(friend.getFriendId());
        if(friend != null){
            currUser.addSentFriendRequests(friend);
            friendUser.addReceivedFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
        }
        repo.save(currUser);
        repo.save(friendUser);
    }

    public void acceptOrDeclineFriendRequest(int id, Friend request){
        User currUser = repo.findById(id);
        User friendUser = repo.findById(request.getFriendId());
        Iterator<Friend> it = currUser.getReceivedFriendRequests().listIterator();
        while(it.hasNext()){
            Friend friendRequest = it.next();
            if(friendRequest.getFriendId() == request.getFriendId()){
                if(request.getStatus().equals("accepted")){
                    it.remove();
                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
                    friendRequest.setStatus("friend");
                    currUser.addFriend(friendRequest);
                    friendUser.addFriend(new Friend(currUser.getId(), currUser.getUsername(), "friend"));
                }
                else if(request.getStatus().equals("declined")){
                    it.remove();
                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
                }
                break;
            }
        }
        repo.save(currUser);
        repo.save(friendUser);
    }


    public void removeFriend(int id, Friend friend){
        User currUser = repo.findById(id);
        User friendUser = repo.findById(friend.getFriendId());
        Iterator<Friend> it = currUser.getFriends().listIterator();
        while(it.hasNext()){
            Friend currFriend = it.next();
            if(friend.getFriendId() == currFriend.getFriendId()){
                it.remove();
                friendUser.removeFriend(new Friend(currUser.getId(), currUser.getUsername()));
            }
        }
        repo.save(currUser);
        repo.save(friendUser);
    }


}
