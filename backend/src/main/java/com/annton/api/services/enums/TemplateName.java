package com.annton.api.services.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum TemplateName {
    CONFIRMATION_EMAIL("confirmation", "Your Confirmation Code"),
    PASS_CODE_EMAIL("pass_code", "Your Pass Code");
    private final String name;
    private final String subject;
}
