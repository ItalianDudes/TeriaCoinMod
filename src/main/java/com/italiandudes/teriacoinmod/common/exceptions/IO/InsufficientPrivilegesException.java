package com.italiandudes.teriacoinmod.common.exceptions.IO;

import java.io.IOException;

@SuppressWarnings("unused")
public class InsufficientPrivilegesException extends IOException {

    public InsufficientPrivilegesException(String message){
        super(message);
    }

}
