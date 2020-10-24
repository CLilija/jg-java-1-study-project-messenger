package chat.client;

import network.TCPConnection;
import network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JScrollPane;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {


    private static String IP_ADDR; //"83.99.253.112"
    private static int PORT = 8189;                  // port -8189
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static String name;



    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("IP");
        IP_ADDR=scanner.next();
        //System.out.println("PORT");
        //PORT=scanner.nextInt();
        System.out.println("Name");
        name=scanner.next();

        SwingUtilities.invokeLater(ClientWindow::new);

    }

    private final JTextArea log = new JTextArea();
    JScrollPane scrollPlane = new JScrollPane(log);// добавил для того что бы сделать скролл, разместил log в scrollPlane
    private final JTextField fieldNickname = new JTextField(name);
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);


        log.setEditable(false);
        log.setLineWrap(true);
        //add(log, BorderLayout.CENTER);// раньше добовляли log
        add(scrollPlane, BorderLayout.CENTER);// добавил для того что бы сделать скролл, добавил именно скрол

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection exception: " + e);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = fieldInput.getText();
        if(msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(fieldNickname.getText() + ": " + msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready...");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection exception:" + e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(() -> {

            log.append(msg + "\n");
            log.setCaretPosition(log.getDocument().getLength());
        });
    }
}
