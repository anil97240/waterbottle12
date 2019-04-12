package com.example.waterbottle.admin_agent_side.Model;

public class UsersBean {
    String Agent_email,Agent_name,Agent_password,Type,Agent_mobile;

    public UsersBean() {
    }

    public UsersBean(String agent_email, String agent_name, String agent_password, String type, String agent_mobile) {
        Agent_email = agent_email;
        Agent_name = agent_name;
        Agent_password = agent_password;
        Type = type;
        Agent_mobile = agent_mobile;
    }

    public String getAgent_email() {
        return Agent_email;
    }

    public void setAgent_email(String agent_email) {
        Agent_email = agent_email;
    }

    public String getAgent_name() {
        return Agent_name;
    }

    public void setAgent_name(String agent_name) {
        Agent_name = agent_name;
    }

    public String getAgent_password() {
        return Agent_password;
    }

    public void setAgent_password(String agent_password) {
        Agent_password = agent_password;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getAgent_mobile() {
        return Agent_mobile;
    }

    public void setAgent_mobile(String agent_mobile) {
        Agent_mobile = agent_mobile;
    }
}
