package com.github.cbuschka.flexcli.parser;

import com.github.cbuschka.flexcli.scanner.Scanner;
import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ParserTest
{
	private Parser parser;
	private List<Token> tokens;

	private void givenInput(String s)
	{
		this.parser = new Parser(new Scanner(new StringReader(s)));
	}

	@Test
	public void shouldParseSubCommand()
	{
		givenInput("subcommand");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(new Token(TokenType.SUB_COMMAND, null, "subcommand", 0, "subcommand"),
				new Token(TokenType.EOF, null, null, 10, null)));
	}


	@Test
	public void shouldParseSubCommandAndValue()
	{
		givenInput("subcommand argument");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(new Token(TokenType.SUB_COMMAND, null, "subcommand", 0, "subcommand"),
				new Token(TokenType.ARGUMENT, null, "argument", 11, "argument"),
				new Token(TokenType.EOF, null, null, 19, null)));
	}

	@Test
	public void shouldParseGlobalOptionPair()
	{
		givenInput("--key=value");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(new Token(TokenType.GLOBAL_OPTION_PAIR, "key", "value", 0, "--key=value"),
				new Token(TokenType.EOF, null, null, 11, null)));
	}

	@Test
	public void shouldParseGlobalOptionKey()
	{
		givenInput("--key");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(
				new Token(TokenType.GLOBAL_OPTION_KEY, "key", null, 0, "--key"),
				new Token(TokenType.EOF, null, null, 5, null)));
	}

	@Test
	public void shouldParseSubCommandAndSubCommandOptionPair()
	{
		givenInput("subcommand --key=value");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(
				new Token(TokenType.SUB_COMMAND, null, "subcommand", 0, "subcommand"),
				new Token(TokenType.SUB_COMMAND_OPTION_PAIR, "key", "value", 11, "--key=value"),
				new Token(TokenType.EOF, null, null, 22, null)));
	}


	@Test
	public void shouldParseSubCommandAndSubCommandOptionKey()
	{
		givenInput("subcommand --key");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(
				new Token(TokenType.SUB_COMMAND, null, "subcommand", 0, "subcommand"),
				new Token(TokenType.SUB_COMMAND_OPTION_KEY, "key", null, 11, "--key=value"),
				new Token(TokenType.EOF, null, null, 14, null)));
	}

	@Test
	public void shouldParseAllAfterDoubleDashAsArguments()
	{
		givenInput("-- - -f --foo --foo2=bar");

		whenParsed();

		assertThat(this.tokens).isEqualTo(Arrays.asList(
				new Token(TokenType.ARGUMENT, null, "-", 3, "-"),
				new Token(TokenType.ARGUMENT, null, "-f", 5, "-f"),
				new Token(TokenType.ARGUMENT, null, "--foo", 8, "--foo"),
				new Token(TokenType.ARGUMENT, null, "--foo2=bar", 14, "--foo2=bar"),
				new Token(TokenType.EOF, null, null, 24, null)));
	}


	private void whenParsed()
	{
		List<Token> tokens = new ArrayList<>();
		while (true)
		{
			Token token = this.parser.next();
			tokens.add(token);
			if (token.type == TokenType.EOF)
			{
				break;
			}
		}
		this.tokens = tokens;
	}
}
