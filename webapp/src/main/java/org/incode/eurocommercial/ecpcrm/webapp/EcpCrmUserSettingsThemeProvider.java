package org.incode.eurocommercial.ecpcrm.webapp;

import org.apache.isis.applib.DomainObjectContainer;
import org.apache.isis.core.metamodel.services.ServicesInjector;
import org.apache.isis.core.runtime.system.context.IsisContext;

import org.apache.isis.core.runtime.system.session.IsisSessionFactory;
import org.isisaddons.module.settings.dom.UserSetting;
import org.isisaddons.module.settings.dom.UserSettingsService;
import org.isisaddons.module.settings.dom.UserSettingsServiceRW;
import org.isisaddons.module.settings.dom.jdo.UserSettingJdo;

import de.agilecoders.wicket.core.settings.ActiveThemeProvider;
import de.agilecoders.wicket.core.settings.IBootstrapSettings;
import de.agilecoders.wicket.core.settings.ITheme;
import de.agilecoders.wicket.core.settings.SessionThemeProvider;
import de.agilecoders.wicket.core.settings.ThemeProvider;

public class EcpCrmUserSettingsThemeProvider implements ActiveThemeProvider {

    static final String ACTIVE_THEME = "activeTheme";

    private final IBootstrapSettings settings;

    public EcpCrmUserSettingsThemeProvider(final IBootstrapSettings settings) {
        this.settings = settings;
    }

    // //////////////////////////////////////

    @Override
    public ITheme getActiveTheme() {
        if(getIsisSessionFactory().getSpecificationLoader().isInitialized()) {
            final String themeName = getIsisSessionFactory().doInSession(() -> {
                final String currentUserName = currentUserName();

                final Class<UserSettingsService> serviceClass = UserSettingsService.class;
                final UserSettingsService userSettingsService = lookupService(serviceClass);

                final UserSetting activeTheme = userSettingsService.find(currentUserName, ACTIVE_THEME);
                return activeTheme != null ? activeTheme.valueAsString() : null;
            });
            return themeFor(themeName);
        }
        return new SessionThemeProvider().getActiveTheme();
    }

    @Override
    public void setActiveTheme(final String themeName) {
        getIsisSessionFactory().doInSession(() -> {
            final String currentUserName = currentUserName();

            final UserSettingsServiceRW userSettingsService = getServicesInjector().lookupService(UserSettingsServiceRW.class);
            final UserSettingJdo activeTheme = (UserSettingJdo) userSettingsService.find(currentUserName, ACTIVE_THEME);
            if(activeTheme != null) {
                activeTheme.updateAsString(themeName);
            } else {
                userSettingsService.newString(currentUserName, ACTIVE_THEME, "Active Bootstrap theme for user", themeName);
            }
        });
    }

    @Override
    public void setActiveTheme(final ITheme theme) {
        setActiveTheme(theme.name());
    }

    private ITheme themeFor(final String themeName) {
        final ThemeProvider themeProvider = settings.getThemeProvider();
        if(themeName != null) {
            for (final ITheme theme : themeProvider.available()) {
                if (themeName.equals(theme.name()))
                    return theme;
            }
        }
        return themeProvider.defaultTheme();
    }

    // //////////////////////////////////////

    protected <T> T lookupService(final Class<T> serviceClass) {
        return getServicesInjector().lookupService(serviceClass);
    }

    protected String currentUserName() {
        final DomainObjectContainer container = getServicesInjector().lookupService(DomainObjectContainer.class);
        return container.getUser().getName();
    }

    // //////////////////////////////////////

    ServicesInjector getServicesInjector() {
        return getIsisSessionFactory().getServicesInjector();
    }

    IsisSessionFactory getIsisSessionFactory() {
        return IsisContext.getSessionFactory();
    }



}
