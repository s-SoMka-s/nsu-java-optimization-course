package lab3;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Lab3 {
    public static void main(String[] args) throws IOException {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS |
                ClassWriter.COMPUTE_FRAMES);

        cw.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "HelloWorld", null,
                "java/lang/Object", null);

        MethodVisitor constructor =
                cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        constructor.visitCode();

        constructor.visitVarInsn(Opcodes.ALOAD, 0);

        constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V");

        constructor.visitInsn(Opcodes.RETURN);

        constructor.visitMaxs(0, 0);

        constructor.visitEnd();

        MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC,
                "main", "([Ljava/lang/String;)V", null, null);

        mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System",
                "out", "Ljava/io/PrintStream;");

        mv.visitLdcInsn("Hello, World!");

        mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");

        mv.visitInsn(Opcodes.RETURN);

        mv.visitMaxs(0, 0);

        mv.visitEnd();


        File file = new File("src/main/java/lab3/HelloWorld.class");
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(cw.toByteArray());
        }
    }
}
