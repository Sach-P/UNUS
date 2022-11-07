package com.example.unus;

public class Card {

    CardRank rank;
    CardColor color;

    /**
     * constructor to create a card of given rank and color
     *
     * @param rank
     * @param color
     */
    public Card(CardRank rank, CardColor color) {
        this.rank = rank;
        this.color = color;
    }

    /**
     * constructor to create a random card based on weights
     */
    public Card() {
        this.color = randomColor();
        this.rank = randomRank(color);
    }

    /**
     * checks whether a card can be played on top of another card.
     * @param card
     * @return card can be played
     */
    public boolean cardPlayable (Card card){
        return card.getColor() == color || card.getRank() == rank || card.getColor() == CardColor.WILD;
    }

    /**
     * changes the card color to a given color. Used for wild cards when changing colors.
     * @param color
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
     * @param color
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
}
