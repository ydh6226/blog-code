package com.study.domain;

import lombok.Data;

@Data
public class File {
    int id;
    int count;

    public void increaseCount() {
        count++;
    }
}
