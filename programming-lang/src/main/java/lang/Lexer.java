package lang;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class Lexer {
    private String code;
    private int pos = 0;

    public Lexer(String code){
        this.code = code;
    }

    public ArrayList<Token> tokenize() {
        var tokens = new ArrayList<Token>();

        while(hasNextToken()) {
            var token = this.getNextToken();
            if (token.type != TokenType.SPACE && token.type != TokenType.LINE_BREAK) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    private boolean hasNextToken(){
        if (this.pos >= this.code.length()) {
            return false;
        }

        return true;
    }

    private Token getNextToken() {
        var tokenTypes = TokenType.values();
        for (var tokenType: tokenTypes) {
            var pattern = Pattern.compile("^" + tokenType.regex);
            var curCode = this.code.substring(this.pos);
            var matcher = pattern.matcher(curCode);
            if (matcher.find()) {
                var res = curCode.substring(matcher.start(), matcher.end());
                this.pos += res.length();

                return new Token(tokenType, res);
            }
        }

        throw new RuntimeException("Unknown token");
    }
}
