package hive;

import hive.game.GameLogic;
import hive.game.GameTile;
import hive.insects.Insect;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HiveGameXMLWriter {

    private HiveGameXMLWriter() {
    }

    public static void writeGameState(GameLogic gameLogic, String filePath) throws IOException {
        try {
            // Set up the SAX Transformer
            SAXTransformerFactory factory = (SAXTransformerFactory) TransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            File file = new File(filePath);

            FileOutputStream fos = new FileOutputStream(file);
            handler.setResult(new StreamResult(fos));

            // Start the document
            handler.startDocument();
            AttributesImpl atts = new AttributesImpl();

            // Write the HiveGameState
            handler.startElement("", "", "Game", atts);

            // Write the board state
            handler.startElement("", "", "Board", atts);
            for (GameTile tile : gameLogic.getBoard().getInitializedTileSet()) {
                atts.clear();
                atts.addAttribute("", "", "x", "CDATA", String.valueOf(tile.getCoordinate().getX()));
                atts.addAttribute("", "", "y", "CDATA", String.valueOf(tile.getCoordinate().getY()));
                atts.addAttribute("", "", "insect", "CDATA", tile.getInsect().toString());
                atts.addAttribute("", "", "player", "CDATA", String.valueOf(tile.getInsect().color));
                handler.startElement("", "", "tile", atts);
                handler.endElement("", "", "tile");
            }
            handler.endElement("", "", "Board");

            // Write the orange player
            atts.clear();
            handler.startElement("", "", "orangePlayer", atts);
            for (Insect insect : gameLogic.getBluePlayer().getInsects()) {
                atts.clear();
                atts.addAttribute("", "", "type", "CDATA", insect.toString());
                handler.startElement("", "", "insect", atts);
                handler.endElement("", "", "insect");
            }
            handler.endElement("", "", "orangePlayer");

            // Write the blue player
            atts.clear();
            handler.startElement("", "", "bluePlayer", atts);
            for (Insect insect : gameLogic.getBluePlayer().getInsects()) {
                atts.clear();
                atts.addAttribute("", "", "type", "CDATA", insect.toString());
                handler.startElement("", "", "insect", atts);
                handler.endElement("", "", "insect");
            }
            handler.endElement("", "", "bluePlayer");

            //Write the GameLogic
            atts.clear();
            atts.addAttribute("", "", "turns", "CDATA", String.valueOf(gameLogic.getTurns()));
            atts.addAttribute("", "", "nextPlayer", "CDATA", String.valueOf(gameLogic.getNextPlayer().color));
            handler.startElement("", "", "GameLogic", atts);
            handler.endElement("", "", "GameLogic");

            // End the document
            handler.endElement("", "", "Game");
            handler.endDocument();

            fos.flush();
            fos.close();
        } catch (SAXException e) {
            throw new IOException("Error while writing XML", e);
        } catch (TransformerConfigurationException e) {
            throw new IOException("Error while setting up configuration.", e);
        }
    }
}