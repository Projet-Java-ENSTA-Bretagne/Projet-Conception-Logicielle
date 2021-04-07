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
            DiscussionController.updateMessages();

            // adding the new message to the Discussion scene

            // the sent message has to be less than 20 characters (for now)
            String currentMessage = currentWholeMessage.substring(0, Math.min(currentWholeMessage.length(), 20));

            /* ------------------------------------------------------------------------ */

            // using the "SendGroup" (or "sendPM") protocol

            try {
                JSONObject msgData = new JSONObject();
                msgData.put("group_id", DiscussionController.getCurrentlyOpenedGroupID());
                msgData.put("message", currentMessage);

                String[] requestStatusAndMsgInfo = sendGroupRequest(msgData);

                String requestStatus = requestStatusAndMsgInfo[0];
                String msgID = requestStatusAndMsgInfo[1];

                if (requestStatus.equals("OK")) {
                    MessageController messageController = new MessageController(DiscussionController.getCurrentSender(),
                            DiscussionController.getCurrentSenderID(), DiscussionController.getCurrentDate(),
                            currentMessage, msgID);

                    DiscussionController.addMessageToAssociatedMessageList(messageController);
                    DiscussionController.displayMessageFromController(messageController);

                    System.out.println("");
                    log.info("Vous venez d'envoyer un nouveau message !");
                    log.debug("Émetteur : \"" + messageController.getSender() + "\"");
                    log.debug("Date d'envoi : \"" + messageController.getDate() + "\"");
                    log.debug("Contenu : \"" + messageController.getContent() + "\"");

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

    private String[] sendGroupRequest(JSONObject msgData) {
        String getUserGroupsRequest = RequestBuilder.buildWithData("sendPM", msgData).toString();
        String responseFromServer = MainController.getTcpClient().sendRequest(getUserGroupsRequest);

        JSONObject wholeReceivedData = new JSONObject(responseFromServer);
        String requestStatus = wholeReceivedData.getString("status");

        String msgID;
        if (requestStatus.equals("OK")) {
            // getting the message ID the received data
            JSONObject data = wholeReceivedData.getJSONObject("data");
            String message = data.getString("message");
            msgID = message.substring(35, message.length());
            log.debug("Checking the ID of the current message : " + msgID);
        }
        else {
            msgID = "none";
        }

        String[] requestStatusAndMsgID = {requestStatus, msgID};
        return requestStatusAndMsgID;
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
