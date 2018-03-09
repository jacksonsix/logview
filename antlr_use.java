package regular;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * @author Leonardo Kenji Feb 4, 2014
 */

public class AntRun {

    
    public static void main(String[] args) throws IOException {
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\somewhat\\src\\regular\\data.test")); // we'll
                                                                                                                                                    // parse
        ArrayInitLexer lexer = new ArrayInitLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        ArrayInitParser parser = new ArrayInitParser(tokens);
        ParseTree tree = parser.init(); 

        System.out.println(tree.toStringTree(parser));

        LogAnalyze.readLog();
    }
    



}
