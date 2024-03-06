package com.example.orderstatusservice.model;

import lombok.Data;

import java.time.Instant;

@Data
public class OrderStatus {

    private String status;

    private Instant date;

}
