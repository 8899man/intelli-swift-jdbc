package com.fr.bi.stable.utils.program;import com.fr.bi.stable.utils.exception.NumberOverflowException;import com.fr.general.ComparatorUtils;import java.util.Collection;import java.util.HashSet;import java.util.Map;import java.util.Set;/** * Created by Hiram on 2015/9/17. */public class BITypeUtils {    /**     * @param la     * @return     */    public static int[] turnToIntArray(long[] la) {        int[] ia = new int[la.length];        for (int i = 0; i < ia.length; i++) {            ia[i] = (int) la[i];        }        return ia;    }    public static int[] turnToIntArrayWithCheck(long[] la) throws NumberOverflowException {        int[] ia = new int[la.length];        for (int i = 0; i < ia.length; i++) {            if (la[i] > Integer.MAX_VALUE || la[i] < Integer.MIN_VALUE) {                throw new NumberOverflowException();            }            ia[i] = Long.valueOf(la[i]).intValue();        }        return ia;    }    public static long[] turnToLongArray(int[] ia) {        long[] la = new long[ia.length];        for (int i = 0; i < la.length; i++) {            la[i] = ia[i];        }        return la;    }    public static Integer[] convert(int[] ia) {        if (ia != null) {            Integer[] result = new Integer[ia.length];            for (int i = 0; i < ia.length; i++) {                result[i] = Integer.valueOf(ia[i]);            }            return result;        } else {            return new Integer[0];        }    }    public static Long convert(Object value) {        if (value != null) {            if (isDouble(value)) {//                return (long)()            }        }        throw new UnsupportedOperationException();    }    public static boolean isDouble(Object value) {        return isAssignable(Double.class, value.getClass());    }    public static long[] convert(Long[] ia) {        if (ia != null) {            long[] result = new long[ia.length];            for (int i = 0; i < ia.length; i++) {                result[i] = ia[i].longValue();            }            return result;        } else {            return new long[0];        }    }    public static Long[] convert(long[] ia) {        if (ia != null) {            Long[] result = new Long[ia.length];            for (int i = 0; i < ia.length; i++) {                result[i] = Long.valueOf(ia[i]);            }            return result;        } else {            return new Long[0];        }    }    public static int[] convert(Integer[] ia) {        if (ia != null) {            int[] result = new int[ia.length];            for (int i = 0; i < ia.length; i++) {                result[i] = ia[i].intValue();            }            return result;        } else {            return new int[0];        }    }    public static Long adapter(int i) {        return Long.valueOf(i);    }    private static Set<String> isPrimitiveTypeSet = new HashSet<String>(9);    static {        isPrimitiveTypeSet.add(int.class.getName());        isPrimitiveTypeSet.add(boolean.class.getName());        isPrimitiveTypeSet.add(char.class.getName());        isPrimitiveTypeSet.add(long.class.getName());        isPrimitiveTypeSet.add(float.class.getName());        isPrimitiveTypeSet.add(double.class.getName());        isPrimitiveTypeSet.add(byte.class.getName());        isPrimitiveTypeSet.add(short.class.getName());    }    public static boolean isPrimitiveType(Class clazz) {        BINonValueUtils.checkNull(clazz);        if (isPrimitiveTypeSet.contains(clazz.getName())) {            return true;        }        return false;    }    public static <T> T stringConvert2BasicType(Class<T> clazz, String value) {        BINonValueUtils.checkNull(clazz, value);        if (ComparatorUtils.equals(value, "null")) {            return null;        }        if (ComparatorUtils.equals(clazz.getName(), Integer.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), int.class.getName())) {            return (T) Integer.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Boolean.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), boolean.class.getName())) {            return (T) Boolean.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Character.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), char.class.getName())) {            return (T) Character.valueOf(value.charAt(0));        } else if (ComparatorUtils.equals(clazz.getName(), Long.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), long.class.getName())) {            return (T) Long.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Float.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), float.class.getName())) {            return (T) Float.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Double.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), double.class.getName())) {            return (T) Double.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Byte.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), byte.class.getName())) {            return (T) Byte.valueOf(value);        } else if (ComparatorUtils.equals(clazz.getName(), Short.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), short.class.getName())) {            return (T) Short.valueOf(value);        }        return (T) value;    }//    public static String basicValueConvert2String(Object value) {//        BINonValueUtils.checkNull(value);//        if (ComparatorUtils.equals(value, "null")) {//            return null;//        }//        if (ComparatorUtils.equals(value.getClass().getName(), Integer.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), int.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Boolean.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), boolean.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Character.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), char.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Long.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), long.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Float.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), float.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Double.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), double.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Byte.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), byte.class.getName())) {//            return String.valueOf(value);//        } else if (ComparatorUtils.equals(value.getClass().getName(), Short.class.getName()) ||//                ComparatorUtils.equals(value.getClass().getName(), short.class.getName())) {//            return String.valueOf(value);//        }//        return value.toString();//    }    private static Set<String> isAutoBoxTypeSet = new HashSet<String>(9);    static {        isAutoBoxTypeSet.add(Integer.class.getName());        isAutoBoxTypeSet.add(Boolean.class.getName());        isAutoBoxTypeSet.add(Character.class.getName());        isAutoBoxTypeSet.add(Long.class.getName());        isAutoBoxTypeSet.add(Float.class.getName());        isAutoBoxTypeSet.add(Double.class.getName());        isAutoBoxTypeSet.add(Byte.class.getName());        isAutoBoxTypeSet.add(Short.class.getName());    }    public static boolean isAutoBoxType(Class clazz) {        BINonValueUtils.checkNull(clazz);        if (isAutoBoxTypeSet.contains(clazz.getName())) {            return true;        }        return false;    }    public static <T> T generateBasicValue(Class<T> clazz) {        BINonValueUtils.checkNull(clazz);        if (ComparatorUtils.equals(clazz.getName(), Integer.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), int.class.getName())) {            return (T) Integer.valueOf(0);        } else if (ComparatorUtils.equals(clazz.getName(), Boolean.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), boolean.class.getName())) {            return (T) Boolean.valueOf(false);        } else if (ComparatorUtils.equals(clazz.getName(), Character.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), char.class.getName())) {            return (T) Character.valueOf(' ');        } else if (ComparatorUtils.equals(clazz.getName(), Long.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), long.class.getName())) {            return (T) Long.valueOf(0);        } else if (ComparatorUtils.equals(clazz.getName(), Float.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), float.class.getName())) {            return (T) Float.valueOf(0);        } else if (ComparatorUtils.equals(clazz.getName(), Double.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), double.class.getName())) {            return (T) Double.valueOf(0);        } else if (ComparatorUtils.equals(clazz.getName(), Byte.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), byte.class.getName())) {            return (T) Byte.valueOf("0");        } else if (ComparatorUtils.equals(clazz.getName(), Short.class.getName()) ||                ComparatorUtils.equals(clazz.getName(), short.class.getName())) {            return (T) Short.valueOf("0");        } else if (ComparatorUtils.equals(clazz.getName(), String.class.getName())) {            return (T) "";        }        return (T) new Object();    }    /**     * 是否赋值类型     *     * @param clazz     * @return     */    public static Boolean isBasicValue(Class clazz) {        if (BITypeUtils.isAutoBoxType(clazz) || BITypeUtils.isPrimitiveType(clazz)) {            return true;        } else if (ComparatorUtils.equals(String.class, clazz)) {            return true;        } else {            return false;        }    }    /**     * target是否可以赋值给maySuper参数。     *     * @param maySuper 变量的类型     * @param target   实际类型     * @return     */    public static Boolean isAssignable(Class maySuper, Class target) {        return maySuper.isAssignableFrom(target);    }    public static Object[] byteArray2BoxArray(byte[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Byte.valueOf(array[i]);        }        return result;    }    public static Object[] shortArray2BoxArray(short[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Short.valueOf(array[i]);        }        return result;    }    public static Object[] intArray2BoxArray(int[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Integer.valueOf(array[i]);        }        return result;    }    public static Object[] longArray2BoxArray(long[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Long.valueOf(array[i]);        }        return result;    }    public static Object[] doubleArray2BoxArray(double[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Double.valueOf(array[i]);        }        return result;    }    public static Object[] floatArray2BoxArray(float[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Float.valueOf(array[i]);        }        return result;    }    public static Object[] charArray2BoxArray(char[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Character.valueOf(array[i]);        }        return result;    }    public static Object[] booleanArray2BoxArray(boolean[] array) {        Object[] result = new Object[array.length];        for (int i = 0; i < array.length; i++) {            result[i] = Boolean.valueOf(array[i]);        }        return result;    }    /**     * primitive的数组转成对应的装包对象的数组     *     * @param array     * @return 如果array是数组对象，那么返回对象数组     * 否则返回空。     */    public static Object[] primitiveArray2BoxArray(Object array) {        if (array.getClass().isArray()) {            if (ComparatorUtils.equals(array.getClass(), char[].class)) {                return charArray2BoxArray((char[]) array);            } else if (ComparatorUtils.equals(array.getClass(), byte[].class)) {                return byteArray2BoxArray((byte[]) array);            } else if (ComparatorUtils.equals(array.getClass(), short[].class)) {                return shortArray2BoxArray((short[]) array);            } else if (ComparatorUtils.equals(array.getClass(), int[].class)) {                return intArray2BoxArray((int[]) array);            } else if (ComparatorUtils.equals(array.getClass(), long[].class)) {                return longArray2BoxArray((long[]) array);            } else if (ComparatorUtils.equals(array.getClass(), boolean[].class)) {                return booleanArray2BoxArray((boolean[]) array);            } else if (ComparatorUtils.equals(array.getClass(), double[].class)) {                return doubleArray2BoxArray((double[]) array);            } else if (ComparatorUtils.equals(array.getClass(), float[].class)) {                return floatArray2BoxArray((float[]) array);            }            return (Object[]) array;        }        /**         * 传递的不是数组，强制转换有出差的可能         *         */        return new Object[]{};    }    public static void addArrayValue(Object array, int index, Object value) {        if (array.getClass().isArray()) {            if (ComparatorUtils.equals(array.getClass(), char[].class)) {                ((char[]) array)[index] = ((Character) value).charValue();            } else if (ComparatorUtils.equals(array.getClass(), byte[].class)) {                ((byte[]) array)[index] = ((Byte) value).byteValue();            } else if (ComparatorUtils.equals(array.getClass(), short[].class)) {                ((short[]) array)[index] = ((Short) value).shortValue();            } else if (ComparatorUtils.equals(array.getClass(), int[].class)) {                ((int[]) array)[index] = ((Integer) value).intValue();            } else if (ComparatorUtils.equals(array.getClass(), long[].class)) {                ((long[]) array)[index] = ((Long) value).longValue();            } else if (ComparatorUtils.equals(array.getClass(), boolean[].class)) {                ((boolean[]) array)[index] = ((Boolean) value).booleanValue();            } else if (ComparatorUtils.equals(array.getClass(), double[].class)) {                ((double[]) array)[index] = ((Double) value).doubleValue();            } else if (ComparatorUtils.equals(array.getClass(), float[].class)) {                ((float[]) array)[index] = ((Float) value).floatValue();            } else {                ((Object[]) array)[index] = value;            }        }    }    public static Boolean isIterableObject(Object value) {        return value instanceof Iterable;    }    public static Boolean isMapObject(Object value) {        return value instanceof Map;    }    public static Boolean isArrayObject(Object value) {        return value.getClass().isArray();    }    public static Boolean isBasicType(Class fieldClass) {        return BITypeUtils.isBasicValue(fieldClass);    }    public static Boolean isIterableType(Class fieldClass) {        return BITypeUtils.isAssignable(Iterable.class, fieldClass);    }    public static Boolean isCollectionType(Class fieldClass) {        return BITypeUtils.isAssignable(Collection.class, fieldClass);    }    public static Boolean isMapType(Class fieldClass) {        return BITypeUtils.isAssignable(Map.class, fieldClass);    }    public static Boolean isArrayType(Class fieldClass) {        return fieldClass.isArray();    }}