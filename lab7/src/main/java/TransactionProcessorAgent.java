import java.lang.instrument.Instrumentation;

public class TransactionProcessorAgent {
    public static void premain(String agentArgument, Instrumentation instrumentation) {
        // 1. Total number of classes loaded by JVM during the application lifetime
        Thread printingHook = new Thread(() -> System.out.println("Total loaded classes: " + instrumentation.getAllLoadedClasses().length));

        Runtime.getRuntime().addShutdownHook(printingHook);

        instrumentation.addTransformer(new ClassTransformer());
    }
}
