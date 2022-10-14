/*
 *  Copyright (C) 2022 ItalianDudes
 *  Software distributed under the GPLv3 license
 */
package com.italiandudes.teriacoinmod.common.exceptions.IO.socket;

import java.io.IOException;

@SuppressWarnings("unused")
public class ValidatingStreamException extends IOException {
    public ValidatingStreamException(Throwable cause){
        super("Validating stream failed.",cause);
    }
}
