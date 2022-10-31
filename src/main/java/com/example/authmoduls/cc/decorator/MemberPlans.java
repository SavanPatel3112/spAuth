package com.example.authmoduls.cc.decorator;

import com.example.authmoduls.cc.enums.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberPlans {

    String memberPlan;
    Plan plan;
    double amount;
    int planNumber;
    String details;

}
