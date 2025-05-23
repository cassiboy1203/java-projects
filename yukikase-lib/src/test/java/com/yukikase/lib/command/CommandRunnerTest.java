package com.yukikase.lib.command;

import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.testclasses.CommandClassSingleMethod;
import com.yukikase.lib.testclasses.CommandClassWithAliasAndSubCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandRunnerTest {

    private CommandRunner sut;

    private IPermissionHandler permissionHandler;
    private Command bucketCommand;

    @BeforeEach
    void setup() {
        permissionHandler = mock(IPermissionHandler.class);
        bucketCommand = mock(Command.class);
    }

    @Test
    void testOnCommandWithSingleMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassSingleMethod.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallAliasMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, "alias", new String[]{});

        //assert
        verify(command).onAlias(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallDefaultMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithSubMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, "command", new String[]{"sub"});

        //assert
        verify(command).onSubCommand(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithAliasSubMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, "alias", new String[]{"sub2"});

        //assert
        verify(command).onSubCommandWithAlias(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallDefaultMethodWithoutPermission() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        when(permissionHandler.getPermissionOffMethod(eq(command.getClass()), any())).thenReturn("method");

        when(sender.hasPermission("method")).thenReturn(false);


        //assert
        assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.onCommand(sender, bucketCommand, "command", new String[]{});
        });

        verify(command, never()).onCommand(sender, bucketCommand, new String[]{});
    }

    @Test
    void testOnCommandWithSenderNotAnPlayer() {
        //arrange
        var sender = mock(CommandSender.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler);

        //act
        sut.onCommand(sender, bucketCommand, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, bucketCommand, new String[]{});
        verify(sender, never()).hasPermission(anyString());
    }
}