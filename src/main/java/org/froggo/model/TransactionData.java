package org.froggo.model;

import lombok.Data;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@Data
public class TransactionData {
    @Getter
    private final UUID id = UUID.randomUUID();
    private String did;
    private Set<String> statementHashes;
    private String consentForDataSharing;
}
