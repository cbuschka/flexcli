package com.github.cbuschka.flexcli.parser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class CommandLine
{
	@Getter
	private final Map<String, Object> globalOptions;
	@Getter
	private final String subCommand;
	@Getter
	private final Map<String, Object> subCommandOptions;
	@Getter
	private final List<String> arguments;
}
