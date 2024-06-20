package com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.controller;

import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.entity.SmsRequestElasticSearch;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.exception.InvalidRequestException;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.SearchByPhoneNoRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.requestbody.SearchByTextRequestBody;
import com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.service.SmsRequestElasticSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static com.assignment.notification.NotificationConsumer.NotificationConsumerApplication.util.PhoneNumberValidator.isValidPhoneNumber;

@Slf4j
@RestController
@RequestMapping("/v1/sms")
public class SmsRequestElasticSearchController {
    private final SmsRequestElasticSearchService SmsRequestElasticSearchService;
    @Autowired
    public SmsRequestElasticSearchController(SmsRequestElasticSearchService SmsRequestElasticSearchService)
    {
        this.SmsRequestElasticSearchService = SmsRequestElasticSearchService;
    }

    /* Get api for getting SMS by phone number between start and end time with pagination */
    @GetMapping("/search/phone")
    public ResponseEntity<List<SmsRequestElasticSearch>> getSmsByPhoneNumberAndTimeRange(@RequestBody SearchByPhoneNoRequestBody searchByPhoneNoRequestBody) {
        try {
            isValidPhoneNumber(searchByPhoneNoRequestBody.getPhoneNumber());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime startTime = LocalDateTime.parse(searchByPhoneNoRequestBody.getStartTime(), formatter);
            LocalDateTime endTime = LocalDateTime.parse(searchByPhoneNoRequestBody.getEndTime(), formatter);

            Pageable pageable = PageRequest.of(searchByPhoneNoRequestBody.getPage(), searchByPhoneNoRequestBody.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<SmsRequestElasticSearch> resultPage = SmsRequestElasticSearchService.findByPhoneNumberAndTimeRange(
                    searchByPhoneNoRequestBody.getPhoneNumber(), startTime, endTime, pageable);

            return ResponseEntity.ok(resultPage.getContent());
        } catch (DateTimeParseException e) {
            log.error("Error parsing date string: " + e.getMessage());
            throw new InvalidRequestException("Invalid date format. Please use 'dd-MM-yyyy HH:mm:ss' format for dates.");
        } catch (Exception e) {
            log.error("Error in fetching sms by phone number and time range in elastic search. Error message: " + e.getMessage());
            throw e;
        }
    }


    /* Get api for getting SMS by text with pagination */
    @GetMapping("/search/text")
    public ResponseEntity<List<SmsRequestElasticSearch>> getSmsByText(@RequestBody SearchByTextRequestBody searchByTextRequestBody) {
        try{
            Pageable pageable = PageRequest.of(searchByTextRequestBody.getPage(), searchByTextRequestBody.getSize(), Sort.by(Sort.Direction.DESC, "createdAt"));

            Page<SmsRequestElasticSearch> resultPage = SmsRequestElasticSearchService.findByTextContaining(
                    searchByTextRequestBody.getSearchText(), pageable);

            return ResponseEntity.ok(resultPage.getContent());
        }
        catch (Exception e)
        {
            log.error("Error in fetching sms by text in elastic search. Error Message: " + e.getMessage());
            throw  e;
        }
    }
//    @GetMapping("/v1/sms/search/text")
//    public ResponseEntity<List<SmsRequestElasticSearch>> getSmsByText(
//            @RequestParam String searchText,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//        try{
//
//            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//            Page<SmsRequestElasticSearch> resultPage = SmsRequestElasticSearchService.findByTextContaining(
//                    searchText, pageable);
//
//            return ResponseEntity.ok(resultPage.getContent());
//        }
//        catch (Exception e)
//        {
//            log.error("Error in fetching sms by text in elastic search. Error Message: " + e.getMessage());
//            throw  e;
//        }
//    }

    //    @GetMapping("/v1/sms/search/{phoneNumber}")
//    public ResponseEntity<List<SmsRequestElasticSearch>> getSmsByPhoneNumberAndTimeRange(
//            @PathVariable String phoneNumber,
//            @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam LocalDateTime startTime,
//            @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") @RequestParam LocalDateTime endTime,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size) {
//
//        try{
//            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//            Page<SmsRequestElasticSearch> resultPage = SmsRequestElasticSearchService.findByPhoneNumberAndTimeRange(
//                    phoneNumber, startTime, endTime, pageable);
//
//            return ResponseEntity.ok(resultPage.getContent());
//        }
//        catch (Exception e)
//        {
//            log.error("Error in fetching sms by phone number and time range in elastic search. Error message: " + e.getMessage());
//            throw  e;
//        }
//    }
}
