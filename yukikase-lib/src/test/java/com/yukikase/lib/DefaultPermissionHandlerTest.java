package com.yukikase.lib;

import com.yukikase.lib.exceptions.NoPermissionFoundException;
import com.yukikase.lib.exceptions.NoPermissionRegisterFound;
import com.yukikase.lib.permission.DefaultPermissionHandler;
import com.yukikase.lib.testclasses.PermissionRegisterTest;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DefaultPermissionHandlerTest {

    private DefaultPermissionHandler sut;
    private Player player;
    private YukikasePlugin plugin;

    @BeforeEach
    void setUp() {
        player = mock(Player.class);
        plugin = mock(YukikasePlugin.class);

        doReturn(PermissionRegisterTest.class).when(plugin).permissionRegister();

        sut = new DefaultPermissionHandler(plugin);
    }

    @Test
    void testPlayerHasPermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, "permission");

        //assert
        assertTrue(actual);
    }

    @Test
    void testPlayerDoesNotHavePermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(false);

        //act
        var actual = sut.playerHasPermission(player, "permission");

        //assert
        assertFalse(actual);
    }

    @Test
    void testPlayerHasPermissionWithPermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(true);

        //act
        var actual = sut.playerHasPermission(player, PermissionRegisterTest.TEST_BASE);

        //assert
        assertTrue(actual);
    }

    @Test
    void testPlayerDoesNotHavePermissionWithPermission() {
        //arrange
        when(player.hasPermission("permission")).thenReturn(false);

        //act
        var actual = sut.playerHasPermission(player, PermissionRegisterTest.TEST_BASE);

        //assert
        assertFalse(actual);
    }

    @Test
    void testGetPermission() {
        //arrange

        //act
        var actual = sut.getPermission("TEST_BASE");

        //assert
        assertEquals(PermissionRegisterTest.TEST_BASE, actual);
    }

    @Test
    void testGetPermissionNoPermissionFound() {
        //arrange

        //assert
        assertThrows(NoPermissionFoundException.class, () -> {
            //act
            sut.getPermission("NONE");
        });
    }

    @Test
    void testGetPermissionFieldNotAPermission() {
        //arrange

        //assert
        assertThrows(NoPermissionFoundException.class, () -> {
            sut.getPermission("NOT_A_PERMISSION");
        });
    }

    @Test
    void testGetPermissionNoRegister() {
        //arrange
        doReturn(null).when(plugin).permissionRegister();

        //assert
        assertThrows(NoPermissionRegisterFound.class, () -> {
            //act
            sut.getPermission("TEST_BASE");
        });
    }
}