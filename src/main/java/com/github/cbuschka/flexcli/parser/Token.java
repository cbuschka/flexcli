package com.github.cbuschka.flexcli.parser;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Token
{
	public final TokenType type;

	public final String key;

	public final String value;

	public final int pos;

	public final String raw;
}
