package com.github.cbuschka.flexcli.parser;

import com.github.cbuschka.flexcli.scanner.Scanner;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandLineBuilderIntegrationTest
{
	private CommandLineBuilder commandLineBuilder;
	private CommandLine commandLine;

	private void givenInput(String s)
	{
		this.commandLineBuilder = new CommandLineBuilder(new Parser(new Scanner(new StringReader(s))));
	}

	@Test
	public void shouldParseSubCommand()
	{
		givenInput("subcommand");

		whenParsed();

		assertThat(this.commandLine.getSubCommand()).isEqualTo("subcommand");
	}


	@Test
	public void shouldParseSubCommandAndValue()
	{
		givenInput("subcommand argument");

		whenParsed();

		assertThat(this.commandLine.getSubCommand()).isEqualTo("subcommand");
		assertThat(this.commandLine.getArguments()).isEqualTo(Arrays.asList("argument"));
	}

	@Test
	public void shouldParseGlobalOptionPair()
	{
		givenInput("--key=value");

		whenParsed();

		assertThat(this.commandLine.getGlobalOptions().get("key")).isEqualTo("value");
	}

	@Test
	public void shouldParseGlobalOptionKey()
	{
		givenInput("--key");

		whenParsed();

		assertThat(this.commandLine.getGlobalOptions().get("key")).isNull();
	}

	@Test
	public void shouldParseSubCommandAndSubCommandOptionPair()
	{
		givenInput("subcommand --key=value");

		whenParsed();

		assertThat(this.commandLine.getGlobalOptions()).isEmpty();
		assertThat(this.commandLine.getSubCommandOptions().get("key")).isEqualTo("value");
	}


	@Test
	public void shouldParseSubCommandAndSubCommandOptionKey()
	{
		givenInput("subcommand --key");

		whenParsed();

		assertThat(this.commandLine.getGlobalOptions()).isEmpty();
		assertThat(this.commandLine.getSubCommandOptions().get("key")).isNull();
	}

	@Test
	public void shouldParseAllAfterDoubleDashAsArguments()
	{
		givenInput("-- any - -f --foo --foo2=bar -x=y");

		whenParsed();

		assertThat(this.commandLine.getGlobalOptions()).isEmpty();
		assertThat(this.commandLine.getSubCommandOptions()).isEmpty();
		assertThat(this.commandLine.getSubCommand()).isNull();
		assertThat(this.commandLine.getArguments()).isEqualTo(Arrays.asList("any", "-", "-f", "--foo", "--foo2=bar", "-x=y"));
	}

	private void whenParsed()
	{
		this.commandLine = this.commandLineBuilder.parseCommandLine();
	}
}
