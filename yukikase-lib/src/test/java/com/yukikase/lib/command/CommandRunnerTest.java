package com.yukikase.lib.command;

import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.testclasses.CommandClassManualPermission;
import com.yukikase.lib.testclasses.CommandClassSingleMethod;
import com.yukikase.lib.testclasses.CommandClassWithAliasAndSubCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandRunnerTest {

    private CommandRunner sut;

    private IPermissionHandler permissionHandler;
    private Command bucketCommand;
    private YukikasePlugin plugin;

    private static Stream<Arguments> commandInput() {
        return Stream.of(
                Arguments.of("multiple", new String[0]),
                Arguments.of("multiple", new String[]{"1"}),
                Arguments.of("multiple", new String[]{"2"}),
                Arguments.of("command", new String[]{"2"}),
                Arguments.of("multiple2", new String[0])
        );
    }
    //TODO: fix these tests

    @BeforeEach
    void setup() {
        permissionHandler = mock(IPermissionHandler.class);
        bucketCommand = mock(Command.class);
        plugin = mock(YukikasePlugin.class);
    }

    @Test
    void testOnCommandWithSingleMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassSingleMethod.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

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
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

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
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

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
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

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
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

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
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(false);


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
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.onCommand(sender, bucketCommand, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, bucketCommand, new String[]{});
        verify(sender, never()).hasPermission(anyString());
    }

    @Test
    void testOnCommandWithMethodPermissionSetToManual() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.onCommand(sender, bucketCommand, "manual", new String[]{});

        //assert
        verify(permissionHandler, never()).playerHasPermission(eq(sender), any(Method.class));
    }

    @Test
    void testOnCommandWithCommandPermissionSetToManual() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassManualPermission.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.onCommand(sender, bucketCommand, "manual", new String[]{});

        //assert
        verify(permissionHandler, never()).playerHasPermission(eq(sender), any(Method.class));
    }

    @ParameterizedTest
    @MethodSource("commandInput")
    void testOnCommandWithMultipleAliases(String input, String[] args) {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(eq(sender), any(Method.class))).thenReturn(true);

        //act
        sut.onCommand(sender, bucketCommand, input, args);

        //assert
        verify(command).onMultiple(sender, bucketCommand, new String[0]);
    }
}