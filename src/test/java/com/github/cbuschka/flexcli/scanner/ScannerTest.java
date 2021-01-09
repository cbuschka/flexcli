package com.github.cbuschka.flexcli.scanner;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ScannerTest
{
	private Reader input;

	private ArrayList<Token> tokens;

	@Test
	public void shouldReturnSingleArgument()
	{
		givenInput("word");

		whenScanned();

		thenTokensAre(new Token(TokenType.ARGUMENT, null, "word", 0, "word"), new Token(TokenType.EOF, null, null, 4, null));
	}

	@Test
	public void shouldReturnEol()
	{
		givenInput("");

		whenScanned();

		thenTokensAre(new Token(TokenType.EOF, null, null, 0, null));
	}


	@Test
	public void shouldReturnDoudbleDash()
	{
		givenInput("--");

		whenScanned();

		thenTokensAre(new Token(TokenType.DOUBLE_DASH, null, "--", 0, "--"), new Token(TokenType.EOF, null, null, 2, null));
	}

	@Test
	public void shouldReturnSingleDash()
	{
		givenInput("-");

		whenScanned();

		thenTokensAre(new Token(TokenType.SINGLE_DASH, null, "-", 0, "-"), new Token(TokenType.EOF, null, null, 1, null));
	}

	@Test
	public void shouldReturnShortOptionKey()
	{
		givenInput("-p");

		whenScanned();

		thenTokensAre(new Token(TokenType.OPTION_KEY,  "p", null,0, "-p"), new Token(TokenType.EOF, null, null, 2, null));
	}

	@Test
	public void shouldReturnLongOptionKey()
	{
		givenInput("--p");

		whenScanned();

		thenTokensAre(new Token(TokenType.OPTION_KEY, "p", null, 0, "--p"), new Token(TokenType.EOF, null, null, 3, null));
	}

	@Test
	public void shouldReturnLongOptionKeyValue()
	{
		givenInput("--key=value");

		whenScanned();

		thenTokensAre(new Token(TokenType.OPTION_PAIR, "key", "value", 0, "--key=value"), new Token(TokenType.EOF, null, null, 11, null));
	}


	private void thenTokensAre(Token... tokens)
	{
		assertThat(this.tokens).isEqualTo(Arrays.asList(tokens));
	}

	private void whenScanned()
	{
		Scanner scanner = new Scanner(this.input);
		this.tokens = new ArrayList<Token>();
		while (true)
		{
			Token token = scanner.next();
			tokens.add(token);
			if (token.type == TokenType.EOF)
			{
				break;
			}
		}
	}

	private void givenInput(String s)
	{
		this.input = new StringReader(s);
	}
}
