package com.github.ruben_bottu.isp_class_schedule_backend.domain.external;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Map;

/**
 * Change annotations in runtime.
 *
 * @author XDean
 *
 */
public class AnnotationUtil {

    private static final Constructor<?> AnnotationInvocationHandler_constructor;
    private static final Constructor<?> AnnotationData_constructor;
    private static final Method Class_annotationData;
    private static final Field Class_classRedefinedCount;
    private static final Field AnnotationData_annotations;
    // CHANGE by Ruben Bottu: renamed from AnnotationData_declaredAnotations
    private static final Field AnnotationData_declaredAnnotations;
    private static final Method Atomic_casAnnotationData;
    private static final Class<?> Atomic_class;
    // CHANGE by Ruben Bottu: renamed from Field_Excutable_DeclaredAnnotations
    private static final Field Field_Executable_DeclaredAnnotations;

    static {
        // static initialization of necessary reflection Objects
        try {
            Class<?> AnnotationInvocationHandler_class = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
            // CHANGE by Ruben Bottu: removed redundant array creation for calling varargs method
            AnnotationInvocationHandler_constructor = AnnotationInvocationHandler_class.getDeclaredConstructor(Class.class, Map.class);
            AnnotationInvocationHandler_constructor.setAccessible(true);

            Atomic_class = Class.forName("java.lang.Class$Atomic");
            Class<?> AnnotationData_class = Class.forName("java.lang.Class$AnnotationData");

            // CHANGE by Ruben Bottu: removed redundant array creation for calling varargs method
            AnnotationData_constructor = AnnotationData_class.getDeclaredConstructor(Map.class, Map.class, int.class);
            AnnotationData_constructor.setAccessible(true);
            Class_annotationData = Class.class.getDeclaredMethod("annotationData");
            Class_annotationData.setAccessible(true);

            Class_classRedefinedCount = Class.class.getDeclaredField("classRedefinedCount");
            Class_classRedefinedCount.setAccessible(true);

            AnnotationData_annotations = AnnotationData_class.getDeclaredField("annotations");
            AnnotationData_annotations.setAccessible(true);
            AnnotationData_declaredAnnotations = AnnotationData_class.getDeclaredField("declaredAnnotations");
            AnnotationData_declaredAnnotations.setAccessible(true);

            Atomic_casAnnotationData = Atomic_class.getDeclaredMethod("casAnnotationData",
                    Class.class, AnnotationData_class, AnnotationData_class);
            Atomic_casAnnotationData.setAccessible(true);

            Field_Executable_DeclaredAnnotations = Executable.class.getDeclaredField("declaredAnnotations");
            Field_Executable_DeclaredAnnotations.setAccessible(true);

            // CHANGE by Ruben Bottu: removed Field_Field_DeclaredAnnotation due to issues with newer Java versions
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
            throw new IllegalStateException("AnnotationUtil init fail, check your java version.", e);
        }
    }

    /**
     * Changes the annotation value for the given key of the given annotation to newValue and returns
     * the previous value.
     *
     * @author Balder@stackoverflow
     * @see <a href="https://stackoverflow.com/a/28118436/7803527">Origin code on Stackoverflow</a>
     * @see <a href=
     *      "http://hg.openjdk.java.net/jdk8u/jdk8u/jdk/file/c46daef6edb5/src/share/classes/sun/reflect/annotation/AnnotationInvocationHandler.java">sun.reflect.annotation.AnnotationInvocationHandler</a>
     */
    @SuppressWarnings("unchecked")
    public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue) {
        Object handler = Proxy.getInvocationHandler(annotation);
        Field f;
        try {
            f = handler.getClass().getDeclaredField("memberValues");
        } catch (NoSuchFieldException | SecurityException e) {
            throw new IllegalStateException(e);
        }
        f.setAccessible(true);
        Map<String, Object> memberValues;
        try {
            memberValues = (Map<String, Object>) f.get(handler);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
        Object oldValue = memberValues.get(key);
        if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
            throw new IllegalArgumentException();
        }
        memberValues.put(key, newValue);
        return oldValue;
    }

    /*
        CHANGE by Ruben Bottu: all following code that was present in the original file has been removed due to issues with newer versions of Java, redundancy in the surrounding project and dependencies.
     */
}
