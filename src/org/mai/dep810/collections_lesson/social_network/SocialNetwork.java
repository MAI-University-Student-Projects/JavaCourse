package org.mai.dep810.collections_lesson.social_network;

import java.util.List;

/* Социальная сеть */
public interface SocialNetwork {

    /* Добавить участника */
    void addPerson(String name);

    /* Добавить линк ("друга") to для участника from */
    void addConnection(String from, String to);

    /* Получить список друзей до уровня level
       level = 1 - непосредственные "друзья",
       level = 2 - непосредственные "друзья" + "друзья" "друзей"
       итд.
     */
    List<String> getUsers();
    List<String> getFriends(int level, String user);
}