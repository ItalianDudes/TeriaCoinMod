/*
 *  Copyright (C) 2022 ItalianDudes
 *  Software distributed under the GPLv3 license
 */
package com.italiandudes.teriacoinmod.common.exceptions.IO.socket;

@SuppressWarnings("unused")
public class NullStreamException extends NullPointerException{
    public NullStreamException(){
        super("The stream is null.");
    }
}
