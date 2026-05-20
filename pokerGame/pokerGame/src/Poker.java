import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Poker {
    private final List<Card> deck = new ArrayList<>();

    public void createAndShuffleDeck() {
        deck.clear();
        String[] suits = {"♥", "♦", "♣", "♠"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                deck.add(new Card(ranks[i], suit, values[i]));
            }
        }
        Collections.shuffle(deck);
    }

    public Card dealCard() {
        return deck.remove(deck.size() - 1);
    }
    public int getHandScore(List<Card> privateCards, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(privateCards);
        allCards.addAll(communityCards);

        Map<String, Integer> suitCounts = new HashMap<>();
        boolean isFlush = false;
        for (Card c : allCards) {
            suitCounts.put(c.getSuit(), suitCounts.getOrDefault(c.getSuit(), 0) + 1);
        }
        for (int count : suitCounts.values()) {
            if (count >= 5) isFlush = true;
        }
        List<Integer> uniqueValues = new ArrayList<>();
        for (Card c : allCards) {
            if (!uniqueValues.contains(c.getValue())) uniqueValues.add(c.getValue());
        }
        Collections.sort(uniqueValues);
        
        boolean isStraight = false;
        for (int i = 0; i <= uniqueValues.size() - 5; i++) {
            if (uniqueValues.get(i + 4) - uniqueValues.get(i) == 4) isStraight = true;
        }
        if (uniqueValues.contains(14) && uniqueValues.contains(2) && 
            uniqueValues.contains(3) && uniqueValues.contains(4) && uniqueValues.contains(5)) {
            isStraight = true;
        }
        Map<Integer, Integer> counts = new HashMap<>();
        for (Card c : allCards) {
            counts.put(c.getValue(), counts.getOrDefault(c.getValue(), 0) + 1);
        }

        int pairs = 0;
        boolean trips = false;
        boolean quads = false;

        for (int count : counts.values()) {
            if (count == 4) {
                quads = true;
            }
            if (count == 3) {
                trips = true;
            }
            if (count == 2) {
                pairs++;
            }
        }

        if (isStraight && isFlush) {
            return 800; 
        }
        if (quads) {
            return 700;
        }
        if (trips && pairs > 0) {
            return 600;
        }
        if (isFlush) {
            return 500;
        }
        if (isStraight) {
            return 400;
        }
        if (trips) {
            return 300;
        }
        if (pairs >= 2) {
            return 200;
        }
        if (pairs == 1) {
            return 100;
        }
        return 0;
    }

   
    public String getHandName(int score) {
        if (score == 800) {
            return "Straight Flush";
        }
        if (score == 700) {
            return "Four of a Kind";
        }
        if (score == 600) {
            return "Full House";
        }
        if (score == 500) {
            return "Flush";
        }
        if (score == 400) {
            return "Straight";
        }
        if (score == 300) {
            return "Three of a Kind";
        }
        if (score == 200) {
            return "Two Pair";
        }
        if (score == 100) {
            return "One Pair";
        }
        return "High Card";
    }
}