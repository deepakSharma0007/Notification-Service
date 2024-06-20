package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.controller;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.BlacklistNumber;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.exception.InvalidRequestException;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.BlacklistedNumberRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.BlacklistNumberService;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.RedisSetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.util.PhoneNumberValidator.isValidPhoneNumberList;

@Slf4j
@RestController
@RequestMapping("/v1")
public class BlacklistController {
    /* Using order-set Data structure to store blacklisted numbers in redis */

    RedisSetService redisSetService;
    BlacklistNumberService blacklistNumberService;
    @Autowired
    public BlacklistController(RedisSetService redisSetService, BlacklistNumberService blacklistNumberService)
    {
        this.redisSetService = redisSetService;
        this.blacklistNumberService = blacklistNumberService;
    }

    /* Post api to blacklist numbers */
    @PostMapping("/blacklist")
    public String addBlacklistedNumbers(@RequestBody BlacklistedNumberRequestBody blacklistedNumbers)
    {
        try{
            List<String> phoneNos = blacklistedNumbers.getPhoneNumbers();
            /* Validating phone numbers */
            isValidPhoneNumberList(phoneNos);
            log.info("All phone numbers are valid");
            for(String phoneNo: phoneNos) {
                Optional<BlacklistNumber> blacklistNumberOptional = blacklistNumberService.findByNumber(phoneNo);
                if (blacklistNumberOptional.isPresent()) {
                    continue;
                } else {
                    BlacklistNumber blacklistNumber = new BlacklistNumber();
                    blacklistNumber.setPhoneNumber(phoneNo);
                    blacklistNumberService.addBlacklistedNumber(blacklistNumber);
                }
                log.info(phoneNo + "is blacklisted");
                redisSetService.add(phoneNo);
            }
            log.info("All numbers in list are successfully blacklisted");
            return "Successfully blacklisted";
        }
        catch (Exception e)
        {
            log.error("Error in post api to blacklist phone numbers. Error message: " + e.getMessage());
            throw  e;
        }
        finally {
            redisSetService.closeConnection();
        }
    }
    /* Post api to whitelist phone numbers */
    @DeleteMapping("/blacklist")
    public String removeBlacklistedNumbers(@RequestBody BlacklistedNumberRequestBody blacklistedNumbersList)
    {
        try{
            List<String> phoneNumbers = blacklistedNumbersList.getPhoneNumbers();
            /* Validating phone numbers */
            isValidPhoneNumberList(phoneNumbers);
            log.info("All phone numbers are valid");

            /* Checking all the numbers in the list are really blacklisted*/
            for(String phoneNo: phoneNumbers) {
                Optional<BlacklistNumber> blacklistNumberOptional = blacklistNumberService.findByNumber(phoneNo);
                if (blacklistNumberOptional.isEmpty()) {
                    throw new InvalidRequestException(phoneNo + " not Blacklisted");
                }
            }
            /* Whitelisting all the numbers */
            for(String phoneNo: phoneNumbers)
            {
                Optional<BlacklistNumber> blacklistNumberOptional = blacklistNumberService.findByNumber(phoneNo);
                blacklistNumberOptional.ifPresent(blacklistNumber -> blacklistNumberService.deleteBlacklistedNumber(blacklistNumber));
                redisSetService.remove(phoneNo);
                log.info(phoneNo + "is successfully whitelisted");
            }
            return "Successfully whitelisted";
        }
        catch (Exception e)
        {
            log.error("Error in post api to whitelist numbers. Error Message: " + e.getMessage());
            throw  e;
        }
        finally {
            redisSetService.closeConnection();
        }
    }

    /* Get api to fetch all blacklisted numbers */
    @GetMapping("/blacklist")
    public List<String> getBlacklistedNumbers()
    {
        List<String> BlackListedNumbersList;
        try{
            Set<String> BlackListNumberSet = redisSetService.findAll();
            BlackListedNumbersList = new ArrayList<>();
            for(String number: BlackListNumberSet)
            {
                BlackListedNumbersList.add(number.substring(4));
            }
            return BlackListedNumbersList;
        }
        catch (Exception e)
        {
            log.error("Error in get api to fetch blacklist numbers. Error Message:" + e.getMessage());
            throw e;
        }
        finally {
            redisSetService.closeConnection();
        }
    }

//    @GetMapping("/all-blacklist")
//    public List<String> getAllBlacklistNumbers()
//    {
//        List<BlacklistNumber> v = blacklistNumberService.getAllNumbers();
//    }
}
