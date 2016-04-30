package fi.metropolia.translatorskeleton.model;

/**
 * Setting model for storing model value
 */
public class Setting {
    private int length;
    private String type;
    private int time_out;

    public Setting(int length, String type, int time_out) {
        this.length = length;
        this.type = type;
        this.time_out = time_out;
    }

    

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime_out() {
        return this.time_out;
    }

    public void setTime_out(int time_out) {
        this.time_out = time_out;
    }

}
