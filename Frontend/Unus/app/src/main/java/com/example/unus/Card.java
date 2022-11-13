package com.example.unus;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

/**
 * Object used to represent a playing card for this game. Stores rank and color along with providing methods used for gameplay
 *
 * @author Isaac Blandin
 */
public class Card {

    //declare card properties
    CardRank rank;
    CardColor color;
    //declare context needed to get image assets
    Context context;

    /**
     * constructor to create a card of given rank and color
     *
     * @param rank
     * @param color
     */
    public Card(CardRank rank, CardColor color, Context context) {
        this.rank = rank;
        this.color = color;
        this.context = context;
    }

    /**
     * constructor to create a random card based on weights
     */
    public Card(Context context) {
        this.color = randomColor();
        this.rank = randomRank(color);
        this.context = context;
    }

    /**
     * checks whether a card can be played on top of another card.
     * @param card card to be compared to the current object
     * @return card can be played
     */
    public boolean cardPlayable (Card card){
        return card.getColor() == color || card.getRank() == rank || color == CardColor.WILD;
    }

    /**
     * changes the card color to a given color. Used for wild cards when changing colors.
     * @param color card color
     */
    public void setColor(CardColor color){
        this.color = color;
    }

    /**
     * returns the color of the current card
     * @return card's color
     */
    public CardColor getColor() {
        return color;
    }

    /**
     * returns the rank of the current card
     * @return card's rank
     */
    public CardRank getRank(){
        return rank;
    }

    /**
     * determines a random color for the card based on weights
     * @return card color
     */
    private CardColor randomColor() {
        int rand = (int) (Math.random() * 13);
        int choice = rand / 3;
        switch (choice) {
            case 0:
                return CardColor.RED;
            case 1:
                return CardColor.GREEN;
            case 2:
                return CardColor.YELLOW;
            case 3:
                return CardColor.PURPLE;
            case 4:
                return CardColor.WILD;
        }
        throw new RuntimeException("The code is messed up");
    }

    /**
     * determines a random rank for the card based on weights and a given color
     * @param color color of card to determine ranks possible
     * @return card rank
     */
    private CardRank randomRank(CardColor color) {
        if (color == CardColor.WILD) {
            if (Math.random() >= 0.5) {
                return CardRank.CHANGE_COLOR;
            } else {
                return CardRank.DRAW_FOUR;
            }
        } else {
            int rand = (int) (Math.random() * 13);
            switch (rand) {
                case 0:
                    return CardRank.ZERO;
                case 1:
                    return CardRank.ONE;
                case 2:
                    return CardRank.TWO;
                case 3:
                    return CardRank.THREE;
                case 4:
                    return CardRank.FOUR;
                case 5:
                    return CardRank.FIVE;
                case 6:
                    return CardRank.SIX;
                case 7:
                    return CardRank.SEVEN;
                case 8:
                    return CardRank.EIGHT;
                case 9:
                    return CardRank.NINE;
                case 10:
                    return CardRank.SKIP;
                case 11:
                    return CardRank.REVERSE;
                case 12:
                    return CardRank.DRAW_TWO;
            }
        }
        throw new RuntimeException("code is broke");
    }

    /**
     * returns the drawable associated with the current card object based on its color and rank
     *
     * @return image asset of the given card
     */
    public Drawable getImage(){
        Drawable drawable;

        drawable = ContextCompat.getDrawable(context, R.drawable.ic_ub);

        switch (color){

            case RED:
                switch (rank){
                    case ZERO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r0);
                        break;
                    case ONE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r1);
                        break;
                    case TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r2);
                        break;
                    case THREE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r3);
                        break;
                    case FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r4);
                        break;
                    case FIVE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r5);
                        break;
                    case SIX:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r6);
                        break;
                    case SEVEN:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r7);
                        break;
                    case EIGHT:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r8);
                        break;
                    case NINE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_r9);
                        break;
                    case DRAW_TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_rd);
                        break;
                    case REVERSE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_rr);
                        break;
                    case SKIP:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_rs);
                        break;
                    case DRAW_FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_rwd);
                        break;
                    case CHANGE_COLOR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_rwc);
                        break;
                }
                break;

            case PURPLE:
                switch (rank){
                    case ZERO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p0);
                        break;
                    case ONE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p1);
                        break;
                    case TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p2);
                        break;
                    case THREE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p3);
                        break;
                    case FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p4);
                        break;
                    case FIVE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p5);
                        break;
                    case SIX:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p6);
                        break;
                    case SEVEN:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p7);
                        break;
                    case EIGHT:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p8);
                        break;
                    case NINE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_p9);
                        break;
                    case DRAW_TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_pd);
                        break;
                    case REVERSE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_pr);
                        break;
                    case SKIP:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_ps);
                        break;
                    case DRAW_FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_pwd);
                        break;
                    case CHANGE_COLOR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_pwc);
                        break;
                }
                break;

            case GREEN:
                switch (rank){
                    case ZERO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g0);
                        break;
                    case ONE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g1);
                        break;
                    case TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g2);
                        break;
                    case THREE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g3);
                        break;
                    case FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g4);
                        break;
                    case FIVE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g5);
                        break;
                    case SIX:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g6);
                        break;
                    case SEVEN:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g7);
                        break;
                    case EIGHT:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g8);
                        break;
                    case NINE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_g9);
                        break;
                    case DRAW_TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_gd);
                        break;
                    case REVERSE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_gr);
                        break;
                    case SKIP:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_gs);
                        break;
                    case DRAW_FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_gwd);
                        break;
                    case CHANGE_COLOR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_gwc);
                        break;
                }
                break;

            case YELLOW:
                switch (rank){
                    case ZERO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y0);
                        break;
                    case ONE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y1);
                        break;
                    case TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y2);
                        break;
                    case THREE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y3);
                        break;
                    case FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y4);
                        break;
                    case FIVE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y5);
                        break;
                    case SIX:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y6);
                        break;
                    case SEVEN:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y7);
                        break;
                    case EIGHT:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y8);
                        break;
                    case NINE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_y9);
                        break;
                    case DRAW_TWO:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_yd);
                        break;
                    case REVERSE:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_yr);
                        break;
                    case SKIP:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_ys);
                        break;
                    case DRAW_FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_ywd);
                        break;
                    case CHANGE_COLOR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_ywc);
                        break;
                }
                break;

            case WILD:
                switch (rank){
                    case DRAW_FOUR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_wd);
                        break;
                    case CHANGE_COLOR:
                        drawable = ContextCompat.getDrawable(context, R.drawable.ic_wc);
                        break;
                }
                break;
        }

        return drawable;
    }
}
