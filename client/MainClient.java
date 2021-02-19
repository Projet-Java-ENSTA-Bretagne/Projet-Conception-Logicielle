public class MainClient {

    public static void main(String[] args) {
        TCPClient tcpClient = new TCPClient("localhost", 6666);

        if (tcpClient.connectToServer()) {
            tcpClient.sendRequest("PING");
            tcpClient.disconnectFromServer();
        }
    }
}
