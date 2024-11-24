package org.example.employeeTrainingRecord;

import org.example.employeeTrainingRecord.security.service.TokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlobalServices {
    @Autowired
    TokenServices tokenServices;
    public String getValueFromToken(String token,String value){
        if(token == null || token.isEmpty()){
            return "401";
        }
        else if (value.equals("role")){
            return tokenServices.getRole(token);
        }else if (value.equals("name")){
            return tokenServices.getName(token);
        }else if (value.equals("oid")){
            return tokenServices.getOid(token);
        }
        return "500";
    }

}
