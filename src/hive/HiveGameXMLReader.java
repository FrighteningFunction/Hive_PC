package hive;

import hive.game.*;
import hive.insects.*;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;

public class HiveGameXMLReader {
    static GameLogic gameLogic = GameLogic.getInstance();

    static GameBoard gameBoard = gameLogic.getBoard();

    static Player orangePlayer = gameLogic.getOrangePlayer();

    static Player bluePlayer = gameLogic.getBluePlayer();

    static Player currentPlayer = null;

    public static void readGameState(String filePath) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        GameContentHandler handler = new GameContentHandler();
        saxParser.parse(new File(filePath), handler);
    }

    private static class GameContentHandler extends DefaultHandler {
        @Override
        public void startDocument() throws SAXException {
            //Nem szükséges
        }

        @Override
        public void endDocument() throws SAXException {
            // Nem szükséges
        }


        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            switch (qName) {
                case "tile":
                    double x = Double.parseDouble(attributes.getValue("x"));
                    double y = Double.parseDouble(attributes.getValue("y"));

                    Coordinate key = new Coordinate(x, y);

                    GameTile tile;

                    //A DoubleTileException elkerülése érdekében
                    if(gameBoard.hasGameTile(key)){
                        tile=gameBoard.getTile(key);
                    }else{
                        tile = new GameTile(gameBoard, key);
                    }

                    String insectType = attributes.getValue("insect");
                    String playerName = attributes.getValue("player");
                    Insect insect;

                    switch (insectType) {
                        case "ant":
                            insect = new Ant(playerName.equals("ORANGE") ? orangePlayer : bluePlayer, gameLogic);
                            insect.setInitialized(true);
                            tile.initialize(insect);
                            insect.setLocation(tile);
                            break;
                        case "beetle":
                            insect = new Beetle(playerName.equals("ORANGE") ? orangePlayer : bluePlayer, gameLogic);
                            insect.setInitialized(true);
                            tile.initialize(insect);
                            insect.setLocation(tile);
                            break;
                        case "spider":
                            insect = new Spider(playerName.equals("ORANGE") ? orangePlayer : bluePlayer, gameLogic);
                            insect.setInitialized(true);
                            tile.initialize(insect);
                            insect.setLocation(tile);
                            break;
                        case "grasshopper":
                            insect = new Grasshopper(playerName.equals("ORANGE") ? orangePlayer : bluePlayer, gameLogic);
                            insect.setInitialized(true);
                            tile.initialize(insect);
                            insect.setLocation(tile);
                            break;
                        case "queen":
                            Queen queen = new Queen(playerName.equals("ORANGE") ? orangePlayer : bluePlayer, gameLogic);
                            queen.setInitialized(true);
                            if (playerName.equals("ORANGE")) {
                                orangePlayer.setQueen(queen);
                            } else {
                                bluePlayer.setQueen(queen);
                            }
                            tile.initialize(queen);
                            queen.setLocation(tile);
                            break;
                        default:
                            MainAppLogger.getLogger().error("Unspecified insect type was found in the XML!");
                            break;
                    }

                    break;
                case "orangePlayer":
                    currentPlayer = orangePlayer;
                    break;
                case "bluePlayer":
                    currentPlayer = bluePlayer;
                    break;
                case "insect":
                    String type = attributes.getValue("type");
                    switch (type) {
                        case "ant":
                            currentPlayer.getInsects().add(new Ant(currentPlayer, gameLogic));
                            break;
                        case "beetle":
                            currentPlayer.getInsects().add(new Beetle(currentPlayer, gameLogic));
                            break;
                        case "spider":
                            currentPlayer.getInsects().add(new Spider(currentPlayer, gameLogic));
                            break;
                        case "grasshopper":
                            currentPlayer.getInsects().add(new Grasshopper(currentPlayer, gameLogic));
                            break;
                        case "queen":
                            Queen queen = new Queen(currentPlayer, gameLogic);
                            currentPlayer.getInsects().add(queen);
                            currentPlayer.setQueen(queen);
                            break;
                        default:
                            MainAppLogger.getLogger().error("Unspecified insect type was found in the XML!");
                            break;
                    }
                    break;
                case "GameLogic":
                    String color = attributes.getValue("nextPlayer");
                    if(color.equals("ORANGE")){
                        gameLogic.setNextPlayer(gameLogic.getOrangePlayer());
                    }else{
                        gameLogic.setNextPlayer(gameLogic.getBluePlayer());
                    }

                    int turns = Integer.parseInt(attributes.getValue("turns"));
                    gameLogic.setTurns(turns);
                    break;
                case "Board", "Game":
                    //Nem csinálunk semmit
                    break;
                default:
                    MainAppLogger.getLogger().error("Unspecified element was found in the XML!");
                    break;
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            // Nem szükséges
        }
    }
}
