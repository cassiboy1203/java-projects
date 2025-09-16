package com.yukikase.framework;

public class TypeUtil {
    private TypeUtil() {
    }

    public static Object convertValue(String value, Class<?> type) {
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class) || type.equals(int.class)) return Integer.parseInt(value);
        if (type.equals(Long.class) || type.equals(long.class)) return Long.parseLong(value);
        if (type.equals(Double.class) || type.equals(double.class)) return Double.parseDouble(value);
        if (type.equals(Float.class) || type.equals(float.class)) return Float.parseFloat(value);
        if (type.equals(Boolean.class) || type.equals(boolean.class)) return Boolean.parseBoolean(value);
        if (Enum.class.isAssignableFrom(type)) return Enum.valueOf((Class<Enum>) type, value);

        throw new IllegalArgumentException("Unsupported type: " + type.getName());
    }
}
