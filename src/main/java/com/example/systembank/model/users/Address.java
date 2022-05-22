package com.example.systembank.model.users;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Embeddable
public class Address {

      @NotNull(message = "The streetAddress cannot be empty")
      private String streetAddress;

      @NotNull(message = "The zipCode cannot be empty")
      @Min(1000)
      @Max(52999)
      private Integer zipCode;

      @NotNull
      private String city;


}

