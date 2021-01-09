package com.github.cbuschka.flexcli.scanner;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Scanner
{
	private final WordReader input;

	private final List<Token> buffer = new ArrayList<>();

	public Scanner(Reader reader)
	{
		this.input = new WordReader(reader);
	}

	public Token next()
	{
		if (!this.buffer.isEmpty())
		{
			return this.buffer.remove(0);
		}

		Word word = this.input.read();
		if (word.value == null)
		{
			return new Token(TokenType.EOF, null, null, word.pos, null);
		}

		if (word.value.equals("--"))
		{
			this.buffer.add(new Token(TokenType.DOUBLE_DASH, null, "--", word.pos, word.value));
		}
		else if (word.value.equals("-"))
		{
			this.buffer.add(new Token(TokenType.SINGLE_DASH, null, "-", word.pos, word.value));
		}
		else if (word.value.startsWith("-"))
		{
			int eqIndex = word.value.indexOf('=');
			int keyEnd = eqIndex == -1 ? word.value.length() : eqIndex;
			String key;
			if (word.value.startsWith("--"))
			{
				key = word.value.substring("--".length(), keyEnd);
			}
			else
			{
				key = word.value.substring("-".length(), keyEnd);
			}

			if (eqIndex > -1)
			{
				String value = word.value.substring(eqIndex + 1);
				this.buffer.add(new Token(TokenType.OPTION_PAIR, key, value, word.pos, word.value));
			}
			else
			{
				this.buffer.add(new Token(TokenType.OPTION_KEY, key, null, word.pos, word.value));
			}
		}
		else
		{
			this.buffer.add(new Token(TokenType.ARGUMENT, null, word.value, word.pos, word.value));
		}

		return this.buffer.remove(0);
	}
}
