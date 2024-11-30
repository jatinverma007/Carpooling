package com.carpooling.ums.dto;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UsersTncDTO {

    private Long id;
    private boolean accepted;
    private Date acceptedDate;
}
