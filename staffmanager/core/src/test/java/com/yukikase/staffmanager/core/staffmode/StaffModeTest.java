package com.yukikase.staffmanager.core.staffmode;

import com.yukikase.lib.permission.IPermissionHandler;
import com.yukikase.staffmanager.core.PermissionRegister;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffModeTest {
    private StaffMode sut;
    private IPermissionHandler permissionHandler;
    private Player player;

    @BeforeEach
    void setup() {
        permissionHandler = mock(IPermissionHandler.class);
        player = mock(Player.class);

        sut = spy(new StaffMode(permissionHandler));
    }

    @Test
    void testToggleStaffModeEnter() {
        //arrange
        when(sut.isInStaffMode(player)).thenReturn(false);
        when(sut.enterStaffMode(player)).thenReturn(1);

        //act
        var actual = sut.toggleStaffMode(player);

        //assert
        assertEquals(1, actual);
        verify(sut).enterStaffMode(player);
    }

    @Test
    void testToggleStaffModeLeave() {
        //arrange
        when(sut.isInStaffMode(player)).thenReturn(true);
        when(sut.leaveStaffMode(player)).thenReturn(1);

        //act
        var actual = sut.toggleStaffMode(player);

        //assert
        assertEquals(1, actual);
        verify(sut).leaveStaffMode(player);
    }

    @Test
    void testEnterStaffMode() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GOD)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_FLY)).thenReturn(true);

        //act
        var actual = sut.enterStaffMode(player);

        //assert
        assertEquals(1, actual);
        verify(player).setInvulnerable(true);
        verify(player).setAllowFlight(true);
        verify(player).sendMessage(IStaffMode.ENTER_STAFF_MODE_MESSAGE);
    }

    @Test
    void testEnterStaffModeNoPermissions() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GOD)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_FLY)).thenReturn(false);

        //act
        var actual = sut.enterStaffMode(player);

        //assert
        assertEquals(1, actual);
        verify(player, never()).setInvulnerable(true);
        verify(player, never()).setAllowFlight(true);
        verify(player).sendMessage(IStaffMode.ENTER_STAFF_MODE_MESSAGE);
    }

    @Test
    void testEnterStaffModeGodPermissions() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GOD)).thenReturn(true);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_FLY)).thenReturn(false);

        //act
        var actual = sut.enterStaffMode(player);

        //assert
        assertEquals(1, actual);
        verify(player).setInvulnerable(true);
        verify(player, never()).setAllowFlight(true);
        verify(player).sendMessage(IStaffMode.ENTER_STAFF_MODE_MESSAGE);
    }

    @Test
    void testEnterStaffModeFlyPermissions() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_GOD)).thenReturn(false);
        when(permissionHandler.playerHasPermission(player, PermissionRegister.STAFF_MODE_FLY)).thenReturn(true);

        //act
        var actual = sut.enterStaffMode(player);

        //assert
        assertEquals(1, actual);
        assertTrue(sut.playersInStaffMode.contains(uuid), "Player not added to players in staff mode list");
        verify(player, never()).setInvulnerable(true);
        verify(player).setAllowFlight(true);
        verify(player).sendMessage(IStaffMode.ENTER_STAFF_MODE_MESSAGE);
    }

    @Test
    void testLeaveStaffMode() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        //act
        var actual = sut.leaveStaffMode(player);

        //assert
        assertEquals(1, actual);
        assertFalse(sut.playersInStaffMode.contains(uuid), "Player added to players in staff mode list");
        verify(player).setInvulnerable(false);
        verify(player).setAllowFlight(false);
        verify(player).sendMessage(IStaffMode.LEAVE_STAFF_MODE_MESSAGE);
    }

    @Test
    void testIsInStaffModeTrue() {
        //arrange
        var uuid = mock(UUID.class);

        sut.playersInStaffMode.add(uuid);

        when(player.getUniqueId()).thenReturn(uuid);

        //act
        var actual = sut.isInStaffMode(player);

        //assert
        assertTrue(actual);
    }

    @Test
    void testIsInStaffModeFalse() {
        //arrange
        var uuid = mock(UUID.class);

        when(player.getUniqueId()).thenReturn(uuid);

        //act
        var actual = sut.isInStaffMode(player);

        //assert
        assertFalse(actual);
    }
}