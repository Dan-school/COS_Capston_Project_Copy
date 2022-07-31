package edu.usm.cos420.antenatal.messengers;

import java.util.HashMap;
import java.util.Map;

public class ControllerToInterface {

    private boolean allFieldsValid;
    private Map<String, ErrorMessage> errorMessages = new HashMap<>();

    public boolean isAllFieldsValid() {
        return allFieldsValid;
    }
    
    public void setAllFieldsValid(boolean allFieldsValid) {
        this.allFieldsValid = allFieldsValid;
    }

    public Map<String, ErrorMessage> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(Map<String, ErrorMessage> errorMessages) {
        this.errorMessages = errorMessages;
    }

    public void addErrorMessage(String field, ErrorMessage error) {
        this.errorMessages.put(field, error);
    }



}
