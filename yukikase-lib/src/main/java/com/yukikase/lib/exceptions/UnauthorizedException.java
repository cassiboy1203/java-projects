package com.yukikase.lib.exceptions;

public class UnauthorizedException extends RuntimeException {
    public static final String EC_PLAYER_UNAUTHORIZED_COMMAND = "Player does not have permission to run this command";
    public static final String PLAYER_UNAUTHORIZED_SUB_COMMAND = "Player does not have permission to run this subcommand";

    public UnauthorizedException(String message) {
        super(message);
    }
}
