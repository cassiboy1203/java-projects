package com.yukikase.lib.command;

import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.lib.testclasses.CommandClassSingleMethod;
import com.yukikase.lib.testclasses.CommandClassWithAliasAndSubCommands;
import com.yukikase.lib.testclasses.PermissionRegisterTest;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandRunnerTest {

    private CommandRunner sut;

    private IPermissionHandler permissionHandler;
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
        plugin = mock(YukikasePlugin.class);
    }

    @Test
    void testOnCommandWithSingleMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassSingleMethod.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallAliasMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, "alias", new String[]{});

        //assert
        verify(command).onAlias(sender, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallDefaultMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, new String[]{});
    }

    @Test
    void testOnCommandWithSubMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        when(permissionHandler.getPermission("TEST_BASE")).thenReturn(PermissionRegisterTest.TEST_BASE);
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(sender, PermissionRegisterTest.TEST_BASE)).thenReturn(true);

        //act
        sut.execute(sender, "command", new String[]{"sub"});

        //assert
        verify(command).onSubCommand(sender, new String[]{});
    }

    @Test
    void testOnCommandWithAliasSubMethod() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, "alias", new String[]{"sub2"});

        //assert
        verify(command).onSubCommandWithAlias(sender, new String[]{});
    }

    @Test
    void testOnCommandWithAliasMethodCallDefaultMethodWithoutPermission() {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        when(permissionHandler.getPermission(anyString())).thenReturn(PermissionRegisterTest.TEST_BASE);
        sut = new CommandRunner(command, permissionHandler, plugin);

        when(permissionHandler.playerHasPermission(sender, PermissionRegisterTest.TEST_BASE)).thenReturn(false);

        //assert
        assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.execute(sender, "command", new String[]{"sub"});
        });

        verify(command, never()).onCommand(sender, new String[]{});
    }

    @Test
    void testOnCommandWithSenderNotAnPlayer() {
        //arrange
        var sender = mock(CommandSender.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, "command", new String[]{});

        //assert
        verify(command).onCommand(sender, new String[]{});
        verify(sender, never()).hasPermission(anyString());
    }

    @ParameterizedTest
    @MethodSource("commandInput")
    void testOnCommandWithMultipleAliases(String input, String[] args) {
        //arrange
        var sender = mock(Player.class);

        var command = spy(CommandClassWithAliasAndSubCommands.class);
        sut = new CommandRunner(command, permissionHandler, plugin);

        //act
        sut.execute(sender, input, args);

        //assert
        verify(command).onMultiple(sender, new String[0]);
    }
}