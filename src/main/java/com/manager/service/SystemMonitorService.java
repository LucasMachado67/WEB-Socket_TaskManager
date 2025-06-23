package com.manager.service;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

@Service
public class SystemMonitorService {

    //Acessa os dados de todo o sistema do computador como hardware, SO, sensores e etc
    private final SystemInfo systemInfo = new SystemInfo();
    //Aqui obtemos a camada de abstração de hardware do biblioteca OSHI
    private final HardwareAbstractionLayer hal = systemInfo.getHardware();
    //Obtem as informações do processador
    private final CentralProcessor processor = hal.getProcessor();
    private long[] prevTicks = processor.getSystemCpuLoadTicks();
    private final List<HWDiskStore> disk = hal.getDiskStores();

    //método para pegar as informações do computador
    public Map<String, Object> getSystemsStats() {

        //Mapa para armazenar as estatísticas 
        Map<String, Object> stats = new HashMap<>();


        //Necessário poís a CPU precisa de dois pontos de tempo para medir
        try{
            //Espera um pequeno intervalo
            Thread.sleep(100); // 100ms
        }catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }
        // retorna os ticks da CPU
        long[] ticks = processor.getSystemCpuLoadTicks();
        //compara os ticks anteriores com os atuais e retorna a porcentagem de uso da CPU desde a última medição.
        double cpuload = processor.getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        prevTicks = ticks;

        //Obtém informações de memória RAM.
        GlobalMemory memory = hal.getMemory();

        //Converter para double e transformar em Giga Bytes
        Double memoryConverted = (double) memory.getTotal() / 1024 / 1024 / 1024;
        Double memoryAvailableConverted = (double) memory.getAvailable() / 1024 / 1024 / 1024;

        DecimalFormat df = new DecimalFormat("#.00");

        //Adicionando no mapa
        
        stats.put("cpuload", df.format(cpuload));
        stats.put("totalMemory", df.format(memoryConverted));
        stats.put("memoryInUse", df.format(memoryConverted - memoryAvailableConverted));
        stats.put("availableMemory", df.format(memoryAvailableConverted));
        //Adicionando os valores do Disk
        stats.put("Disk Name", disk.get(0).getName());
        stats.put("Disk model", disk.get(0).getModel());
        stats.put("Disk Size(Bytes)", disk.get(0).getSize() / 1024 / 1024 / 1024);

        return stats;
    }
}
