package org.froggo.model;

import lombok.Data;

import java.util.Set;

@Data
public class ValidationRequest {
    private String did;
    private Set<String> statementHashes;
}
