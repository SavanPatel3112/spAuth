package com.example.authmoduls.cc.decorator;

import com.example.authmoduls.cc.enums.Plan;
import com.example.authmoduls.cc.modal.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponse {

        String firstName;
        String middleName;
        String lastName;
        String fullName;
        String email;
        String phoneNumber;
        Address address;
        Plan plan;
        List<MemberPlans> memberPlans;
        @JsonIgnore
        Date date;

}
