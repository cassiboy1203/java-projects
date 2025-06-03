package com.yukikase.lib.command;

import com.yukikase.lib.YukikasePlugin;
import com.yukikase.lib.interfaces.ICommand;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.lib.testclasses.ServerTest;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

class CommandRegisterTest {

    private CommandRegister sut;

    private IPermissionHandler permissionHandler;
    private YukikasePlugin plugin;
    private ServerTest server;

    @BeforeEach
    void setup() {
        sut = new CommandRegister();

        permissionHandler = mock(IPermissionHandler.class);
        plugin = mock(YukikasePlugin.class);
        server = spy(new ServerTest());

        when(plugin.getServer()).thenReturn(server);
        when(server.getCommandMap()).thenCallRealMethod();
    }

    //    @Test
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

        //act
        sut.registerCommands(commands, plugin, permissionHandler);

        //assert
        verify(server.getCommandMap()).register(eq("command1"), any(CommandRunner.class));
        verify(server.getCommandMap()).register(eq("command2"), any(CommandRunner.class));
        verify(server.getCommandMap()).register(eq("command3"), any(CommandRunner.class));
    }
}