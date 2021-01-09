package com.github.cbuschka.flexcli.scanner;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

class WordReader
{
	private final PushbackReader reader;
	private int pos = 0;

	public WordReader(Reader reader)
	{
		this.reader = new PushbackReader(reader, 2);
	}

	private void skipSpaces() throws IOException
	{
		while (true)
		{
			int c = this.reader.read();
			this.pos++;

			if (c == -1)
			{
				this.pos--;
				return;
			}
			else if (Character.isWhitespace(c))
			{
				continue;
			}
			else
			{
				this.reader.unread(c);
				this.pos--;
				return;
			}
		}
	}

	private String readUntilSpaceOrEof() throws IOException
	{
		StringBuilder buf = new StringBuilder();
		while (true)
		{
			int c = this.reader.read();
			this.pos++;

			if (c == -1)
			{
				this.pos--;
				break;
			}
			else if (Character.isWhitespace(c))
			{
				break;
			}
			else if (Character.isWhitespace(c))
			{
				break;
			}
			else
			{
				buf.append((char) c);
			}
		}

		return buf.length() > 0 ? buf.toString() : null;
	}

	public Word read()
	{
		try
		{
			skipSpaces();

			int wordPos = this.pos;
			String word = readUntilSpaceOrEof();

			return new Word(word, wordPos);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
