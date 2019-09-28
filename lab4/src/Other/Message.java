package Other;

public class Message {

    public Message(){
        msg = Constants.DEFAULT_MSG;
        id = Constants.DEFAULT_MSG_ID;
        authorId = Constants.DEFAULT_AUTHOR_ID;
    }

    public Message(int id, String msg, int authorId){
        this.id = id;
        this.msg = msg;
        this.authorId = authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public void setMsg(String msg){
        this.msg = msg;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public int getId() {
        return id;
    }

    public int getAuthorId() {
        return authorId;
    }

    private String msg;
    private int id;
    private int authorId;
}
