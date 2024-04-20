package com.example.springaop.testaop;

import org.springframework.stereotype.Service;

@Service
public class ShipmentService {

    @Log
    public void doStuff (){
        System.out.println("Inside Method");
    }

    public void matchJoinPointExecution(){
        System.out.println("Inside matchJoinPointExecution :::: ");
    }
}
