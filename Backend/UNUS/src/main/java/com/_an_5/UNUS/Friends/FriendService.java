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
            Friend friend = new Friend();
            friend.setUserRequest(requested);
            friend.setRequestedUser(requester);
            friendRepository.save(friend);
            return success;
        }
        return failure;

    }

    public String acceptOrDeclineFriendRequest(int id, int friendRelationId, String status){
//        User currUser = repo.findById(id);
//        User friendUser = repo.findById(request.getFriendId());
//        Iterator<Friend> it = currUser.getReceivedFriendRequests().listIterator();
//        while(it.hasNext()){
//            Friend friendRequest = it.next();
//            if(friendRequest.getFriendId() == request.getFriendId()){
//                if(request.getStatus().equals("accepted")){
//                    it.remove();
//                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
//                    friendRequest.setStatus("friend");
//                    currUser.addFriend(friendRequest);
//                    friendUser.addFriend(new Friend(currUser.getId(), currUser.getUsername(), "friend"));
//                }
//                else if(request.getStatus().equals("declined")){
//                    it.remove();
//                    friendUser.removeSentFriendRequests(new Friend(currUser.getId(), currUser.getUsername()));
//                }
//                break;
//            }
//        }
//        repo.save(currUser);
//        repo.save(friendUser);

        Friend request = friendRepository.findById(friendRelationId);

        if(status.equals("accepted")){
            request.addFriend(request.getUserRequest());
            request.addFriend(request.getRequestedUser());
            request.setStatus(status);
            request.setRequestedUser(null);
            request.setUserRequest(null);
            friendRepository.save(request);
            return "{\"message\":\"accepted\"}";
        }
        else{
            friendRepository.deleteById(friendRelationId);
            return "{\"message\":\"declined\"}";
        }
    }

//
//    public void removeFriend(int id, Friend friend){
//        User currUser = repo.findById(id);
//        User friendUser = repo.findById(friend.getFriendId());
//        Iterator<Friend> it = currUser.getFriends().listIterator();
//        while(it.hasNext()){
//            Friend currFriend = it.next();
//            if(friend.getFriendId() == currFriend.getFriendId()){
//                it.remove();
//                friendUser.removeFriend(new Friend(currUser.getId(), currUser.getUsername()));
//            }
//        }
//        repo.save(currUser);
//        repo.save(friendUser);
//    }
}
