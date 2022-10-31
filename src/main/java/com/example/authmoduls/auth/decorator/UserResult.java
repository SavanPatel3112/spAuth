package com.example.authmoduls.auth.decorator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserResult {
    Set<String> userIds;
    Set<Integer> semester;
}
