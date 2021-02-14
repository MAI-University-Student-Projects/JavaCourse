package org.mai.dep810.collections_lesson.social_network;

import java.util.*;

public class SocialNetworkImpl implements SocialNetwork {

    Map<String, Set<String>> _user_map;

    public SocialNetworkImpl() {
        _user_map = new HashMap<>();
    }

    @Override
    public void addPerson(String name) {
        _user_map.put(name, new HashSet<>());
    }

    @Override
    public void addConnection(String from, String to) {
        if(!(_user_map.containsKey(from) && _user_map.containsKey(to)) || from.equals(to))
            throw new UserNotFoundException("User not found");
        _user_map.get(from).add(to);
        _user_map.get(to).add(from);
    }

    @Override
    public List<String> getUsers() {
        return new ArrayList<>(_user_map.keySet());
    }

    @Override
    public List<String> getFriends(int level, String user) {
        if(!(_user_map.containsKey(user)))
            throw new UserNotFoundException("User not found");
        Set<String> result = new HashSet<>(_user_map.get(user));
        if(level > 1) {
            for(String friend : _user_map.get(user))
                result.addAll(getFriends(level - 1, friend));
            result.remove(user);
        }
        return new ArrayList<>(result);
    }
}
