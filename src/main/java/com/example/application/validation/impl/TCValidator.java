package com.example.application.validation.impl;
import com.example.application.validation.TCValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class TCValidator implements ConstraintValidator<TCValid, String>{


    @Override
    public boolean isValid(String strNumber, ConstraintValidatorContext context) {
    	

        if(strNumber == null || strNumber.length()!=11 || strNumber.charAt(0)=='0'){
        	
       

        	return false;
        }
        int oddSum=0,evenSum=0,controlDigit=0;
        for(int i=0;i<=8;i++){
            if(i%2==0){
                oddSum+=Character.getNumericValue(strNumber.charAt(i));

            }else{
                evenSum+=Character.getNumericValue(strNumber.charAt(i));
            }
        }
        controlDigit = (oddSum*7-evenSum)%10;
        if(Character.getNumericValue(strNumber.charAt(9))!=controlDigit){
        	
        	return false;
        }
        if(Character.getNumericValue(strNumber.charAt(10))!=(controlDigit+evenSum+oddSum)%10){
        
        	return false;
        }
        
    	
        return true;
    }

}
