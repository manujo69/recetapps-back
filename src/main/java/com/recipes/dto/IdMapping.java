package com.recipes.dto;

public class IdMapping {
    private String clientId;
    private Long serverId;

    public IdMapping() {}
    public IdMapping(String clientId, Long serverId) {
        this.clientId = clientId;
        this.serverId = serverId;
    }

    public String getClientId() { return clientId; }
    public void setClientId(String clientId) { this.clientId = clientId; }
    public Long getServerId() { return serverId; }
    public void setServerId(Long serverId) { this.serverId = serverId; }
}
