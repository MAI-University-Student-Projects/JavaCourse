package org.mai.dep810.collections_lesson.social_network;

import org.junit.*;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.util.List;

public class SocialNetworkImplTest {

    SocialNetwork soc_net;
    String user_0 = "Senya";
    String user_1 = "Vova";

    @Before
    public void setUp() throws Exception {
        soc_net = new SocialNetworkImpl();
        soc_net.addPerson(user_0);
        soc_net.addPerson(user_1);
    }

    @After
    public void tearDown() throws Exception {
        soc_net = null;
    }

    @Test
    public void addPerson() {
        List<String> users = soc_net.getUsers();
        assertThat(users, hasItems(user_0, user_1));

        String user_2 = "Petya";
        assertThat(users, not(hasItem(user_2)));
        soc_net.addPerson(user_2);
        users = soc_net.getUsers();
        assertThat(users, hasItem(user_2));
    }

    @Test
    public void addConnection() {
        String user_2 = "Petya";
        try {
            soc_net.addConnection(user_2, user_0);
        }
        catch (UserNotFoundException ex) {
            assertEquals("User not found", ex.getMessage());
        }
        soc_net.addPerson(user_2);
        soc_net.addConnection(user_2, user_0);
        assertThat(soc_net.getFriends(1, user_2), hasItem(user_0));
        assertThat(soc_net.getFriends(1, user_2), not(hasItem(user_1)));
    }

    @Test()
    public void getFriends() {
        String user_2 = "Petya";
        String user_3 = "Artem";
        String user_4 = "Andrey";
        String user_5 = "Stepan";
        soc_net.addPerson(user_2);
        soc_net.addPerson(user_3);
        soc_net.addPerson(user_4);
        soc_net.addPerson(user_5);
        soc_net.addConnection(user_2, user_0);
        soc_net.addConnection(user_0, user_1);
        soc_net.addConnection(user_1, user_3);
        soc_net.addConnection(user_3, user_4);
        soc_net.addConnection(user_3, user_5);
        soc_net.addConnection(user_0, user_5);
        List<String> thrd_lvl_frnds_to_0 = soc_net.getFriends(3, user_0);
        assertThat(thrd_lvl_frnds_to_0, not(hasItem(user_0)));
        assertThat(thrd_lvl_frnds_to_0, hasItem(user_4));
    }
}