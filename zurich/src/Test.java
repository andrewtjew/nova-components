

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import com.geneva.debug.Output;
import com.geneva.debug.UnitPrinter;
import com.geneva.generation.Builder;
import com.geneva.generation.UnitBuilder;
import com.geneva.lexing3.LexerResult;
import com.geneva.parsing.Node;
import com.geneva.parsing.Parser;
import com.geneva.parsing.Unit;
import com.geneva.util.Source;


public class Test
{
	public static void testLexer() throws Exception
	{
		String text="a=>b";
		System.out.println(text);
		Source source=new Source(text,null);
		LexerResult result=com.geneva.lexing3.GenevaLexer.lex(source,1000000);
		Output.print(source, result);
	}
	
	public static void testExpression() throws Exception
	{
//		String text="1*(2+3)-(4*(5-6)/7) ;";
//		String text="5/1.a.b*(2+3)-4;";
//		String text="1+h().g.f(2+3)+(5/4);";
//		String text="1+h().g.f(2+3)+(5/4);";
//		String text="f(1+g(a(),b),h(2));";
//		String text="f[a+g((5))];";
//		String text="6/-5*!a+4;";
//		String text="6*-(-(4));";
//		String text="6*-!+-4/a;";
//		String text="a!=4==!g(5)&hello;";
//		String text="a>1?b+4:c/2;";
//		String text="a.b.c";
//		String text="f(1+g(a(),b),h(),a);";
//		String text="new 5;";
//		String text="(5+(string)t)-(foo)g()*(int)(a/8)";
//		String text="((5+6)/((a-b)))";
//		String text="foo( a=>b{5,4})";
		String text="a,b,c";
		System.out.println(text);
		Source source=new Source(text,null);

		Parser parser=Parser.newParser(source);
		Node node=parser.debugParseExpression();
		Output.printNodes(node);
		
		
		/*
		Lexer lexer=new Lexer(source);
		StatementParser parser=new StatementParser(lexer);
		ExpressionNode node=parser.parseExpression();
		if (node==null)
		{
			System.out.println("(null)");
			return;
		}
		StringBuilder sb=new StringBuilder();
		printNodes(node,sb);
		System. out . println(sb.toString());
		*/
	}
	public static void testParser() throws Exception
	{
//		String text="using f;";
//		String text="class Test{ f(a(1,2,3),b+4,((5))):a{}}";
//		String text="using a.b;using k;namespace hello.world{ class Test{ var:type;functionName:(a,b)=>(x:c,n:d); a:T|{a,disjunction:k|l|m}|G; foo(a:{}){z:{x:T,y:T}=5;k:t1|t2|{b,c:t3|t4}=bar();} a; b:c; f()=>(a,b) {} g(a,c){ (a:T=R1,b=R2)=2*2;a=new (ab+5);if (a==2) {while (a) { break; } return (f(g),5/2);} }}}";
//		String text="class Test{ g(a,c){}}";
		
//		String text="using k;namespace world{ class Test{x:a.b.c; [a=fn(5*3),b=6]mutable public var:type=5;[c=0]foo([k:c.d=5] a:T.e)=>([t=2] b:Z){a:T;(mutable a=x,b=y)=bar(t=soap);if (5==c) {c=6;}else if (a==b){c=7;}else{c=8;} while (true){a=6;}}}}";
//		String text="using k;namespace world{ a:n.a.m.e|t; class Test extends b{a:c.t.T|{a,disjunction:k|l|m}|G; [k=1] public mutable x:a.b.c; foo(x=5/(2+5),y)=>(b){if (a==5){v:c=(x)=>(y){t=p;};}}}}";
//		String text="class Test{a:T|{a,m}|G; [k=1] x; }";
//		String text="public class Test{a:{c|t,d,e}; [k=1] x; foo(){if (a) {break;continue;} label hello;goto hello;}}";
//		String text="x=(a)=>(){a=b;},5;";
//		String text="foo(a,a+(a)foo(a)+(c+b));";
//		String text="a[5,(a)t[5]];";
//		String text="enum Enum:/*  \"comment\\\"*/\" */ int{a=5,c,b=2}class foo{}";
//		String text="using a.b.c;";
//		String text="using k;namespace world{ class Test{x:a.b.c; [a=fn(5*3),b=6]mutable public var:type=5;[c=0]foo([k:c.d=5] a:T.e)=>([t=2] b:Z){a:T;(mutable a=x,b=y)=bar(t=soap);if (5==c) {c=6;}else if (a==b){c=7;}else{c=8;} while (true){a=6;}}}}";
//		String text="using a=k.a;namespace ns{class T{[a=2] public foo(){a=2;}mutable public x:A.B=2;}}}";
//		String text="using a=k.a;namespace ns{class T{[a=2] public foo(){t:(a,c)=>(d);mutable v=a*f(2,3);}mutable public x:A.B|int|{a:int,b,c:String|int}=2}}}";
		String text="using a=k.a;namespace ns{class T{[a=2] public foo(){using (a=5,b=6){a=5;}t:(a,c)=>(d);mutable v=a*f(2,3);}mutable public x:A.B|int|{a:int,b,c:String|int}=2}}}";

		
		System.out.println(text);
		Source source=new Source(text,null);
		Parser parser=Parser.newParser(source);
		Unit unit=parser.parse();
		UnitPrinter.print(unit);
		ArrayList<Unit> units=new ArrayList<Unit>();
		units.add(unit);
	}
	
	public static void testCompiler() throws Exception
	{
//		System.out.println(new File(".").getCanonicalPath());
		String directoryName="programs";
		ArrayList<Unit> units=new ArrayList<Unit>();
		File directory=new File(directoryName);
		for (String fileName:directory.list())
		{
			System.out.println("Reading "+fileName);
			File file=new File(directoryName+File.separator+fileName);
			java.io.FileInputStream stream=new FileInputStream(file);
			byte[] buffer=new byte[(int) file.length()];
			stream.read(buffer);
			stream.close();
			String text=new String(buffer,"UTF-8");
			Source source=new Source(text,fileName);
			Parser parser=Parser.newParser(source);
			Unit unit=parser.parse();
			units.add(unit);
			UnitPrinter.print(unit);
			
		}
		ArrayList<UnitBuilder> unitBuilders=new ArrayList<UnitBuilder>();
		Builder builder=new Builder();
		for (Unit unit:units)
		{
    		UnitBuilder unitBuilder=new UnitBuilder(builder,unit);
    		unitBuilders.add(unitBuilder);
    		unitBuilder.buildTypeNames();
		}
		//We must be able to lock step here, because we need all types before we can proceed to the next work in parallel.
		
		for (UnitBuilder unitBuilder:unitBuilders)
		{
    		unitBuilder.buildSpecifiers();
		}

	}
}

