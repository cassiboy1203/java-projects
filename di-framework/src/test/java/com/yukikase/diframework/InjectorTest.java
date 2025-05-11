package com.yukikase.diframework;

import com.yukikase.diframework.testclasses.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class InjectorTest {

    private Injector sut;

    @BeforeEach
    void setup() {
        sut = spy(new Injector());
    }

    @Test
    void testStart() {
        Map<Class<?>, Set<Tuple<String, Class<?>>>> expected = new HashMap<>();

        Set<Tuple<String, Class<?>>> class1Set = new HashSet<>();
        class1Set.add(new Tuple<>("com.yukikase.diframework.testclasses.Class1", Class1.class));

        Set<Tuple<String, Class<?>>> class2Set = new HashSet<>();
        class2Set.add(new Tuple<>("com.yukikase.diframework.testclasses.Class2", Class2.class));

        Set<Tuple<String, Class<?>>> class3Set = new HashSet<>();
        class3Set.add(new Tuple<>("com.yukikase.diframework.testclasses.Class3", Class3.class));

        Set<Tuple<String, Class<?>>> class4Set = new HashSet<>();
        class4Set.add(new Tuple<>("QualifierTest", Class4.class));

        Set<Tuple<String, Class<?>>> class6Set = new HashSet<>();
        class6Set.add(new Tuple<>("com.yukikase.diframework.testclasses.Class6", Class6.class));

        Set<Tuple<String, Class<?>>> class7Set = new HashSet<>();
        class7Set.add(new Tuple<>("com.yukikase.diframework.testclasses.Class7", Class7.class));

        Set<Tuple<String, Class<?>>> superClassSet = new HashSet<>();
        superClassSet.add(new Tuple<>("com.yukikase.diframework.testclasses.Class2", Class2.class));

        Set<Tuple<String, Class<?>>> interfaceClassSet = new HashSet<>();
        interfaceClassSet.add(new Tuple<>("com.yukikase.diframework.testclasses.Class3", Class3.class));

        Set<Tuple<String, Class<?>>> superClassWithMultiple = new HashSet<>();
        superClassWithMultiple.add(new Tuple<>("com.yukikase.diframework.testclasses.Class6", Class6.class));
        superClassWithMultiple.add(new Tuple<>("com.yukikase.diframework.testclasses.Class7", Class7.class));


        expected.put(Class1.class, class1Set);
        expected.put(Class2.class, class2Set);
        expected.put(Class3.class, class3Set);
        expected.put(Class4.class, class4Set);
        expected.put(Class6.class, class6Set);
        expected.put(Class7.class, class7Set);
        expected.put(SuperClass.class, superClassSet);
        expected.put(InterfaceClass.class, interfaceClassSet);
        expected.put(SuperClassWithMultiple.class, superClassWithMultiple);

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

        doReturn(classes).when(sut).getClasses(MainClass.class);

        sut.start(MainClass.class);

        assertEquals(expected, sut.diMap, "diMap is not equal");
    }
}