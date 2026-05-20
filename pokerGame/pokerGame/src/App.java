import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private final Poker poker = new Poker(); 
    private final List<Card> playerHand = new ArrayList<>();
    private final List<Card> dealerHand = new ArrayList<>();
    private final List<Card> communityCards = new ArrayList<>();

    private final HBox dealerCardRow = new HBox(10);
    private final HBox communityCardRow = new HBox(12);
    private final HBox playerCardRow = new HBox(10);
    
    private final Label statusMsg = new Label("Press Deal Hand to play Texas Hold'em!");
    private final Button actionBtn = new Button("Deal Hand");
    private int phase = 0; 

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) {
        VBox tableLayout = new VBox(25);
        tableLayout.setPadding(new Insets(20));
        tableLayout.setAlignment(Pos.CENTER);

        statusMsg.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        actionBtn.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        actionBtn.setOnAction(e -> processGamePhase());

        dealerCardRow.setAlignment(Pos.CENTER);
        communityCardRow.setAlignment(Pos.CENTER);
        playerCardRow.setAlignment(Pos.CENTER);

        resetDisplaySlots();

        tableLayout.getChildren().addAll(
            new Label("DEALER'S HAND"),
            dealerCardRow,
            new Label("COMMUNITY POOL"),
            communityCardRow,
            new Label("YOUR POCKET HAND"),
            playerCardRow,
            statusMsg, actionBtn
        );

        stage.setTitle("Poker Game");
        stage.setScene(new Scene(tableLayout, 650, 520));
        stage.show();
    }

    private void processGamePhase() {
        if (phase == 0) { 
            poker.createAndShuffleDeck();
            playerHand.clear(); 
            dealerHand.clear(); 
            communityCards.clear();
            playerHand.add(poker.dealCard()); 
            playerHand.add(poker.dealCard());
            dealerHand.add(poker.dealCard()); 
            dealerHand.add(poker.dealCard());

            renderCards(playerCardRow, playerHand, false);
            renderCards(dealerCardRow, dealerHand, true); 
            renderCards(communityCardRow, communityCards, false);

            statusMsg.setText("Pocket cards dealt! Proceed to the Flop?");
            actionBtn.setText("Deal Flop");
            phase = 1;

        } else if (phase == 1) { 
            communityCards.add(poker.dealCard());
            communityCards.add(poker.dealCard());
            communityCards.add(poker.dealCard());

            renderCards(communityCardRow, communityCards, false);
            statusMsg.setText("Flop is on the table. Deal Turn & River?");
            actionBtn.setText("Deal Final Cards");
            phase = 2;

        } else if (phase == 2) { 
            communityCards.add(poker.dealCard());
            communityCards.add(poker.dealCard());

            renderCards(communityCardRow, communityCards, false);
            statusMsg.setText("All community cards out! Who won?");
            actionBtn.setText("Showdown!");
            phase = 3;

        } else if (phase == 3) { 
            renderCards(dealerCardRow, dealerHand, false); 
            int playerValue = poker.getHandScore(playerHand, communityCards);
            int dealerValue = poker.getHandScore(dealerHand, communityCards);
            String playerResult = poker.getHandName(playerValue);
            String dealerResult = poker.getHandName(dealerValue);
            String finalVerdict;
            if (playerValue > dealerValue) {
                finalVerdict = " YOU WIN THE ROUND!";
            } else if (dealerValue > playerValue) {
                finalVerdict = " DEALER WINS THE ROUND!";
            } else {
                finalVerdict = " IT'S A TIE (SPLIT POT)!";
            }

            statusMsg.setText("You: " + playerResult + " | Dealer: " + dealerResult + "\n" + finalVerdict);
            actionBtn.setText("Deal Hand");
            phase = 0;
        }
    }

    private void renderCards(HBox layoutRow, List<Card> cardList, boolean makeHidden) {
        layoutRow.getChildren().clear();
        for (Card card : cardList) {
            StackPane cardUi = new StackPane();
            cardUi.setPrefSize(65, 90);
            DropShadow shadow = new DropShadow();
            shadow.setRadius(5.0);
            shadow.setOffsetX(0.0);
            shadow.setOffsetY(3.0);
            shadow.setColor(Color.color(0, 0, 0, 0.3));
            cardUi.setEffect(shadow);

            if (makeHidden) {
                cardUi.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(6), Insets.EMPTY)));
                cardUi.setBorder(new Border(new BorderStroke(Color.CADETBLUE, BorderStrokeStyle.DASHED, new CornerRadii(4), new BorderWidths(4))));
            } else {
                cardUi.setBackground(new Background(new BackgroundFill(Color.WHITE, new CornerRadii(6), Insets.EMPTY)));
                Label cardTxt = new Label(card.toString());
                cardTxt.setFont(Font.font("Arial", FontWeight.BOLD, 18));
                cardTxt.setTextFill(card.getColor());
                cardUi.getChildren().add(cardTxt);
            }
            layoutRow.getChildren().add(cardUi);
        }
    }

    private void resetDisplaySlots() {
        dealerCardRow.getChildren().clear(); 
        communityCardRow.getChildren().clear(); 
        playerCardRow.getChildren().clear();
        for (int i = 0; i < 2; i++) {
            dealerCardRow.getChildren().add(createEmptySlot());
        }
        for (int i = 0; i < 5; i++) {
            communityCardRow.getChildren().add(createEmptySlot());
        }
        for (int i = 0; i < 2; i++) {
            playerCardRow.getChildren().add(createEmptySlot());
        }
    }

    private StackPane createEmptySlot() {
        StackPane frame = new StackPane();
        frame.setPrefSize(65, 90);
        frame.setBorder(new Border(new BorderStroke(Color.LIGHTGRAY, BorderStrokeStyle.SOLID, new CornerRadii(6), new BorderWidths(1.5))));
        return frame;
    }
}


// COMPILE  : Javac --module-path "C:\javafx-sdk-21.0.2\lib" --add-modules javafx.controls Card.java Poker.java App.java   

// runn: java --module-path "C:\javafx-sdk-21.0.2\lib" --add-modules javafx.controls App