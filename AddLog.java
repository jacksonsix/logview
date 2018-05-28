package practice;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import practice.JavaParser.ClassOrInterfaceModifierContext;

public class Addlog extends JavaBaseListener {
	JavaParser parser;
	Set<String> functions;
	Map<String,Integer> calls;
	String currentMethodDecl;
	String curPackage;
	String curClass;
	boolean print = false;
	
    public Addlog(JavaParser parser,boolean print) {this.parser = parser;
      this.functions = new HashSet<String>();
      this.calls = new HashMap<String,Integer>();
      this.print = print;
    }
	
    public Map<String, Integer> getCalls() {
		return calls;
	}

	public void setCalls(Map<String, Integer> calls) {
		this.calls = calls;
	}

	public String getCurClass() {
		return curClass;
	}

	public void setCurClass(String curClass) {
		this.curClass = curClass;
	}

	public Set<String> getFunctions() {
		return functions;
	}
    
    private void  printline(String msg){
    	if(this.print){
    		System.out.println(msg);
    	}
    }

	public void setFunctions(Set<String> functions) {
		this.functions = functions;
	}

	public boolean isPrint() {
		return print;
	}

	public void setPrint(boolean print) {
		this.print = print;
	}
	private void writeCall(String caller, String callee){
		if(!this.print) return;
		if(!this.functions.contains(caller)) return;
		
		String line = caller +"->"+ callee;
		if(this.calls.containsKey(line)){
			this.calls.put(line, this.calls.get(line) +1);
		}else{
			this.calls.put(line, 1);
		}
		
	}

	@Override public void enterTypeDeclaration(JavaParser.TypeDeclarationContext ctx) {
    	List<ClassOrInterfaceModifierContext> paras = ctx.classOrInterfaceModifier();
    	StringBuilder st = new StringBuilder();
    	for(ClassOrInterfaceModifierContext p : paras){
    		st.append(p.getText());
    	}
    	printline(st+" ");
    }
    
    @Override
    public void enterClassDeclaration(JavaParser.ClassDeclarationContext ctx){
    	this.curClass = ctx.Identifier().getText();
        printline("class "+ctx.Identifier()+" {");
        printline("  "+"private static final Logger log = LoggerFactory.getLogger(" +ctx.Identifier() + ".class); ");
    }
    @Override
    public void exitClassDeclaration(JavaParser.ClassDeclarationContext ctx) {
        printline("}");
    }

    /** Listen to matches of methodDeclaration */
    @Override
    public void enterMethodDeclaration(
        JavaParser.MethodDeclarationContext ctx
    )
    {
        // need parser to get tokens    	
        TokenStream tokens = parser.getTokenStream();
        printline("  " +tokens.getText(ctx));
        this.functions.add(fullMethodName(ctx.myMethodName().Identifier().getText()));
        this.currentMethodDecl = ctx.myMethodName().Identifier().getText();
/*        String type = "void";
        if ( ctx.type()!=null ) {
            type = tokens.getText(ctx.type());
        }
        String args = tokens.getText(ctx.formalParameters());
        
        printline("\t"+type+" "+ctx.myMethodName().Identifier()+args+";");*/
        
    }
    
    @Override public void enterPackageDeclaration(JavaParser.PackageDeclarationContext ctx) { 
    	String pack = ctx.qualifiedName().getText();
    	this.curPackage=pack;
    	 printline("package " +pack +";");
     	 printline("import " +"org.slf4j.Logger;");
         printline("import " +"org.slf4j.LoggerFactory;");

    }
    @Override public void enterImportDeclaration(JavaParser.ImportDeclarationContext ctx) { 
    	//String imstatement = ctx.qualifiedName().getText();
    	//printline("import " +imstatement +";");
       
        printline(parser.getTokenStream().getText(ctx));
    }
    @Override public void exitCompilationUnit(JavaParser.CompilationUnitContext ctx) { 

    	boolean debug = true;
    	if(debug){
        	Iterator<String> it = this.functions.iterator();
        	while(it.hasNext()){
        		printline(it.next());
        	}
        	
        	Iterator it2 = this.calls.entrySet().iterator();
            while (it2.hasNext()) {
                Map.Entry pair = (Map.Entry)it2.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                //it2.remove(); // avoids a ConcurrentModificationException
            }
    	}
    }
	@Override public void enterEveryRule(ParserRuleContext ctx) {
		//printline("enter rule");
	}
	private String fullMethodName(String method){
		return this.curPackage+"."+this.curClass+"."+method;
	}
	@Override public void enterExpression(JavaParser.ExpressionContext ctx) { 		
		int s = ctx.children.size();		
		if(s ==3 
			&&ctx.getChild(0).getClass().getCanonicalName().indexOf("ExpressionContext") > -1 
			&& ctx.getChild(1).getText().equals("(")
			&& ctx.getChild(2).getText().equals(")")){
			if(this.functions.contains(fullMethodName(ctx.getChild(0).getText()))){
				  //printline("invocation " + ctx.getChild(0).getText() +" in " + this.currentMethodDecl);
					writeCall(fullMethodName(this.currentMethodDecl),fullMethodName(ctx.getChild(0).getText()));
			}

		}else if(s ==4 
				&&ctx.getChild(0).getClass().getCanonicalName().indexOf("ExpressionContext") > -1 
				&& ctx.getChild(1).getText().equals("(")
				&& ctx.getChild(2).getClass().getCanonicalName().indexOf("ExpressionListContext") > -1
				&& ctx.getChild(3).getText().equals(")")){
			if(this.functions.contains(fullMethodName(ctx.getChild(0).getText()))){
				   //printline("invocation " + ctx.getChild(0).getText() +" in " + this.currentMethodDecl );
				   writeCall(fullMethodName(this.currentMethodDecl),fullMethodName(ctx.getChild(0).getText()));
			}

		}
		
	}
}
