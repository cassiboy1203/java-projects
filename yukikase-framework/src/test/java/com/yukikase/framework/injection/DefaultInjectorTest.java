package com.yukikase.framework.injection;

import com.yukikase.framework.Tuple;
import com.yukikase.framework.exceptions.BeanNotFoundException;
import com.yukikase.framework.exceptions.ClassNotABeanException;
import com.yukikase.framework.injection.testclasses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class DefaultInjectorTest {

    private DefaultInjector sut;

    private static Stream<Arguments> provideParameters() {
        return Stream.of(
                Arguments.of(Class1.class, Class1.class),
                Arguments.of(SuperClass.class, Class2.class),
                Arguments.of(InterfaceClass.class, Class3.class),
                Arguments.of(Class4.class, Class4.class)
        );
    }

    private static Stream<Arguments> provideParametersWithName() {
        return Stream.of(
                Arguments.of(Class1.class, Class1.class, null),
                Arguments.of(SuperClass.class, Class2.class, "Class2"),
                Arguments.of(InterfaceClass.class, Class3.class, "Class3"),
                Arguments.of(Class4.class, Class4.class, "Class4"),
                Arguments.of(SuperClassWithMultiple.class, Class6.class, "Class6"),
                Arguments.of(SuperClassWithMultiple.class, Class7.class, "Class7")
        );
    }

    private static Stream<Arguments> provideParametersWithException() {
        return Stream.of(
                Arguments.of(DefaultInjectorTest.class, null, String.format(BeanNotFoundException.EC_NO_BEANS_FOUND_OF_TYPE, DefaultInjectorTest.class.getSimpleName())),
                Arguments.of(SuperClassWithMultiple.class, null, null),
                Arguments.of(SuperClassWithMultiple.class, "Class1", String.format(BeanNotFoundException.EC_NO_BEANS_FOUND_WITH_NAME, SuperClassWithMultiple.class.getSimpleName(), "Class1"))
        );
    }

    private static Stream<Arguments> provideParametersWithSingletons() {
        return Stream.of(
                Arguments.of(Class1.class, new Class1()),
                Arguments.of(SingletonClass.class, new SingletonClass())
        );
    }

    @BeforeEach
    void setup() {
        sut = new DefaultInjector(setupDiMap());
    }

    @Test
    void testStart() {
        sut = spy(new DefaultInjector());

        Map<Class<?>, Set<Tuple<String, Class<?>>>> expected = setupDiMap();

        List<Class<?>> classes = new ArrayList<>();
        classes.add(Class1.class);
        classes.add(Class2.class);
        classes.add(Class3.class);
        classes.add(Class4.class);
        classes.add(Class5.class);
        classes.add(Class6.class);
        classes.add(Class7.class);
        classes.add(InterfaceClass.class);
        classes.add(SuperClass.class);
        classes.add(SuperClassWithMultiple.class);
        classes.add(MainClass.class);
        classes.add(SingletonClass.class);
        classes.add(ClassWithNonDefaultConstructor.class);
        classes.add(ClassWithNonDefaultConstructorWithoutInject.class);
        classes.add(ClassWithNonDefaultConstructorWithMultipleConstructors.class);
        classes.add(ClassWithMethods.class);
        classes.add(ClassWithMethodsWithoutInject.class);

        doReturn(classes).when(sut).getClasses(MainClass.class);

        sut.start(MainClass.class);

        assertEquals(expected, sut.diMap, "diMap is not equal");
    }

    @Test
    void testStartWithRegister() {
        //arrange
        sut = spy(new DefaultInjector());

        var expected = new ArrayList<Class<?>>();
        expected.add(ConfigurationClass.class);

        List<Class<?>> classes = new ArrayList<>();
        classes.add(Class1.class);
        classes.add(Class2.class);
        classes.add(Class3.class);
        classes.add(Class4.class);
        classes.add(Class5.class);
        classes.add(Class6.class);
        classes.add(Class7.class);
        classes.add(InterfaceClass.class);
        classes.add(SuperClass.class);
        classes.add(SuperClassWithMultiple.class);
        classes.add(MainClass.class);
        classes.add(SingletonClass.class);
        classes.add(ClassWithNonDefaultConstructor.class);
        classes.add(ClassWithNonDefaultConstructorWithoutInject.class);
        classes.add(ClassWithNonDefaultConstructorWithMultipleConstructors.class);
        classes.add(ClassWithMethods.class);
        classes.add(ClassWithMethodsWithoutInject.class);
        classes.add(ConfigurationClass.class);

        doReturn(classes).when(sut).getClasses(MainClass.class);

        //act
        sut.start(MainClass.class);

        //assert
        assertEquals(setupDiMap(), sut.diMap);
        assertEquals(expected, sut.configurations);
    }

    @Test
    void testRegisterSingleton() {
        //arrange
        sut = new DefaultInjector();

        var instance = new Class1();
        var expectedSingletonList = new HashMap<Class<?>, Object>();
        expectedSingletonList.put(Class1.class, instance);

        var expectedDiMap = new HashMap<Class<?>, Set<Tuple<String, Class<?>>>>();
        var tuple = new Tuple<String, Class<?>>(Class1.class.getSimpleName(), Class1.class);
        var tupleSet = new HashSet<Tuple<String, Class<?>>>();
        tupleSet.add(tuple);
        expectedDiMap.put(Class1.class, tupleSet);

        //act
        sut.registerSingleton(Class1.class, instance);

        //assert
        assertEquals(expectedSingletonList, sut.singletons, "Singleton list is not equal");
        assertEquals(expectedDiMap, sut.diMap, "DiMap is not equal");
    }

    @ParameterizedTest
    @MethodSource("provideParameters")
    void testGetInstanceWithoutName(Class<?> input, Class<?> expectedType) {
        //arrange

        //act
        var actual = sut.getInstance(input);

        //assert
        assertInstanceOf(expectedType, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParametersWithName")
    void testGetInstanceWithName(Class<?> input, Class<?> expectedType, String name) {
        //arrange

        //act
        var actual = sut.getInstance(input, name);

        //assert
        assertInstanceOf(expectedType, actual);
    }

    @ParameterizedTest
    @MethodSource("provideParametersWithException")
    void testGetInstanceWhenNoBeanOfTypeRegistered(Class<?> clazz, String name, String exceptionMessage) {
        //arrange

        //assert
        var exception = assertThrows(BeanNotFoundException.class, () -> {
            //act
            sut.getInstance(clazz, name);
        });

        if (exceptionMessage != null)
            assertEquals(exceptionMessage, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideParametersWithSingletons")
    void testGetInstanceWithSingletons(Class<?> clazz, Object expected) {
        //arrange
        var diMap = setupDiMap();
        var singletons = new HashMap<Class<?>, Object>();
        singletons.put(clazz, expected);
        sut = new DefaultInjector(diMap, singletons);

        //act
        var actual = sut.getInstance(clazz);

        //arrange
        assertEquals(expected, actual);
    }

    @Test
    void testGetInstanceWithNotYetCreatedSingletonsStayTheSame() {
        //arrange

        //act
        var actual = sut.getInstance(SingletonClass.class);
        var actual2 = sut.getInstance(SingletonClass.class);

        //arrange
        assertEquals(actual, actual2);
    }

    @Test
    void testGetInstances() {
        //arrange

        //act
        var actual = sut.getInstances(SuperClassWithMultiple.class);

        //assert
        assertEquals(2, actual.length, "Array length is not equal");
        Class<?> nextClassType = null;
        if (actual[0] instanceof Class7)
            nextClassType = Class6.class;
        else if (actual[0] instanceof Class6)
            nextClassType = Class7.class;
        else
            fail("First element in array is not of type Class7 or Class6");

        assertInstanceOf(nextClassType, actual[1]);
    }

    @Test
    void testGetInstanceOfClassWithNonDefaultConstructor() {
        //arrange

        //act
        var actual = sut.getInstance(ClassWithNonDefaultConstructor.class);

        //assert
        assertNotNull(actual.class1);
    }

    @Test
    void testGetInstanceOfClassWithMultipleNonDefaultConstructor() {
        //arrange

        //act
        var actual = sut.getInstance(ClassWithNonDefaultConstructorWithMultipleConstructors.class);

        //assert
        assertNull(actual.class1);
        assertNotNull(actual.class2);
    }

    @Test
    void testGetInstanceOfClassWithNonDefaultConstructorWithoutInject() {
        //arrange

        //assert
        assertThrows(ClassNotABeanException.class, () -> {
            //act
            var actual = sut.getInstance(ClassWithNonDefaultConstructorWithoutInject.class);
        });
    }

    @Test
    void testGetInstanceWithClassWithMethods() {
        //arrange

        //act
        var actual = sut.getInstance(ClassWithMethods.class);

        //assert
        assertNotNull(actual.class1);
        assertNotNull(actual.class2);
        assertNull(actual.class3);
        assertEquals(2, actual.superClassWithMultiples.size(), "Size of list is not equal");
        assertEquals(2, actual.superClassWithMultiplesArray.length, "Size of array is not equal");
    }

    @Test
    void testGetInstanceWithClassWithMethodsWithoutInject() {
        //arrange

        //act
        var actual = sut.getInstance(ClassWithMethodsWithoutInject.class);

        //assert
        assertNull(actual.class1);
        assertNull(actual.class2);
        assertNull(actual.class3);
    }

    @Test
    void testRunConfigurations() {
        //arrange
        var configurations = new ArrayList<Class<?>>();
        configurations.add(ConfigurationClass.class);

        sut = new DefaultInjector(setupDiMap(), configurations);

        //act
        sut.runConfigurations();

        //assert
        assertTrue(ConfigurationClass.register);
        assertFalse(ConfigurationClass.notRegister);
    }

    private Map<Class<?>, Set<Tuple<String, Class<?>>>> setupDiMap() {
        Map<Class<?>, Set<Tuple<String, Class<?>>>> diMap = new HashMap<>();

        Set<Tuple<String, Class<?>>> class1Set = new HashSet<>();
        class1Set.add(new Tuple<>("Class1", Class1.class));

        Set<Tuple<String, Class<?>>> class2Set = new HashSet<>();
        class2Set.add(new Tuple<>("Class2", Class2.class));

        Set<Tuple<String, Class<?>>> class3Set = new HashSet<>();
        class3Set.add(new Tuple<>("Class3", Class3.class));

        Set<Tuple<String, Class<?>>> class4Set = new HashSet<>();
        class4Set.add(new Tuple<>("QualifierTest", Class4.class));

        Set<Tuple<String, Class<?>>> class6Set = new HashSet<>();
        class6Set.add(new Tuple<>("Class6", Class6.class));

        Set<Tuple<String, Class<?>>> class7Set = new HashSet<>();
        class7Set.add(new Tuple<>("Class7", Class7.class));

        Set<Tuple<String, Class<?>>> superClassSet = new HashSet<>();
        superClassSet.add(new Tuple<>("Class2", Class2.class));

        Set<Tuple<String, Class<?>>> interfaceClassSet = new HashSet<>();
        interfaceClassSet.add(new Tuple<>("Class3", Class3.class));

        Set<Tuple<String, Class<?>>> superClassWithMultiple = new HashSet<>();
        superClassWithMultiple.add(new Tuple<>("Class6", Class6.class));
        superClassWithMultiple.add(new Tuple<>("Class7", Class7.class));

        Set<Tuple<String, Class<?>>> singletonClassSet = new HashSet<>();
        singletonClassSet.add(new Tuple<>("SingletonClass", SingletonClass.class));

        Set<Tuple<String, Class<?>>> classWithNonDefaultConstructor = new HashSet<>();
        classWithNonDefaultConstructor.add(new Tuple<>("ClassWithNonDefaultConstructor", ClassWithNonDefaultConstructor.class));

        Set<Tuple<String, Class<?>>> classWithNonDefaultConstructorWithoutInject = new HashSet<>();
        classWithNonDefaultConstructorWithoutInject.add(new Tuple<>("ClassWithNonDefaultConstructorWithoutInject", ClassWithNonDefaultConstructorWithoutInject.class));

        Set<Tuple<String, Class<?>>> classWithNonDefaultConstructorWithMultipleConstructors = new HashSet<>();
        classWithNonDefaultConstructorWithMultipleConstructors.add(new Tuple<>("ClassWithNonDefaultConstructorWithMultipleConstructors", ClassWithNonDefaultConstructorWithMultipleConstructors.class));

        Set<Tuple<String, Class<?>>> classWithMethodsSet = new HashSet<>();
        classWithMethodsSet.add(new Tuple<>("ClassWithMethods", ClassWithMethods.class));

        Set<Tuple<String, Class<?>>> classWithMethodsWithoutInjectSet = new HashSet<>();
        classWithMethodsWithoutInjectSet.add(new Tuple<>("ClassWithMethodsWithoutInject", ClassWithMethodsWithoutInject.class));

        diMap.put(Class1.class, class1Set);
        diMap.put(Class2.class, class2Set);
        diMap.put(Class3.class, class3Set);
        diMap.put(Class4.class, class4Set);
        diMap.put(Class6.class, class6Set);
        diMap.put(Class7.class, class7Set);
        diMap.put(SuperClass.class, superClassSet);
        diMap.put(InterfaceClass.class, interfaceClassSet);
        diMap.put(SuperClassWithMultiple.class, superClassWithMultiple);
        diMap.put(SingletonClass.class, singletonClassSet);
        diMap.put(ClassWithNonDefaultConstructor.class, classWithNonDefaultConstructor);
        diMap.put(ClassWithNonDefaultConstructorWithoutInject.class, classWithNonDefaultConstructorWithoutInject);
        diMap.put(ClassWithNonDefaultConstructorWithMultipleConstructors.class, classWithNonDefaultConstructorWithMultipleConstructors);
        diMap.put(ClassWithMethods.class, classWithMethodsSet);
        diMap.put(ClassWithMethodsWithoutInject.class, classWithMethodsWithoutInjectSet);

        return diMap;
    }
}