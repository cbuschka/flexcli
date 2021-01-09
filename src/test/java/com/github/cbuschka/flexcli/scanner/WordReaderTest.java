package com.github.cbuschka.flexcli.scanner;

import org.junit.Test;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class WordReaderTest
{
	private WordReader input;
	private List<Word> words;

	@Test
	public void shouldReturnEofForEmptyInput()
	{
		givenInput("");

		whenRead();

		thenWordsAre(new Word(null, 0));
	}


	@Test
	public void shouldReturnWordsWithoutSpaces()
	{
		givenInput(" aWord  secondWord      word3 ");

		whenRead();

		thenWordsAre(new Word("aWord", 1), new Word("secondWord", 8), new Word("word3", 24), new Word(null, 30));
	}

	@Test
	public void shouldReturnEofForWhitespaceInput()
	{
		givenInput("   \t \r  \n");

		whenRead();

		thenWordsAre(new Word(null, 9));
	}

	private void thenWordsAre(Word... words)
	{
		assertThat(this.words).isEqualTo(Arrays.asList(words));
	}

	private void whenRead()
	{
		List<Word> words = new ArrayList<>();
		while (true)
		{
			Word word = this.input.read();
			words.add(word);
			if (word.value == null)
			{
				break;
			}
		}

		this.words = words;
	}

	private void givenInput(String s)
	{
		this.input = new WordReader(new StringReader(s));
	}


}
