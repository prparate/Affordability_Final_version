package com.affordability.service.impl;

import com.affordability.service.IEnvironmentProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@RequestScope
@Slf4j
public class EnvironmentProvider implements IEnvironmentProvider {

    private String premfinaEnvironmentName = null;

    @Override
    public String getPremfinaEnvironmentName() {
        return premfinaEnvironmentName;
    }

    @Override
    public void setEnvironmentFromHttpRequest(String environmentFromRequest) throws IllegalArgumentException {
        this.premfinaEnvironmentName = environmentFromRequest;
    }
}
