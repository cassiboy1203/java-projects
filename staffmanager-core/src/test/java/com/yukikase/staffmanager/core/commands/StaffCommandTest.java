package com.yukikase.staffmanager.core.commands;

import com.yukikase.staffmanager.core.staffmode.IStaffMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StaffCommandTest {
    private StaffCommand sut;
    private IStaffMode staffMode;

    @BeforeEach
    void setup() {
        staffMode = mock(IStaffMode.class);

        sut = new StaffCommand(staffMode);
    }

    @Test
    void testName() {
        //arrange
        var expected = "staff";

        //act
        var actual = sut.name();

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testOnCommand() {
        //arrange
        var player = mock(Player.class);
        var command = mock(Command.class);

        when(staffMode.toggleStaffMode(player)).thenReturn(true);

        //act
        var actual = sut.onCommand(player, command, null);

        //assert
        verify(staffMode).toggleStaffMode(player);

        assertTrue(actual);
    }

    @Test
    void testOnCommandWithNonPlayer() {
        //arrange
        var sender = mock(CommandSender.class);
        var command = mock(Command.class);

        //act
        var actual = sut.onCommand(sender, command, null);

        //assert
        verify(staffMode, never()).toggleStaffMode(any());

        assertFalse(actual);
    }
}