package regular;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import practice.*;
/**
 * @author Leonardo Kenji Feb 4, 2014
 */

public class AntRun {
	static String folder = "C:\\_eclipse\\Workfolder\\targetcode\\";
    public static void main(String[] args) throws Exception {
    	
        test4();
    }
    
    private static void test1() throws FileNotFoundException, IOException{
/*    	   ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\somewhat\\src\\regular\\data.test")); // we'll
          // parse
			ArrayInitLexer lexer = new ArrayInitLexer(input);
			CommonTokenStream tokens = new CommonTokenStream(lexer);
			ArrayInitParser parser = new ArrayInitParser(tokens);
			ParseTree tree = parser.init(); 
			
			System.out.println(tree.toStringTree(parser));*/
    }
    private static void test2() throws FileNotFoundException, IOException{
    	    ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\somewhat\\src\\practice\\t.expr")); // we'll
          // parse
    	    LabeledExprLexer lexer = new LabeledExprLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            LabeledExprParser parser = new LabeledExprParser(tokens);
            ParseTree tree = parser.prog(); // parse

            EvalVisitor eval = new EvalVisitor();
            eval.visit(tree);
    }
    
    private static void test3() throws Exception {
        String inputFile = null;
       
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\testtest\\src\\practice\\Demo.java"));

        JavaLexer lexer = new JavaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit(); // parse

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        ExtractInterfaceListener extractor = new ExtractInterfaceListener(parser);
        walker.walk(extractor, tree); // initiate walk of tree with listener
    }
    
    private static void test4() throws Exception {
        String inputFile = null;
        Set<String> funcs = new HashSet<String>();
        Map<String,Integer> allcalls = new HashMap<String,Integer>();
        
        String[] files = {"Demo.java","Demo2.java"};
        for(String file :files){
        	 ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(folder + file));
             JavaLexer lexer = new JavaLexer(input);
             CommonTokenStream tokens = new CommonTokenStream(lexer);
             JavaParser parser = new JavaParser(tokens);
             ParseTree tree = parser.compilationUnit(); // parse
             ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
             Addlog extractor = new Addlog(parser,false);
             walker.walk(extractor, tree); // initiate walk of tree with listener
             funcs.addAll(extractor.getFunctions());

        }
        // 2nd round
        for(String file :files){
       	    ANTLRInputStream input = new ANTLRInputStream(new FileInputStream(folder + file));
            JavaLexer lexer = new JavaLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            JavaParser parser = new JavaParser(tokens);
            ParseTree tree = parser.compilationUnit(); // parse
            ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
            Addlog extractor = new Addlog(parser,false);
            extractor.setPrint(true);
            extractor.setFunctions(funcs);
            walker.walk(extractor, tree);          
            allcalls.putAll(extractor.getCalls());
       }
        System.out.println(123);

    }
    private static void test5() throws Exception {       
       
        ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\testtest\\src\\practice\\Demo.java"));
        JavaLexer lexer = new JavaLexer(input);
		CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser parser = new JavaParser(tokens);
        ParseTree tree = parser.compilationUnit(); // parse

        ParseTreeWalker walker = new ParseTreeWalker(); // create standard walker
        InsertSerialIDListener extractor = new InsertSerialIDListener(tokens);
        walker.walk(extractor, tree); // initiate walk of tree with listener

        // print back ALTERED stream
        System.out.println(extractor.getRewriter().getText());
    }
    private static void test6() throws Exception{
    	  ANTLRInputStream input = new ANTLRInputStream(new FileInputStream("C:\\_eclipse\\Workfolder\\testtest\\src\\practice\\Demo.java"));
          JavaLexer lexer = new JavaLexer(input);
          CommonTokenStream tokens = new CommonTokenStream(lexer);
          JavaParser parser = new JavaParser(tokens);
          ParseTree tree = parser.compilationUnit();
          
          ParseTreeWalker walker = new ParseTreeWalker();
          InsertLog insertlog = new InsertLog(tokens);
          walker.walk(insertlog, tree);
          
          System.out.println(insertlog.getRewriter().getText());
    }



}
