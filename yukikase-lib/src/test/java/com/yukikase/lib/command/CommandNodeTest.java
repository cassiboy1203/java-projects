package com.yukikase.lib.command;

import com.yukikase.lib.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandNodeTest {
    private CommandNode sut;

    private void setupSutWithSubCommandsAliasAliasWithSubCommand() {
        sut = new CommandNode("", "", null, null, true);

        var aliasRoot = new CommandNode("alias", "", null, null);
        var aliasSub = new CommandNode("alias", "sub", null, null);

        aliasRoot.addChild(aliasSub);

        var defaultCommand = new CommandNode("test", "", null, null);
        var sub1 = new CommandNode("test", "sub1", null, null);
        var sub2 = new CommandNode("test", "sub2", null, null);

        defaultCommand.addChild(sub1);
        defaultCommand.addChild(sub2);

        sut.addChild(aliasRoot);
        sut.addChild(defaultCommand);
    }

    @Test
    void testGetNodeToExecuteGetAlias() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        var expected = new CommandNode("alias", "", null, null);

        //act
        var actual = sut.getNodeToExecute("alias", new String[]{});

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetNodeToExecuteGetSubCommand() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        var expected = new CommandNode("test", "sub1", null, null);

        //act
        var actual = sut.getNodeToExecute("test", new String[]{"sub1"});

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetNodeToExecuteGetDefaultCommand() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        var expected = new CommandNode("test", "", null, null);

        //act
        var actual = sut.getNodeToExecute("test", new String[]{});

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetNodeToExecuteGetAliasSubCommand() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        var expected = new CommandNode("alias", "sub", null, null);

        //act
        var actual = sut.getNodeToExecute("alias", new String[]{"sub"});

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetNodeToExecuteGetAliasWhenNoSubCommandFound() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        var expected = new CommandNode("alias", "", null, null);

        //act
        var actual = sut.getNodeToExecute("alias", new String[]{"none"});

        //assert
        assertEquals(expected, actual);
    }

    @Test
    void testGetNodeToExecuteThrowsInvalidCommandExceptionWhenNoCommandOfAliasFound() {
        //arrange
        setupSutWithSubCommandsAliasAliasWithSubCommand();

        //assert
        var exception = assertThrows(InvalidCommandException.class, () -> {
            //act
            var actual = sut.getNodeToExecute("none", new String[]{});
        });

        assertEquals(String.format(InvalidCommandException.COMMAND_NOT_FOUND, "none"), exception.getMessage());
    }
}