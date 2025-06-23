package com.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import com.manager.service.SystemMonitorService;

@Controller
public class SystemMonitorController {

    //é usada para enviar mensagens via WebSocket
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private SystemMonitorService monitorService;

    //Executa este método a cada 2 segundos, (Precisa acionar @EnableScheduling na classe application)
    @Scheduled(fixedRate = 2000)
    public void sendSystemStats(){
        //Enviando os dados via WebSocket
        var stats = monitorService.getSystemsStats();
        messagingTemplate.convertAndSend("/topic/system-stats", stats);
        //Mostrando os dados
        System.out.println(stats);
        System.out.println("CPU LOAD: " + stats.get("cpuload") + "%");
        System.out.println("Memory: " + stats.get("totalMemory"));
        System.out.println("Memory in use: " + stats.get("memoryInUse"));
        System.out.println("Available Memory: " + stats.get("availableMemory"));
        System.out.println("Disk Storage Name: " + stats.get("Disk Name"));
        System.out.println("Disk Storage Model: " + stats.get("Disk model"));
        System.out.println("Disk Storage Size: " + stats.get("Disk Size(Bytes)"));
    }


}
