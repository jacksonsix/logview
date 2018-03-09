package practice;
import org.antlr.v4.runtime.*;

import java.io.IOException;

import org.antlr.runtime.tree.*;

public class AntLog {
	@SuppressWarnings("deprecation")
    public static void main(String[] args) {
        String[] arg0 = { "-visitor", "C:\\Users\\Code\\antlr4\\Java.g4", "-package", "practice" };
        org.antlr.v4.Tool.main(arg0);
    }
		
	
}


