package com.yukikase.lib;

import com.yukikase.lib.exceptions.NoPermissionFoundException;
import com.yukikase.lib.testclasses.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultPermissionHandlerTest {

    private DefaultPermissionHandler sut;
    private YukikasePlugin plugin;
    private Player player;

    @BeforeEach
    void setUp() {
        plugin = mock(PluginClass.class);
        player = mock(Player.class);

        sut = new DefaultPermissionHandler(plugin);
    }

    @Test
    void testPlayerHasPermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(true);

        //assert
        var actual = sut.playerHasPermission(player, "permission");

        //assert
        assertTrue(actual);
    }

    @Test
    void testPlayerDoesNotHavePermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(false);

        //assert
        var actual = sut.playerHasPermission(player, "permission");

        //assert
        assertFalse(actual);
    }

    @Test
    void testPlayerHasPermissionWithStingArray() {
        //arrange
        when(player.hasPermission("plugin.permission.test")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, "permission", "test");

        //assert
        assertTrue(actual);
    }

    @Test
    void testPlayerDoesNotHavePermissionWithStingArray() {
        //arrange
        when(player.hasPermission("permission.test")).thenReturn(false);

        //act
        var actual = sut.playerHasPermission(player, "permission", "test");

        //assert
        assertFalse(actual);
    }

    @Test
    void playerHasPermissionOfCommand() {
        //arrange
        when(player.hasPermission("plugin.class")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, CommandClassSingleMethod.class);

        //assert
        assertTrue(actual);
    }

    @Test
    void playerHasPermissionOfCommandWithoutPermissionOnClassThrowsNoPermissionFoundException() {
        //arrange
        plugin = mock(YukikasePlugin.class);
        sut = new DefaultPermissionHandler(plugin);

        //assert
        var exception = assertThrows(NoPermissionFoundException.class, () -> {
            //act
            sut.playerHasPermission(player, CommandClassWithAliasAndSubCommands.class);
        });

        assertEquals(String.format(NoPermissionFoundException.CLASS_DOES_NOT_HAVE_PERMISSION, CommandClassWithAliasAndSubCommands.class.getName()), exception.getMessage());
    }

    @Test
    void playerHasPermissionOfCommandWithIgnorePluginPrefix() {
        //arrange
        when(player.hasPermission("class")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, PermissionNoPluginPrefixClass.class);

        //assert
        assertTrue(actual);
    }

    @Test
    void playerHasPermissionOfCommandWithIgnorePrefix() {
        //arrange
        when(player.hasPermission("class")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, PermissionNoPrefixClass.class);

        //assert
        assertTrue(actual);
    }

    @Test
    void playerHasPermissionOfMethod() throws NoSuchMethodException {
        //arrange
        when(player.hasPermission("plugin.class.method")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, CommandClassSingleMethod.class.getMethod("onCommand", CommandSender.class, Command.class, String[].class));

        //assert
        assertTrue(actual);
    }

    @Test
    void playerHasPermissionOfMethodWithoutPermissionOnMethodThrowsNoPermissionFoundException() {
        //arrange
        plugin = mock(YukikasePlugin.class);
        sut = new DefaultPermissionHandler(plugin);

        //assert
        var exception = assertThrows(NoPermissionFoundException.class, () -> {
            //act
            sut.playerHasPermission(player, CommandClassWithAliasAndSubCommands.class.getMethod("onCommand", CommandSender.class, Command.class, String[].class));
        });

        assertEquals(String.format(NoPermissionFoundException.METHOD_DOES_NOT_HAVE_PERMISSION, "onCommand"), exception.getMessage());
    }

    @Test
    void playerHasPermissionOfMethodWithIgnorePrefix() throws NoSuchMethodException {
        //arrange
        when(player.hasPermission("method")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, CommandClassWithAliasAndSubCommands.class.getMethod("onAlias", CommandSender.class, Command.class, String[].class));

        //assert
        assertTrue(actual);
    }

    @Test
    void playerHasPermissionOfClassWithAdditionalArgs() {
        //arrange
        when(player.hasPermission("plugin.class.add1.add2")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, CommandClassSingleMethod.class, "add1", "add2");

        //assert
        assertTrue(actual);
    }
}