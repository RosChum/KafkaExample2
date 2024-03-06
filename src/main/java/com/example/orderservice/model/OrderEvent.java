package com.example.orderservice.model;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderEvent {

    private String product;

    private Integer quantity;
}
