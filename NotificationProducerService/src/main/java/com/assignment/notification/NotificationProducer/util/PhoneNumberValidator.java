package com.assignment.notification.NotificationProducer.util;

import com.assignment.notification.NotificationProducer.exception.InvalidRequestException;

import java.util.List;

public class PhoneNumberValidator {

    /* checking list of phone numbers is valid */
    public static void isValidPhoneNumberList(List<String> phoneNos)
    {
        if(phoneNos == null || phoneNos.isEmpty())
        {
            throw new InvalidRequestException("List of phone numbers is mandatory");
        }

        // checking whether number is valid or not
        for(String phoneNo: phoneNos)
        {
            isValidPhoneNumber(phoneNo);
        }
    }

    /* checking a phone number is valid */
    public static void isValidPhoneNumber(String phoneNo)
    {
        /* checking whether phone number exists or not */
        if(phoneNo == null || phoneNo.isEmpty())
        {
            throw new InvalidRequestException("Phone number is mandatory");
        }
        /* +91 - [10 dig] is the valid number => phone number should have 13 characters */
        if(phoneNo.length() != 13)
        {
            throw new InvalidRequestException(phoneNo + " is not a valid phone number");
        }

        /* verifying country code */
        if(phoneNo.charAt(0) != '+' || phoneNo.charAt(1) != '9' || phoneNo.charAt(2) != '1')
        {
            throw new InvalidRequestException(phoneNo + " is not a valid number (Country code: +91)");
        }

        /* checking whether number is valid or not */
        for(int i=0;i<phoneNo.length();i++)
        {
            char ch = phoneNo.charAt(i);
            if(ch < '0' || ch > '9')
            {
                if(i > 0)
                {
                    throw new InvalidRequestException(phoneNo + " is not a valid phone number");
                }
            }
        }

    }
}
