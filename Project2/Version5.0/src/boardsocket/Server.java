package boardsocket;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

import whiteboardgui.ServerPanel;

public class Server {

    public Server(){
        this.initialization();
    }
    
    private void initialization(){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        int portNumber = 0;
        boolean legalPort = false;
        do {
            String input = JOptionPane.showInputDialog(null,
                    "Enter a port for the server (1024 ~ 65535):", "2029");
            if (input == null) {
                System.exit(1);
            }
            try {
                portNumber = Integer.parseInt(input);
                if ((portNumber < 1024) || (portNumber > 65535)) {
                    JOptionPane.showMessageDialog(null,
                            "Port number should be in 1024 ~ 65535", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    legalPort = true;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null,
                        "Please in legal pure number format", "Error", JOptionPane.ERROR_MESSAGE);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null,
                        "Unkown error occurs", "Error", JOptionPane.ERROR_MESSAGE);
            }

        } while (!legalPort);

        System.out.println("Server starts on port: " + portNumber);

        ServerPanel svPanel = new ServerPanel(portNumber);
        svPanel.setVisible(true);

    }

    public static void main(String[] args) {
        Server server = new Server();
    }
}
