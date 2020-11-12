package com.example.demo.entities;

public class Server {
    private String serverBrand;
    private String platform;
    private String CPUSpeed;
    private String memoryManufactory;
    private String memorySpeed;

    public Server() {
    }

    public Server(String serverBrand, String platform, String CPUSpeed, String memoryManufactory, String memorySpeed) {
        this.serverBrand = serverBrand;
        this.platform = platform;
        this.CPUSpeed = CPUSpeed;
        this.memoryManufactory = memoryManufactory;
        this.memorySpeed = memorySpeed;
    }

    public String getServerBrand() {
        return serverBrand;
    }

    public void setServerBrand(String serverBrand) {
        this.serverBrand = serverBrand;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCPUSpeed() {
        return CPUSpeed;
    }

    public void setCPUSpeed(String CPUSpeed) {
        this.CPUSpeed = CPUSpeed;
    }

    public String getMemoryManufactory() {
        return memoryManufactory;
    }

    public void setMemoryManufactory(String memoryManufactory) {
        this.memoryManufactory = memoryManufactory;
    }

    public String getMemorySpeed() {
        return memorySpeed;
    }

    public void setMemorySpeed(String memorySpeed) {
        this.memorySpeed = memorySpeed;
    }
}
