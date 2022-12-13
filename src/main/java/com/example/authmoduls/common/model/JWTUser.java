package com.example.authmoduls.common.model;

import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JWTUser {

    private enum ClaimFiledNames{
        ID, ACCESSS
    }

    String id;
    List<String> accesss;

    public JWTUser(String id) {
        this.id = id;
    }

    public Map<String, Object> toClaim(){
        Map<String, Object> claim   = new HashMap<>();
        claim.put(ClaimFiledNames.ID.toString(),id);
        claim.put(ClaimFiledNames.ACCESSS.toString(),accesss);
        return claim;
    }

    public boolean hasRole(String role){

        return this.accesss.contains(role);
    }

    public static JWTUser fromClaim(Claims claim){
        JWTUser jwtUser = new JWTUser();
        jwtUser.setId((String) claim.get(ClaimFiledNames.ID.toString()));
        jwtUser.setAccesss((List<String>) claim.get(ClaimFiledNames.ACCESSS.toString()));
        return jwtUser;
    }
}
