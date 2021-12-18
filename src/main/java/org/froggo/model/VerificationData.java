package org.froggo.model;

import lombok.Data;

import java.util.Set;

@Data
public class VerificationData {
    private String did;
    private Set<String> statementsHash;
    private String consentForDataSharing;
}
