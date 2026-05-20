import javafx.scene.paint.Color;

public class Card {
    private final String rank;
    private final String suit;
    private final int value;

    public Card(String rank, String suit, int value) {
        this.rank = rank;
        this.suit = suit;
        this.value = value;
    }

    public int getValue() {
         return value; 
        }
    public String getSuit() { 
        return suit; 
    }
    
    public Color getColor() {
        return (suit.equals("♥") || suit.equals("♦")) ? Color.RED : Color.BLACK;
    }

    @Override
    public String toString() {
        return rank + suit;
    }
}