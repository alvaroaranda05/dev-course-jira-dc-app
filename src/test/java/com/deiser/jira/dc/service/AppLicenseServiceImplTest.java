package com.deiser.jira.dc.service;

import com.atlassian.plugin.Plugin;
import com.atlassian.plugin.PluginAccessor;
import com.atlassian.plugin.PluginInformation;
import com.atlassian.upm.api.license.PluginLicenseManager;
import com.atlassian.upm.api.license.entity.PluginLicense;
import com.atlassian.upm.api.util.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class AppLicenseServiceImplTest {

    private static final String  ATLASSIAN_LICENSING_ENABLED = "atlassian-licensing-enabled";

    @Mock
    private PluginLicenseManager pluginLicenseManager;
    @Mock
    private PluginAccessor pluginAccessor;
    @Mock
    private Plugin plugin;
    @Mock
    private PluginInformation pluginInformation;
    @Mock
    private PluginLicense license;

    private AppLicenseServiceImpl sut;

    /*
     Para crear los beans dentro del ComponentAccessor de Jira en el TEST, sólo para el caso
     en el no se instancien los beans por el constructor
     Hay que añadirlo dentro del método inicial: setUp

            new MockComponentWorker()
                .addMock(PluginLicenseManager.class, pluginLicenseManager)
                .init();
    */

    @BeforeEach
    void setUp(){
        initMocks(this);
        sut = new AppLicenseServiceImpl(pluginLicenseManager, pluginAccessor);
    }


    @Test
    void hasLicense_withoutEnabledLicensing_shouldReturnTrue(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "false");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertTrue(result);
        verify(pluginLicenseManager).getPluginKey();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verifyZeroInteractions(license);
    }


    @Test
    void hasLicense_withEnabledLicensingAndNoDefined_shouldReturnFalse(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "true");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        when(pluginLicenseManager.getLicense()).thenReturn(Option.none());

        //When
        boolean result = sut.hasLicense();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getPluginKey();
        verify(pluginLicenseManager).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verifyZeroInteractions(license);
    }

    @Test
    void hasLicense_withEnabledLicensingAndNoValid_shouldReturnFalse(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "true");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        when(pluginLicenseManager.getLicense()).thenReturn(Option.some(license));
        when(license.isValid()).thenReturn(false);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getPluginKey();
        verify(pluginLicenseManager, Mockito.times(2)).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verify(license).isValid();
        verifyNoMoreInteractions(license);
    }

    @Test
    void hasLicense_withEnabledLicensingAndNoActive_shouldReturnFalse(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "true");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        when(pluginLicenseManager.getLicense()).thenReturn(Option.some(license));
        when(license.isValid()).thenReturn(true);
        when(license.isActive()).thenReturn(false);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getPluginKey();
        verify(pluginLicenseManager, Mockito.times(3)).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verify(license).isValid();
        verify(license).isActive();
        verifyNoMoreInteractions(license);
    }

    @Test
    void hasLicense_withEnabledLicensingAndDefinedAndValidAndActive_shouldReturnTrue(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "true");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        when(pluginLicenseManager.getLicense()).thenReturn(Option.some(license));
        when(license.isValid()).thenReturn(true);
        when(license.isActive()).thenReturn(true);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertTrue(result);
        verify(pluginLicenseManager).getPluginKey();
        verify(pluginLicenseManager, Mockito.times(3)).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verify(license).isValid();
        verify(license).isActive();
        verifyNoMoreInteractions(license);
    }

    @Test
    void hasLicense_withErrorWhileGettingPluginInformation_shouldReturnFalse(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenThrow(Exception.class);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getPluginKey();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verifyZeroInteractions(plugin);
        verifyZeroInteractions(pluginInformation);
        verifyZeroInteractions(license);
    }

    @Test
    void hasLicense_withErrorWhileGettingLicense_shouldReturnFalse(){
        //Given
        when(pluginLicenseManager.getPluginKey()).thenReturn("PLUGIN_KEY");
        when(pluginAccessor.getPlugin(anyString())).thenReturn(plugin);
        when(plugin.getPluginInformation()).thenReturn(pluginInformation);

        Map<String, String> parameters = new HashMap();
        parameters.put(ATLASSIAN_LICENSING_ENABLED, "true");
        when(pluginInformation.getParameters()).thenReturn(parameters);

        when(pluginLicenseManager.getLicense()).thenThrow(Exception.class);

        //When
        boolean result = sut.hasLicense();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getPluginKey();
        verify(pluginLicenseManager).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(pluginAccessor).getPlugin(anyString());
        verifyNoMoreInteractions(pluginAccessor);
        verify(plugin).getPluginInformation();
        verifyNoMoreInteractions(plugin);
        verify(pluginInformation).getParameters();
        verifyNoMoreInteractions(pluginInformation);
        verifyZeroInteractions(license);
    }


    @Test
    void isEvaluation_withEvaluationLicense_shouldReturnTrue(){
        //Hemos utilizado un Spy a modo de ejemplo para mockear los métodos públicos a los que se llama.
        //Given
        AppLicenseServiceImpl spySut = spy(sut);
        doReturn(true).when(spySut).hasLicense();

        when(pluginLicenseManager.getLicense()).thenReturn(Option.some(license));
        when(license.isEvaluation()).thenReturn(true);

        //When
        boolean result = spySut.isEvaluation();

        //Then
        assertTrue(result);
        verify(pluginLicenseManager).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(license).isEvaluation();
        verifyNoMoreInteractions(license);
        verifyZeroInteractions(pluginAccessor);
        verifyZeroInteractions(plugin);
        verifyZeroInteractions(pluginInformation);
    }

    @Test
    void isEvaluation_withoutLicense_shouldReturnFalse(){
        //Hemos utilizado un Spy a modo de ejemplo para mockear los métodos públicos a los que se llama.
        //Given
        AppLicenseServiceImpl spySut = spy(sut);
        doReturn(false).when(spySut).hasLicense();

        //When
        boolean result = spySut.isEvaluation();

        //Then
        assertFalse(result);
        verifyZeroInteractions(pluginLicenseManager);
        verifyZeroInteractions(pluginAccessor);
        verifyZeroInteractions(plugin);
        verifyZeroInteractions(pluginInformation);
        verifyZeroInteractions(license);
    }

    @Test
    void isEvaluation_withNoEvaluationLicense_shouldReturnFalse(){
        //Hemos utilizado un Spy a modo de ejemplo para mockear los métodos públicos a los que se llama.
        //Given
        AppLicenseServiceImpl spySut = spy(sut);
        doReturn(true).when(spySut).hasLicense();

        when(pluginLicenseManager.getLicense()).thenReturn(Option.some(license));
        when(license.isEvaluation()).thenReturn(false);

        //When
        boolean result = spySut.isEvaluation();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verify(license).isEvaluation();
        verifyNoMoreInteractions(license);
        verifyZeroInteractions(pluginAccessor);
        verifyZeroInteractions(plugin);
        verifyZeroInteractions(pluginInformation);
    }

    @Test
    void isEvaluation_withErrorWhileGettingLicense_shouldReturnFalse(){
        //Hemos utilizado un Spy a modo de ejemplo para mockear los métodos públicos a los que se llama.
        //Given
        AppLicenseServiceImpl spySut = spy(sut);
        doReturn(true).when(spySut).hasLicense();

        when(pluginLicenseManager.getLicense()).thenThrow(Exception.class);

        //When
        boolean result = spySut.isEvaluation();

        //Then
        assertFalse(result);
        verify(pluginLicenseManager).getLicense();
        verifyNoMoreInteractions(pluginLicenseManager);
        verifyZeroInteractions(license);
        verifyZeroInteractions(pluginAccessor);
        verifyZeroInteractions(plugin);
        verifyZeroInteractions(pluginInformation);
    }
}