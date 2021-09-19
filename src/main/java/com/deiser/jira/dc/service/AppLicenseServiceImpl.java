package com.deiser.jira.dc.service;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginInformation;
import com.atlassian.upm.api.license.PluginLicenseManager;
import org.apache.log4j.Logger;


public class AppLicenseServiceImpl implements AppLicenseService {

    private static final Logger LOGGER = Logger.getLogger(AppLicenseServiceImpl.class);

    private static final String  ATLASSIAN_LICENSING_ENABLED = "atlassian-licensing-enabled";

    private final PluginLicenseManager pluginLicenseManager;
    private final PluginAccessor pluginAccessor;


    public AppLicenseServiceImpl(PluginLicenseManager pluginLicenseManager,
                                 PluginAccessor pluginAccessor) {
        this.pluginLicenseManager = pluginLicenseManager;
        this.pluginAccessor = pluginAccessor;
    }

    @Override
    public boolean hasLicense() {
        try {
            if (!isLicensingEnabled()) return true;

            return pluginLicenseManager.getLicense().isDefined() &&
                    pluginLicenseManager.getLicense().get().isValid() &&
                    pluginLicenseManager.getLicense().get().isActive();
        } catch (Exception ex) {
            LOGGER.error("There was an error verifying the license", ex);
            return false;
        }
    }

    @Override
    public boolean isEvaluation() {
        try {
            return hasLicense() &&
                    pluginLicenseManager.getLicense().get().isEvaluation();
        } catch (Exception ex) {
            LOGGER.error("There was an error verifying the license", ex);
            return false;
        }
    }

    private Boolean isLicensingEnabled() {
        Plugin plugin = pluginAccessor.getPlugin(pluginLicenseManager.getPluginKey());
        PluginInformation pluginInformation = plugin.getPluginInformation();
        return Boolean.valueOf(pluginInformation.getParameters().get(ATLASSIAN_LICENSING_ENABLED));
    }
}
