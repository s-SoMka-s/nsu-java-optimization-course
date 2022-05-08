package lang;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        var file = "src/print.txt";
        var code = Files.readString(Path.of(file));
        var lexer = new Lexer(code);
        var tokens = lexer.tokenize();

        var parser = new Parser(tokens);
        var root = parser.parse();

        var runner = new Runner();
        runner.run(root);
        System.out.println(code);
    }
}
