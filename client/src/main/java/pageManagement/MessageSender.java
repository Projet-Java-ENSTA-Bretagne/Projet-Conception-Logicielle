package pageManagement;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

public class MessageSender implements Observer {
    /*
    allows the client to send a message by looking at the enter keystroke
     */

    private static final Logger log = LogManager.getLogger(MessageSender.class);


    private void sendMessage(String messageText) throws IOException {
        String currentWholeMessage = messageText;

        if ((currentWholeMessage == null) || (currentWholeMessage.length() == 0)) {
            // we don't want to send empty messages
            return;
        }

        else {
            System.out.println("");
            log.info("Vous venez d'envoyer un nouveau message !");

            // adding the new message to the Discussion scene

            // the sent message has to be less than 20 characters (for now)
            String currentMessage = currentWholeMessage.substring(0, Math.min(currentWholeMessage.length(), 20));

            MessageController messageController = new MessageController(DiscussionController.getCurrentSender(),
                    DiscussionController.getCurrentDate(), currentMessage);

            log.debug("Émetteur : \"" + messageController.getSender() + "\"");
            log.debug("Date d'envoi : \"" + messageController.getDate() + "\"");
            log.debug("Contenu : \"" + messageController.getContent() + "\"");

            DiscussionController.addMessageToAssociatedMessageList(messageController);
            DiscussionController.displayMessageFromController(messageController);
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        DiscussionController controller = (DiscussionController) o;
        String text = controller.getMessageTextField().getText();
        try {
            this.sendMessage(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // resetting the message TextField
        controller.getMessageTextField().setText("");
    }
}
