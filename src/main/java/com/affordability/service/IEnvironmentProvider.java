package com.affordability.service;

public interface IEnvironmentProvider {
    String getPremfinaEnvironmentName();
    void setEnvironmentFromHttpRequest(String environmentFromRequest) throws IllegalArgumentException;
}
