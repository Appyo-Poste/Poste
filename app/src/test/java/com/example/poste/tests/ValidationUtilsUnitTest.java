package com.example.poste.tests;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import android.content.Context;
import android.widget.Toast;

import com.example.poste.utils.ValidationUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class ValidationUtilsUnitTest {
    @Mock
    private Context mockContext;

    @Mock
    private Toast mockToast;

    @Before
    public void setUp(){
        mockContext = mock(Context.class);
        mockToast = mock(Toast.class);

    }

    @Test
    public void testValidPassword(){
        String validPassword = "ValidPass";
        boolean result = ValidationUtils.validatePassword(mockContext, validPassword);
        assertTrue(result);
    }

    /*
    @Test
    public void testInvalidShortPassword(){
        String shortPassword = "short";
        boolean result = ValidationUtils.validatePassword(mockContext, shortPassword);
        assertFalse(result);
    }

    @Test
    public void testInvalidLongPassword(){
        String longPassword = "0123456789012345678901234567890123";
        boolean result = ValidationUtils.validatePassword(mockContext, longPassword);
        assertFalse(result);
    }
    */
}
