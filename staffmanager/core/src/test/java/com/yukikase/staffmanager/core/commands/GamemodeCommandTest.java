package com.yukikase.staffmanager.core.commands;

import com.yukikase.lib.exceptions.InvalidCommandUsageException;
import com.yukikase.lib.exceptions.PlayerNotFoundException;
import com.yukikase.lib.exceptions.UnauthorizedException;
import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.staffmanager.core.PermissionRegister;
import com.yukikase.staffmanager.core.staffmode.IStaffMode;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GamemodeCommandTest {
    private GamemodeCommand sut;

    private IStaffMode staffMode;
    private IPermissionHandler permissionHandler;
    private Player player;
    private Command command;
    private MockedStatic<Bukkit> bukkit;

    @BeforeEach
    void setUp() {
        staffMode = mock(IStaffMode.class);
        permissionHandler = mock(IPermissionHandler.class);
        player = mock(Player.class);
        command = mock(Command.class);
        bukkit = mockStatic(Bukkit.class);

        sut = new GamemodeCommand(permissionHandler, staffMode);
    }

    @AfterEach
    void tearDown() {
        bukkit.close();
    }

    @Test
    void testName() {
        //arrange

        //act
        var actual = sut.name();

        //assert
        assertEquals("gamemode", actual);
    }

    @Test
    void testOnCommand() {
        //arrange

        //assert
        assertThrows(InvalidCommandUsageException.class, () -> {
            //act
            sut.onCommand(player, new String[0]);
        });
    }

    @Test
    void testOnCreative() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(true);

        //act
        sut.onCreative(player, new String[0]);

        //assert
        verify(player).setGameMode(GameMode.CREATIVE);
        verify(player).sendMessage(String.format(GamemodeCommand.GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, "creative"));
    }

    @Test
    void testOnSurvival() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(true);

        //act
        sut.onSurvival(player, new String[0]);

        //assert
        verify(player).setGameMode(GameMode.SURVIVAL);
        verify(player).sendMessage(String.format(GamemodeCommand.GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, "survival"));
    }

    @Test
    void testOnSpectator() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(true);

        //act
        sut.onSpectator(player, new String[0]);

        //assert
        verify(player).setGameMode(GameMode.SPECTATOR);
        verify(player).sendMessage(String.format(GamemodeCommand.GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, "spectator"));
    }

    @Test
    void testOnAdventure() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(true);

        //act
        sut.onAdventure(player, new String[0]);

        //assert
        verify(player).setGameMode(GameMode.ADVENTURE);
        verify(player).sendMessage(String.format(GamemodeCommand.GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, "adventure"));
    }

    @Test
    void testChangeGamemodePlayerNotInStaffMode() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);

        //act
        sut.onCreative(player, new String[0]);

        //assert
        verify(player).setGameMode(GameMode.CREATIVE);
        verify(player).sendMessage(String.format(GamemodeCommand.GAMEMODE_CHANGED_MESSAGE, ChatColor.YELLOW, "creative"));
    }

    @Test
    void testChangeGamemodePlayerNotInStaffModeAndNoPermissionThrowsUnauthorizedException() {
        //arrange
        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(false);

        //assert
        assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.onCreative(player, new String[0]);
        });
    }

    @Test
    void testChangeGamemodeOtherPlayerPlayerInStaffMode() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(false);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //act
        sut.onCreative(player, new String[]{"other"});

        //assert
        verify(otherPlayer).setGameMode(GameMode.CREATIVE);
    }

    @Test
    void testChangeGamemodePlayerInStaffModeAndNoPermissionToChangeOtherThrowsUnauthorizedException() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)).thenReturn(false);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //assert
        assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.onCreative(player, new String[]{"other"});
        });

        verify(otherPlayer, never()).setGameMode(GameMode.CREATIVE);
    }

    @Test
    void testChangeGamemodeOtherPlayer() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(true);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //act
        sut.onCreative(player, new String[]{"other"});

        //assert
        verify(otherPlayer).setGameMode(GameMode.CREATIVE);
    }


    @Test
    void testChangeGamemodeOtherPlayerOtherNotFoundThrowsPlayerNotFoundException() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(true);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(null);

        //assert
        assertThrows(PlayerNotFoundException.class, () -> {
            //act
            sut.onCreative(player, new String[]{"other"});
        });

        verify(player, never()).setGameMode(GameMode.CREATIVE);
        verify(otherPlayer, never()).setGameMode(GameMode.CREATIVE);
    }


    @Test
    void testChangeGamemodeWithMoreThen1Argument() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(true);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //assert
        assertThrows(InvalidCommandUsageException.class, () -> {
            //act
            sut.onCreative(player, new String[]{"other", "arg2"});
        });

        verify(player, never()).setGameMode(GameMode.CREATIVE);
        verify(otherPlayer, never()).setGameMode(GameMode.CREATIVE);
    }


    @Test
    void testChangeGamemodeOtherPlayerWithNoPermissionOutsideStaffMode() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(true);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //assert
        var exception = assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.onCreative(player, new String[]{"other"});
        });

        assertEquals(GamemodeCommand.PLAYER_NO_PERMISSON_GAMEMODE_OTHER_STAFF, exception.getMessage());

        verify(player, never()).setGameMode(GameMode.CREATIVE);
        verify(otherPlayer, never()).setGameMode(GameMode.CREATIVE);
    }


    @Test
    void testChangeGamemodeOtherPlayerWithNoPermission() {
        //arrange
        var otherPlayer = mock(Player.class);

        when(staffMode.isInStaffMode(player)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_BASE)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GAMEMODE_OTHER)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.GAMEMODE_OTHER)).thenReturn(false);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //assert
        var exception = assertThrows(UnauthorizedException.class, () -> {
            //act
            sut.onCreative(player, new String[]{"other"});
        });

        assertEquals(GamemodeCommand.PLAYER_NO_PERMISSION_GAMEMODE_OTHER, exception.getMessage());

        verify(player, never()).setGameMode(GameMode.CREATIVE);
        verify(otherPlayer, never()).setGameMode(GameMode.CREATIVE);
    }

    @Test
    void testChangeGamemodeWithNonPlayerSender() {
        //arrange
        var sender = mock(CommandSender.class);
        var otherPlayer = mock(Player.class);

        bukkit.when(() -> Bukkit.getPlayer("other")).thenReturn(otherPlayer);

        //act
        sut.onCreative(sender, new String[]{"other"});

        //assert
        verify(otherPlayer).setGameMode(GameMode.CREATIVE);
    }

    @Test
    void testChangeGamemodeWithNonPlayerSenderWithoutArgumentThrowsInvalidCommandException() {
        //arrange
        var sender = mock(CommandSender.class);

        //assert
        assertThrows(InvalidCommandUsageException.class, () -> {
            //act
            sut.onCreative(sender, new String[0]);
        });
    }
}