// Generated from TemplateLexer.g4 by ANTLR 4.7.1
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TemplateLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		OPEN_TMP=1, TMP_ESC=2, TMP_TEXT=3, CLOSE_TMP=4, DOT=5, ID=6, LETTER=7, 
		NUMBER=8, DIGIT=9, WS=10;
	public static final int
		TMP=1;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE", "TMP"
	};

	public static final String[] ruleNames = {
		"OPEN_TMP", "TMP_ESC", "TMP_TEXT", "CLOSE_TMP", "ESC", "UNICODE", "HEX", 
		"DOT", "ID", "LETTER", "NUMBER", "DIGIT", "WS"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'${'", "'\\$'", null, "'}'", "'.'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "OPEN_TMP", "TMP_ESC", "TMP_TEXT", "CLOSE_TMP", "DOT", "ID", "LETTER", 
		"NUMBER", "DIGIT", "WS"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public TemplateLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TemplateLexer.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\fP\b\1\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\3\4\6"+
		"\4(\n\4\r\4\16\4)\3\5\3\5\3\5\3\5\3\6\3\6\3\6\5\6\63\n\6\3\7\3\7\3\7\3"+
		"\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\6\n@\n\n\r\n\16\nA\3\13\3\13\3\f\6\fG\n"+
		"\f\r\f\16\fH\3\r\3\r\3\16\3\16\3\16\3\16\2\2\17\4\3\6\4\b\5\n\6\f\2\16"+
		"\2\20\2\22\7\24\b\26\t\30\n\32\13\34\f\4\2\3\b\3\2&&\f\2$$\61\61^^ddh"+
		"hppttvv}}\177\177\5\2\62;CHch\4\2C\\c|\3\2\62;\5\2\13\f\17\17\"\"\2O\2"+
		"\4\3\2\2\2\2\6\3\2\2\2\2\b\3\2\2\2\3\n\3\2\2\2\3\22\3\2\2\2\3\24\3\2\2"+
		"\2\3\26\3\2\2\2\3\30\3\2\2\2\3\32\3\2\2\2\3\34\3\2\2\2\4\36\3\2\2\2\6"+
		"#\3\2\2\2\b\'\3\2\2\2\n+\3\2\2\2\f/\3\2\2\2\16\64\3\2\2\2\20:\3\2\2\2"+
		"\22<\3\2\2\2\24?\3\2\2\2\26C\3\2\2\2\30F\3\2\2\2\32J\3\2\2\2\34L\3\2\2"+
		"\2\36\37\7&\2\2\37 \7}\2\2 !\3\2\2\2!\"\b\2\2\2\"\5\3\2\2\2#$\7^\2\2$"+
		"%\7&\2\2%\7\3\2\2\2&(\n\2\2\2\'&\3\2\2\2()\3\2\2\2)\'\3\2\2\2)*\3\2\2"+
		"\2*\t\3\2\2\2+,\7\177\2\2,-\3\2\2\2-.\b\5\3\2.\13\3\2\2\2/\62\7^\2\2\60"+
		"\63\t\3\2\2\61\63\5\16\7\2\62\60\3\2\2\2\62\61\3\2\2\2\63\r\3\2\2\2\64"+
		"\65\7w\2\2\65\66\5\20\b\2\66\67\5\20\b\2\678\5\20\b\289\5\20\b\29\17\3"+
		"\2\2\2:;\t\4\2\2;\21\3\2\2\2<=\7\60\2\2=\23\3\2\2\2>@\5\26\13\2?>\3\2"+
		"\2\2@A\3\2\2\2A?\3\2\2\2AB\3\2\2\2B\25\3\2\2\2CD\t\5\2\2D\27\3\2\2\2E"+
		"G\5\32\r\2FE\3\2\2\2GH\3\2\2\2HF\3\2\2\2HI\3\2\2\2I\31\3\2\2\2JK\t\6\2"+
		"\2K\33\3\2\2\2LM\t\7\2\2MN\3\2\2\2NO\b\16\4\2O\35\3\2\2\2\b\2\3)\62AH"+
		"\5\7\3\2\6\2\2\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}