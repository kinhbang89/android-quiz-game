package fi.metropolia.translatorskeleton.model;

/**
 * Created by Bang on 24/04/16.
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

    private int getLength() {
        return this.length;
    }
    private void setLength(int length) {
        this.length = length;
    }
    private String getType() {
        return this.type;
    }
    private void setType(String type) {
        this.type = type;
    }
    private int getTime_out() {
        return this.time_out;
    }
    private void setTime_out(int time_out) {
        this.time_out = time_out;
    }

}
