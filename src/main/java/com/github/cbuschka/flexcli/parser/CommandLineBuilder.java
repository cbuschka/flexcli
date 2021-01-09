package com.github.cbuschka.flexcli.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandLineBuilder
{
	private final Parser parser;

	private Map<String, Object> globalOptions = new HashMap<>();
	private String subCommand = null;
	private Map<String, Object> subCommandOptions = new HashMap<>();
	private List<String> arguments = new ArrayList<>();

	public CommandLineBuilder(Parser parser)
	{
		this.parser = parser;
	}

	public CommandLine parseCommandLine()
	{
		while (true)
		{
			Token token = this.parser.next();
			switch (token.type)
			{
				case SUB_COMMAND:
					this.subCommand = token.value;
					break;
				case ARGUMENT:
					this.arguments.add(token.value);
					break;
				case SUB_COMMAND_OPTION_KEY:
				case SUB_COMMAND_OPTION_PAIR:
					this.subCommandOptions.put(token.key, token.value);
					break;
				case GLOBAL_OPTION_KEY:
				case GLOBAL_OPTION_PAIR:
					this.globalOptions.put(token.key, token.value);
					break;
				case EOF:
					return new CommandLine(this.globalOptions, this.subCommand, this.subCommandOptions, this.arguments);
			}
		}
	}
}
