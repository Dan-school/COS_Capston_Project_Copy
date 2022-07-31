package edu.usm.cos420.antenatal.messengers;

public class ErrorMessage {
    private boolean valid;
    private String message;
    
    public ErrorMessage(boolean valid, String message) {
        this.valid = valid;
        this.message = message == null ? "" : message;
    }

    public boolean isValid() {
        return valid;
    }
    public void setValid(boolean valid) {
        this.valid = valid;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
