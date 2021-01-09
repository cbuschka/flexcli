package com.github.cbuschka.flexcli.scanner;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Token
{
	public final TokenType type;

	// defined for TokenType.OPTION_PAIR and OPTION_KEY
	public final String key;

	// defined for SINGLE_DASH, DOUBLE_DASH, ARGUMENT
	public final String value;

	public final int pos;

	public final String raw;
}
