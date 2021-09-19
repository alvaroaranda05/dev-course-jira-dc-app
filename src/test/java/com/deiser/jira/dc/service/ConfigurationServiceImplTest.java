package com.deiser.jira.dc.service;

import com.deiser.jira.dc.ao.data.AOConfiguration;
import com.deiser.jira.dc.ao.data.ConfigurationMgr;
import com.deiser.jira.dc.model.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

class ConfigurationServiceImplTest {

    private static final String CONFIG_VALUE = "VALUE";
    private static final String CONFIG_KEY = "KEY";

    @Mock
    private ConfigurationMgr configurationMgr;
    @Mock
    private AOConfiguration entity;

    private ConfigurationServiceImpl sut;

    @BeforeEach
    void setUp(){
        initMocks(this);

        when(entity.getValue()).thenReturn(CONFIG_VALUE);
        when(entity.getKey()).thenReturn(CONFIG_KEY);

        sut = new ConfigurationServiceImpl(configurationMgr);
    }

    @Test
    void get_withConfigurations_shouldReturnConfigurations(){
        //Given
        AOConfiguration[] configurationList = {entity};
        when(configurationMgr.get()).thenReturn(configurationList);

        //When
        List<Configuration> result = sut.get();

        //Then
        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .element(0)
                .hasFieldOrPropertyWithValue("value", CONFIG_VALUE)
                .hasFieldOrPropertyWithValue("key", CONFIG_KEY);
        verify(configurationMgr).get();
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void get_withoutConfiguration_shouldReturnEmptyList() {
        //Given
        AOConfiguration[] configurationList = {};
        when(configurationMgr.get()).thenReturn(configurationList);

        //When
        List<Configuration> result = sut.get();

        //Then
        assertThat(result)
                .isNotNull()
                .isEmpty();
        verify(configurationMgr).get();
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void getByKey_withExistedConfiguration_shouldReturnIt(){
        //Given
        when(configurationMgr.existKey(anyString())).thenReturn(true);
        when(configurationMgr.getConfiguration(anyString())).thenReturn(entity);

        //When
        Configuration result = sut.get(CONFIG_KEY);

        //Then
        assertThat(result)
                .isNotNull()
                .isInstanceOf(Configuration.class);
        assertThat(result.getValue()).isEqualTo(CONFIG_VALUE);
        assertThat(result.getKey()).isEqualTo(CONFIG_KEY);

        verify(configurationMgr).existKey(anyString());
        verify(configurationMgr).getConfiguration(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void getByKey_withNoExistedConfiguration_shouldReturnNull(){
        //Given
        when(configurationMgr.existKey(anyString())).thenReturn(false);

        //When
        Configuration result = sut.get(CONFIG_KEY);

        //Then
        assertThat(result)
                .isNull();

        verify(configurationMgr).existKey(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void exist_shouldCallExistKeyMethodOfManager(){
        //Given
        when(configurationMgr.existKey(anyString())).thenReturn(true);

        //When
        boolean result = sut.exist(CONFIG_KEY);

        //Then
        assertThat(result).isTrue();

        verify(configurationMgr).existKey(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void create_withExistedValue_shouldReturnNull(){
        //Given
        when(configurationMgr.existKey(anyString())).thenReturn(true);

        //When
        Configuration result = sut.create(CONFIG_KEY, CONFIG_VALUE);

        //Then
        assertThat(result).isNull();

        verify(configurationMgr).existKey(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void create_withNoExistedValue_shouldCreateAndReturn(){
        //Given
        when(configurationMgr.existKey(anyString())).thenReturn(false);
        when(configurationMgr.create(anyString(), anyString())).thenReturn(entity);

        //When
        Configuration result = sut.create(CONFIG_KEY, CONFIG_VALUE);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getKey()).isEqualTo(CONFIG_KEY);
        assertThat(result.getValue()).isEqualTo(CONFIG_VALUE);

        verify(configurationMgr).existKey(anyString());
        verify(configurationMgr).create(anyString(), anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void update_withNoExistedValue_shouldReturnNullAndNotSave(){
        //Given
        when(configurationMgr.getConfiguration(anyString())).thenReturn(null);

        //When
        Configuration result = sut.update(CONFIG_KEY, CONFIG_VALUE);

        //Then
        assertThat(result).isNull();

        verify(configurationMgr).getConfiguration(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void update_withExistedValue_shouldUpdateThePreviousValue(){
        //Given
        when(configurationMgr.getConfiguration(anyString())).thenReturn(entity);
        doNothing().when(entity).setValue(anyString());
        doNothing().when(configurationMgr).save(any(AOConfiguration.class));

        //When
        Configuration result = sut.update(CONFIG_KEY, CONFIG_VALUE);

        //Then
        assertThat(result).isNotNull();

        verify(configurationMgr).getConfiguration(anyString());
        verify(configurationMgr).save(any(AOConfiguration.class));
        verifyNoMoreInteractions(configurationMgr);
        verify(entity).getKey();
        verify(entity).getValue();
        verify(entity).setValue(anyString());
        verifyNoMoreInteractions(entity);
    }

    @Test
    void delete_withNoExistedValue_shouldReturnFalseAndNotDelete(){
        //Given
        when(configurationMgr.getConfiguration(anyString())).thenReturn(null);

        //When
        boolean result = sut.delete(CONFIG_KEY);

        //Then
        assertThat(result).isFalse();

        verify(configurationMgr).getConfiguration(anyString());
        verifyNoMoreInteractions(configurationMgr);
    }

    @Test
    void delete_withExistedValue_shouldReturnTrueAndDeleteTheValue(){
        //Given
        when(configurationMgr.getConfiguration(anyString())).thenReturn(entity);
        doNothing().when(configurationMgr).delete(entity);

        //When
        boolean result = sut.delete(CONFIG_KEY);

        //Then
        assertThat(result).isTrue();
        ArgumentCaptor<AOConfiguration> entityArgumentCaptor = ArgumentCaptor.forClass(AOConfiguration.class);

        verify(configurationMgr).getConfiguration(anyString());
        verify(configurationMgr).delete(entityArgumentCaptor.capture());
        verifyNoMoreInteractions(configurationMgr);

        assertThat(entityArgumentCaptor.getValue().getValue()).isEqualTo(CONFIG_VALUE);
        assertThat(entityArgumentCaptor.getValue().getKey()).isEqualTo(CONFIG_KEY);
    }
}