package com.project.christinkcdev.share.sharein.models;

public interface Comparable {
    boolean comparisonSupported();

    String getComparableName();

    long getComparableDate();

    long getComparableSize();
}
