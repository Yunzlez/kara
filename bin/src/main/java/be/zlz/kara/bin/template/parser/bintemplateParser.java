// Generated from C:/JEE/Projects/zlzbin-j/bin/src/main/resources/antlr\bintemplate.g4 by ANTLR 4.7
package be.zlz.kara.bin.template.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class bintemplateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, ID=3, TEXT=4, LETTER=5, ANY=6;
	public static final int
		RULE_template = 0, RULE_placeholder = 1;
	public static final String[] ruleNames = {
		"template", "placeholder"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'{{'", "'}}'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, "ID", "TEXT", "LETTER", "ANY"
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

	@Override
	public String getGrammarFileName() { return "bintemplate.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public bintemplateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TemplateContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(bintemplateParser.EOF, 0); }
		public List<TerminalNode> TEXT() { return getTokens(bintemplateParser.TEXT); }
		public TerminalNode TEXT(int i) {
			return getToken(bintemplateParser.TEXT, i);
		}
		public List<PlaceholderContext> placeholder() {
			return getRuleContexts(PlaceholderContext.class);
		}
		public PlaceholderContext placeholder(int i) {
			return getRuleContext(PlaceholderContext.class,i);
		}
		public TemplateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_template; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof bintemplateListener ) ((bintemplateListener)listener).enterTemplate(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof bintemplateListener ) ((bintemplateListener)listener).exitTemplate(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof bintemplateVisitor ) return ((bintemplateVisitor<? extends T>)visitor).visitTemplate(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TemplateContext template() throws RecognitionException {
		TemplateContext _localctx = new TemplateContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_template);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(8);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0 || _la==TEXT) {
				{
				setState(6);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case TEXT:
					{
					setState(4);
					match(TEXT);
					}
					break;
				case T__0:
					{
					setState(5);
					placeholder();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(10);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(11);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PlaceholderContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(bintemplateParser.ID, 0); }
		public PlaceholderContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_placeholder; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof bintemplateListener ) ((bintemplateListener)listener).enterPlaceholder(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof bintemplateListener ) ((bintemplateListener)listener).exitPlaceholder(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof bintemplateVisitor ) return ((bintemplateVisitor<? extends T>)visitor).visitPlaceholder(this);
			else return visitor.visitChildren(this);
		}
	}

	public final PlaceholderContext placeholder() throws RecognitionException {
		PlaceholderContext _localctx = new PlaceholderContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_placeholder);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(13);
			match(T__0);
			setState(14);
			match(ID);
			setState(15);
			match(T__1);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\b\24\4\2\t\2\4\3"+
		"\t\3\3\2\3\2\7\2\t\n\2\f\2\16\2\f\13\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\2\2"+
		"\4\2\4\2\2\2\23\2\n\3\2\2\2\4\17\3\2\2\2\6\t\7\6\2\2\7\t\5\4\3\2\b\6\3"+
		"\2\2\2\b\7\3\2\2\2\t\f\3\2\2\2\n\b\3\2\2\2\n\13\3\2\2\2\13\r\3\2\2\2\f"+
		"\n\3\2\2\2\r\16\7\2\2\3\16\3\3\2\2\2\17\20\7\3\2\2\20\21\7\5\2\2\21\22"+
		"\7\4\2\2\22\5\3\2\2\2\4\b\n";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}