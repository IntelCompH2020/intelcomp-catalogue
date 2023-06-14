package eu.intelcomp.catalogue.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

@ConfigurationProperties(prefix = "intelcomp")
public class IntelcompProperties {

    private Set<Object> admins;

    private String loginRedirect;

    private String logoutRedirect;

    public Set<Object> getAdmins() {
        return admins;
    }

    public void setAdmins(Set<Object> admins) {
        this.admins = admins;
    }

    public String getLoginRedirect() {
        return loginRedirect;
    }

    public void setLoginRedirect(String loginRedirect) {
        this.loginRedirect = loginRedirect;
    }

    public String getLogoutRedirect() {
        return logoutRedirect;
    }

    public void setLogoutRedirect(String logoutRedirect) {
        this.logoutRedirect = logoutRedirect;
    }
}