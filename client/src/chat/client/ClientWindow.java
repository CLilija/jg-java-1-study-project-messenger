package chat.client;

import network.TCPConnection;
import network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import java.sql.Connection;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class ClientWindow extends JFrame implements ActionListener, TCPConnectionListener {


    //private static final String IP_ADDR = "192.168.0.100";//ip address - 83.99.253.112?
    //private static final int PORT = 8189;                  // port - ?
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    ConnectionData connectionData=new ConnectionData();


    public static void main(String[] args) {


        SwingUtilities.invokeLater(() -> new ClientWindow());

    }

    private final JTextArea log = new JTextArea();
    JScrollPane scrollPlane = new JScrollPane(log);// добавил для того что бы сделать скролл
    private final JTextField fieldNickname = new JTextField(connectionData.getName());
    private final JTextField fieldInput = new JTextField();

    private TCPConnection connection;

    private ClientWindow() {

        ConnectionData connectionData=new ConnectionData();
        Scanner scanner = new Scanner(System.in);
        System.out.println("IP");
        connectionData.setIP_ADDR(scanner.next());

        System.out.println("PORT");
        connectionData.setPORT(scanner.nextInt());

        System.out.println("Name");
        connectionData.setName(scanner.next());


        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);


        log.setEditable(false);
        log.setLineWrap(true);
        //add(log, BorderLayout.CENTER);
       add(scrollPlane, BorderLayout.CENTER);

        fieldInput.addActionListener(this);
        add(fieldInput, BorderLayout.SOUTH);
        add(fieldNickname, BorderLayout.NORTH);

        setVisible(true);
        try {
            connection = new TCPConnection(this, connectionData.getIP_ADDR(), connectionData.getPORT());
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
        printMsg("Connection exception:???? " + e);
    }

    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                log.append(msg + "\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}
