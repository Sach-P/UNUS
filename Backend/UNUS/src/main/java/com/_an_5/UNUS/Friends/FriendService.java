package com._an_5.UNUS.Friends;

import com._an_5.UNUS.Users.User;
import com._an_5.UNUS.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;

@Service
public class FriendService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;

    private String success = "{\"message\":\"passed\"}";
    private String failure = "{\"message\":\"failed\"}";

    public String sendFriendRequest(int id, int friendId){
        User requested = userRepository.findById(friendId);
        User requester = userRepository.findById(id);
        if(requested != null && requester != null){
            Friend friend1 = new Friend(id, "pending", requester.getUsername());
            friend1.setRequestedUser(requested);

            Friend friend2 = new Friend(friendId, "pending", requested.getUsername());
            friend2.setUserRequest(requester);

            friendRepository.save(friend1);
            friendRepository.save(friend2);
            return success;
        }
        return failure;

    }

    public String acceptOrDeclineFriendRequest(int id, int friendId, String status){
        User requested = userRepository.findById(id);
        User requester = userRepository.findById(friendId);
        Friend friend1 = null;
        Friend friend2 = null;

        Iterator<Friend> it = requested.getReceivedFriendRequests().iterator();
        while(it.hasNext()){
            Friend friendRequest = it.next();
            if(friendRequest.getFriendId() == friendId){
                friend1 = friendRequest;
                break;
            }
        }

        it = requester.getSentFriendRequests().iterator();
        while(it.hasNext()){
            Friend friendRequest = it.next();
            if(friendRequest.getFriendId() == id){
                friend2 = friendRequest;
                break;
            }
        }

        if(friend1 != null && friend2 != null){
            if(status.equals("accepted")){
                friend1.setFriend(requested);
                friend1.setStatus("friend");
                friend1.setRequestedUser(null);

                friend2.setFriend(requester);
                friend2.setStatus("friend");
                friend2.setUserRequest(null);

                friendRepository.save(friend1);
                friendRepository.save(friend2);

                return "{\"message\":\"accepted\"}";

            }
            else{
                friendRepository.deleteById(friend1.getId());
                friendRepository.deleteById(friend2.getId());
                return "{\"message\":\"declined\"}";
            }
        }

        return failure;
    }


    public String removeFriend(int id, int friendId){
        User user1 = userRepository.findById(id);
        User user2 = userRepository.findById(friendId);
        Friend friend1 = null;
        Friend friend2 = null;

        Iterator<Friend> it = user1.getFriends().iterator();
        while(it.hasNext()){
            Friend friend = it.next();
            if(friend.getFriendId() == friendId){
                friend1 = friend;
                it.remove();
                break;
            }
        }

        it = user2.getFriends().iterator();
        while(it.hasNext()){
            Friend friend = it.next();
            if(friend.getFriendId() == id){
                friend2 = friend;
                it.remove();
                break;
            }
        }

        if(friend1 != null && friend2 != null){
            friend1.setFriend(null);
            friend2.setFriend(null);
            friendRepository.deleteById(friend1.getId());
            friendRepository.deleteById(friend2.getId());
            return success;
        }

        return failure;

    }

}
