package com.example.authmoduls.common.decorator;

import com.example.authmoduls.auth.decorator.Resultupdate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Component
public class ResultResponse<T> {
    T data;
    Response status;
    Resultupdate result;
}
