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

    @Before
    public void setUp(){
        mockContext = mock(Context.class);
    }

    @Test
    public void testValidPassword(){
        String validPassword = "ValidPass";
        boolean result = ValidationUtils.validatePassword(mockContext, validPassword);
        assertTrue(result);
    }
    // TODO test password > 32 char
    // TODO test password < 8 char

    @Test
    public void testValidConfirmPassword(){
        String validPassword = "ValidPass";
        String confirmPassword = "ValidPass";
        assertTrue(ValidationUtils.validatePassword(mockContext,validPassword,confirmPassword));
    }
    // TODO test password not matching
    // TODO test password not matching that is invalid (i.e. <8 char or >32 char)

    @Test
    public void testValidNames(){
        String firstName = "John";
        String lastName = "Doe";
        assertTrue(ValidationUtils.validateNames(mockContext, firstName, lastName));
    }
    // TODO test empty first name
    // TODO test empty last name
    // TODO test >32 char first name
    // TODO test >32 char last name

    /*
    @Test
    public void testInvalidLongPassword(){
        String longPassword = "0123456789012345678901234567890123";
        boolean result = ValidationUtils.validatePassword(mockContext, longPassword);
        assertFalse(result);
    }
    */
}
