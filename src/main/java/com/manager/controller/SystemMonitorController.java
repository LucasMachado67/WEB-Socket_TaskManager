package com.manager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.manager.service.SystemMonitorService;
import org.springframework.web.bind.annotation.GetMapping;

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
        //Mostrando os dados no system
        System.out.println(stats);
        System.out.println("CPU LOAD: " + stats.get("cpuload") + "%");
        System.out.println("Memory: " + stats.get("totalMemory"));
        System.out.println("Memory in use: " + stats.get("memoryInUse"));
        System.out.println("Available Memory: " + stats.get("availableMemory"));
        System.out.println("Disk Storage Name: " + stats.get("Disk Name"));
        System.out.println("Disk Storage Model: " + stats.get("Disk model"));
        System.out.println("Disk Storage Size: " + stats.get("Disk Size(Bytes)"));
    }
    
    @Scheduled(fixedRate = 2000)
    public void atualizarStatsPeriodicamente() {
        var stats = monitorService.getSystemsStats();
        messagingTemplate.convertAndSend("/topic/system-stats", stats);
    }

    @GetMapping("/")
        public String getHardwareInformation(Model model) {
        var stats = monitorService.getSystemsStats();
        //O string format é para formatar quando menor que 1, ele conseguir mostrar o 0,

        model.addAttribute("cpuload", String.format("%.2f", Double.parseDouble(stats.get("cpuload").toString().replace(",", "."))) + "%");
        model.addAttribute("memory", String.format("%.2f", Double.parseDouble(stats.get("totalMemory").toString().replace(",", "."))));
        model.addAttribute("memoryInUse", String.format("%.2f", Double.parseDouble(stats.get("memoryInUse").toString().replace(",", "."))));
        model.addAttribute("memoryAvailable", String.format("%.2f", Double.parseDouble(stats.get("availableMemory").toString().replace(",", "."))));
        model.addAttribute("diskStorageName", stats.get("Disk Name"));
        model.addAttribute("diskStorageModel", stats.get("Disk model"));
        model.addAttribute("diskStorageSize", stats.get("Disk Size(Bytes)"));

        return "index";
    }
}
