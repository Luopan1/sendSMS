package mybanner.toerax.com.sendsms.bean;

/**
 * Created by 19233 on 2018/7/17 16:45.
 */
public class Message {
    private String Phone;
    private String time;
    private String message;
    private String statue;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatue() {
        return statue;
    }

    public void setStatue(String statue) {
        this.statue = statue;
    }
}
