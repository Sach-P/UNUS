package com.example.unus;
import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * junit class used to test all of the methods of Card Object
 */
@RunWith(AndroidJUnit4.class)
public class CardTest {

    Context context;

    /**
     * set up context to be used throughout this test class
     */
    @Before
    public void setupContext(){
        context = InstrumentationRegistry.getInstrumentation().getContext();
    }

    /**
     * tests the three different constructors of Card
     * @throws JSONException
     */
    @Test
    public void testConstructors() throws JSONException {
        //test random card generation
        Card c1 = new Card(context);
        assertNotNull(c1.getColor());
        assertNotNull(c1.getRank());

        //test pre-selected card constructor
        Card c2 = new Card(CardRank.REVERSE, CardColor.RED, context);
        assertEquals(c2.getRank(), CardRank.REVERSE);
        assertEquals(c2.getColor(), CardColor.RED);

        //test jsonObject constructor
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("rank", CardRank.SKIP.toString());
        jsonObject.put("color", CardColor.PURPLE.toString());

        Card c3 = new Card(jsonObject, context);
        assertEquals(c3.getRank(), CardRank.SKIP);
        assertEquals(c3.getColor(), CardColor.PURPLE);
    }

    /**
     * tests the cardPlayable method of Card
     */
    @Test
    public void testCardPlayable(){
        Card c1 = new Card(CardRank.TWO, CardColor.RED, context);

        //test if rank matching works
        Card c2 = new Card(CardRank.TWO, CardColor.YELLOW, context);
        assertTrue(c1.cardPlayable(c2));

        //test if color matching works
        Card c3 = new Card(CardRank.REVERSE, CardColor.RED, context);
        assertTrue(c1.cardPlayable(c3));

        //test if wild card works at all times
        Card c4 = new Card(CardRank.CHANGE_COLOR, CardColor.WILD, context);
        assertTrue(c4.cardPlayable(c1));
    }

    /**
     * tests the set and get color methods
     */
    @Test
    public void testColor(){
        Card c1 = new Card(CardRank.EIGHT, CardColor.GREEN, context);
        assertEquals(c1.getColor(), CardColor.GREEN);
        c1.setColor(CardColor.RED);
        assertEquals(c1.getColor(), CardColor.RED);
    }

    /**
     * tests the getRank method
     */
    @Test
    public void testGetRank(){
        Card c1 = new Card(CardRank.DRAW_FOUR, CardColor.WILD, context);
        assertEquals(c1.getRank(), CardRank.DRAW_FOUR);
    }

    @Test
    public void testToJsonObject() throws JSONException {
        Card c1 = new Card(CardRank.TWO, CardColor.YELLOW, context);
        JSONObject jsonObject = c1.toJsonObject();
        assertEquals(jsonObject.getString("rank"), CardRank.TWO.toString());
        assertEquals(jsonObject.getString("color"), CardColor.YELLOW.toString());
    }


}
