package Other;

public class Message {

    public Message(){
        msg = Constants.DEFAULT_MSG;
        type = Constants.DEFAULT_TYPE;
        id = Constants.DEFAULT_ID;
    }

    public Message(String msg, String type, int id){
        this.msg = msg;
        this.type = type;
        this.id = id;
    }

    public Message(String msg, String type){
        this.msg = msg;
        this.type = type;
        this.id = Constants.DEFAULT_ID;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    private String msg;
    private String type;
    private int id;
}
