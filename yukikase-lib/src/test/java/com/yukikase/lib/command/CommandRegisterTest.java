package com.yukikase.lib.command;

import com.yukikase.lib.IPermissionHandler;
import com.yukikase.lib.interfaces.ICommand;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class CommandRegisterTest {

    private CommandRegister sut;

    private IPermissionHandler permissionHandler;
    private JavaPlugin plugin;

    @BeforeEach
    void setup() {
        sut = new CommandRegister();

        permissionHandler = mock(IPermissionHandler.class);
        plugin = mock(JavaPlugin.class);
    }

    @Test
    void testRegisterCommands() {
        //arrange
        var commands = new ArrayList<ICommand>();
        var command1 = mock(ICommand.class);
        var command2 = mock(ICommand.class);
        var command3 = mock(ICommand.class);

        when(command1.name()).thenReturn("command1");
        when(command2.name()).thenReturn("command2");
        when(command3.name()).thenReturn("command3");

        commands.add(command1);
        commands.add(command2);
        commands.add(command3);

        var bucketCommand1 = mock(PluginCommand.class);
        var bucketCommand2 = mock(PluginCommand.class);
        var bucketCommand3 = mock(PluginCommand.class);
        when(plugin.getCommand("command1")).thenReturn(bucketCommand1);
        when(plugin.getCommand("command2")).thenReturn(bucketCommand2);
        when(plugin.getCommand("command3")).thenReturn(bucketCommand3);

        //act
        sut.registerCommands(commands, plugin, permissionHandler);

        //assert
        verify(bucketCommand1).setExecutor(any());
        verify(bucketCommand2).setExecutor(any());
    }
}