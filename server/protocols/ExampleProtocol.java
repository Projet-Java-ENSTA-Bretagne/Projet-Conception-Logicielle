package protocols;

import server.IContext;

import java.io.*;

public class ExampleProtocol implements IProtocol {

    public void execute(IContext ctx, InputStream inStream, OutputStream outStream) {
        String inputRequest;
        BufferedReader is = new BufferedReader(new InputStreamReader(inStream));
        PrintStream os = new PrintStream(outStream);

        try {
            String valeurExpediee = "";

            inputRequest = is.readLine();
            if (inputRequest != null) {
                System.out.println("Received order : " + inputRequest);
                String[] args = inputRequest.split(" ");

                if (args[0].contentEquals("PING")) {
                    valeurExpediee = "PONG";
                    System.out.println("Answered to server: " + valeurExpediee);
                }
                os.println(valeurExpediee);
            }
        } catch (Exception e) {
            System.err.println("Catched an exception : " + e);
        }
    }
}
