package com.github.cbuschka.flexcli.scanner;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@AllArgsConstructor
@ToString
public class Word
{
	public final String value;

	public final int pos;
}
