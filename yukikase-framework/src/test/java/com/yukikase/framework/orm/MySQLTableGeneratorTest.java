package com.yukikase.framework.orm;

import com.yukikase.framework.orm.mysql.MySQLTableGenerator;
import com.yukikase.framework.orm.testclasses.TestEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class MySQLTableGeneratorTest {

    private MySQLTableGenerator sut;
    private IDatabaseConnector connector;

    @BeforeEach
    void setUp() {
        connector = mock(IDatabaseConnector.class);
        sut = new MySQLTableGenerator(connector);
    }

    private static Stream<Arguments> testFromJavaType() {
        return Stream.of(
                Arguments.of(String.class, 255, "VARCHAR(255)"),
                Arguments.of(String.class, 1, "VARCHAR(1)"),
                Arguments.of(int.class, 255, "INT"),
                Arguments.of(Integer.class, 255, "INT"),
                Arguments.of(long.class, 255, "BIGINT"),
                Arguments.of(Long.class, 255, "BIGINT"),
                Arguments.of(double.class, 255, "DOUBLE"),
                Arguments.of(Double.class, 255, "DOUBLE"),
                Arguments.of(float.class, 255, "FLOAT"),
                Arguments.of(Float.class, 255, "FLOAT"),
                Arguments.of(boolean.class, 255, "BIT(1)"),
                Arguments.of(Boolean.class, 255, "BIT(1)"),
                Arguments.of(UUID.class, 255, "CHAR(36)"),
                Arguments.of(int.class, 1, "INT")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testFromJavaType(Class<?> type, int length, String expected) {
        var actual = sut.fromJavaType(type, length);

        assertEquals(expected, actual);
    }

    private static Stream<Arguments> testGenerateTable() {
        return Stream.of(
                Arguments.of(TestEntity.class, "CREATE TABLE IF NOT EXISTS TestEntity(\n" +
                        "\tinteger INT PRIMARY KEY,\n" +
                        "\tstring VARCHAR(255),\n" +
                        "\tlengthString VARCHAR(100),\n" +
                        "\ttest BIT(1),\n" +
                        "\tnonNullString VARCHAR(255) NOT NULL,\n" +
                        ")")
        );
    }

    @ParameterizedTest
    @MethodSource
    void testGenerateTable(Class<?> clazz, String expected) {
        var actual = sut.generateTable(clazz);

        assertEquals(expected, actual);
    }
}