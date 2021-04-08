package pageManagement;

import networking.RequestBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.json.JSONException;
import java.util.Observable;
import java.util.Observer;
import java.io.IOException;

/**
 * Allows the client to send a message by looking at the "Enter" keystroke.
 */
public class MessageSender implements Observer {

    private final Logger log = LogManager.getLogger(MessageSender.class);

    private void sendMessage(String messageText) throws IOException {
        String currentWholeMessage = messageText;

        if ((currentWholeMessage == null) || (currentWholeMessage.length() == 0)) {
            // we don't want to send empty messages
            return;
        }

        else {
            // adding the new message to the Discussion scene

            // the sent message has to be less than 20 characters (for now)
            String currentMessage = currentWholeMessage.substring(0, Math.min(currentWholeMessage.length(), 20));

            /* ------------------------------------------------------------------------ */

            // using the "SendGroup" (or "sendPM") protocol

            try {
                JSONObject msgData = new JSONObject();
                msgData.put("group_id", DiscussionController.getCurrentlyOpenedGroupID());
                msgData.put("message", currentMessage);

                String requestStatus = sendGroupRequest(msgData);

                if (requestStatus.equals("OK")) {
                    DiscussionController.updateMessages();

                    System.out.println("");
                    log.info("Vous venez d'envoyer un nouveau message !");
                    log.debug("Émetteur : \"" + DiscussionController.getCurrentSender() + "\"");
                    log.debug("Date d'envoi : \"" + DiscussionController.getCurrentDate() + "\"");
                    log.debug("Contenu : \"" + currentMessage + "\"");

                    // resetting the message TextField
                    DiscussionController.getMessageTextField().setText("");
                }
                else {
                    log.error("La communication avec le serveur est corrompue (sendGroupStatus : \"" + requestStatus + "\")");
                    System.exit(1);
                }
            }
            catch (JSONException jsonException) {
                log.error("Erreur JSON détectée : " + jsonException);
                System.exit(1);
            }
        }
    }

    private String sendGroupRequest(JSONObject msgData) {
        String getUserGroupsRequest = RequestBuilder.buildWithData("sendPM", msgData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(getUserGroupsRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String requestStatus = wholeReceivedData.getString("status");

        return requestStatus;
    }

    @Override
    public void update(Observable o, Object arg) {
        String messageText = DiscussionController.getMessageTextField().getText();

        try {
            sendMessage(messageText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
