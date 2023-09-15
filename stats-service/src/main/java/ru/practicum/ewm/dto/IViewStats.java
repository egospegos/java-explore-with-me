package ru.practicum.ewm.dto;

import java.util.List;

public interface IViewStats {
    String getApp();

    List<String> getUri();

    Integer getHits();
}
