package com.example.authmoduls.auth.decorator;
import com.example.authmoduls.common.decorator.UserDateDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthAndYear {
    List<UserDateDetails> userDateDetails ;
    Set<String> title;
    int totalCount;
}
