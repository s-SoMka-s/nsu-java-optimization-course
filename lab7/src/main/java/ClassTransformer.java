import javassist.*;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import jdk.internal.org.objectweb.asm.Opcodes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class ClassTransformer implements ClassFileTransformer {

    @Override
    public byte[] transform(ClassLoader loader,
                            String className,
                            Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain,
                            byte[] classfileBuffer) {
        ClassPool classPool = ClassPool.getDefault();
        try {
            // compile time class
            CtClass currentClass = classPool.makeClass(new ByteArrayInputStream(classfileBuffer));

            // 3. Record all allocations
            recordAllAllocations(className, currentClass);

            // 4. Measure a time needed to complete each method in all classes defined in package nsu.fit.javaperf
            measureAllMethodsCompletionTime(currentClass);

            return currentClass.toBytecode();
        } catch (IOException | CannotCompileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void recordAllAllocations(String className, CtClass currentClass) {
        // get all class constructors
        CtConstructor[] constructors = currentClass.getConstructors();
        Arrays.stream(constructors).forEach(constructor -> func(className, constructor));
    }

    private static void measureAllMethodsCompletionTime(CtClass currentClass) throws CannotCompileException {
        boolean isRequiredPackage = currentClass.getPackageName().startsWith("nsu.fit.javaperf");
        if (!isRequiredPackage) {
            return;
        }

        // add field for time control
        CtField startTimeField = new CtField(CtClass.longType, "startTime", currentClass);
        startTimeField.setModifiers(Modifier.PRIVATE | Modifier.STATIC);
        currentClass.addField(startTimeField);

        // get all class methods
        CtMethod[] methods = currentClass.getDeclaredMethods();

        Arrays.stream(methods).forEach(method -> addMethodCompletionTimeMeasurer(method));
    }

    private static void addMethodCompletionTimeMeasurer(CtMethod method) {
        try {
            method.insertBefore("startTime = System.currentTimeMillis();\n");

            StringBuilder sb = new StringBuilder();
            sb.append("long finish = System.currentTimeMillis();");
            sb.append("long timeElapsed = finish - startTime;");
            sb.append("System.out.println(\"ClassTransformer: elapsed Time: \" + timeElapsed + \"ms\");");

            method.insertAfter(sb.toString());
        } catch (CannotCompileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private static void func(String className, CtConstructor constructor) {
        try {
            CodeIterator iterator = constructor.getMethodInfo().getCodeAttribute().iterator();
            while (iterator.hasNext()) {
                int opcode = iterator.next();
                if (opcode == Opcodes.NEW) {
                    constructor.insertAfter("System.out.println(\"ClassTransformer: Allocating " + className + "\");");
                    return;
                }
            }
        } catch (CannotCompileException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (BadBytecode e) {
            e.printStackTrace();
        }
    }
}
