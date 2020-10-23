package chat.client;

public class ConnectionData {

    private  String IP_ADDR ;//ip address - 83.99.253.112?
    private  int PORT ;
    private String name;

    public String getIP_ADDR() {
        return IP_ADDR;
    }

    public int getPORT() {
        return PORT;
    }

    public String getName() {
        return name;
    }

    public void setIP_ADDR(String ip) {
        this.IP_ADDR = ip;
    }

    public void setPORT(int PORT) {
        this.PORT = PORT;
    }

    public void setName(String name) {
        this.name = name;
    }
}
