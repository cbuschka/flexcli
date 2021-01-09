package com.github.cbuschka.flexcli.parser;

import com.github.cbuschka.flexcli.scanner.Scanner;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser
{
	private final Scanner scanner;

	private final List<com.github.cbuschka.flexcli.scanner.Token> tokenStack = new ArrayList<>();

	private ParserState state = ParserState.INITIAL;

	private final List<Token> buffer = new LinkedList<>();

	public Parser(Scanner scanner)
	{
		this.scanner = scanner;
	}

	public Token next()
	{
		while (this.buffer.isEmpty())
		{
			com.github.cbuschka.flexcli.scanner.Token token = this.scanner.next();
			switch (this.state)
			{
				case INITIAL:
					initial(token);
					break;
				case SUB_COMMAND_SEEN:
					subCommandSeen(token);
					break;
				case GLOBAL_OPTION_KEY_SEEN:
					globalOptionKeySeen(token);
					break;
				case SUB_COMMAND_OPTION_KEY_SEEN:
					subCommandOptionKeySeen(token);
					break;
				case END:
					this.buffer.add(new Token(TokenType.EOF, null, null, token.pos, token.raw));
				case DOUBLE_DASH_SEEN:
					doubleDashSeen(token);
					break;
				default:
					throw new IllegalStateException("Unexpected state " + this.state + " (token=" + token + ").");
			}
		}

		return this.buffer.remove(0);
	}

	private void doubleDashSeen(com.github.cbuschka.flexcli.scanner.Token token)
	{
		switch (token.type)
		{
			case ARGUMENT:
			case OPTION_KEY:
			case SINGLE_DASH:
			case OPTION_PAIR:
				this.buffer.add(new Token(TokenType.ARGUMENT, null, token.raw, token.pos, token.raw));
				break;
			case EOF:
				this.buffer.add(new Token(TokenType.EOF, null, null, token.pos, token.raw));
				this.state = ParserState.END;
				break;
			default:
				throw new IllegalArgumentException("Unexpected input " + token + " in state " + this.state + " .");
		}

	}

	private void subCommandOptionKeySeen(com.github.cbuschka.flexcli.scanner.Token token)
	{
		switch (token.type)
		{
			case ARGUMENT:
			{
				com.github.cbuschka.flexcli.scanner.Token keyToken = this.tokenStack.remove(this.tokenStack.size() - 1);
				this.buffer.add(new Token(TokenType.SUB_COMMAND_OPTION_KEY, keyToken.key, null, keyToken.pos, keyToken.raw));
				this.buffer.add(new Token(TokenType.ARGUMENT, null, token.value, token.pos, token.raw));
				this.state = ParserState.SUB_COMMAND_SEEN;
			}
			break;
			case EOF:
			{
				com.github.cbuschka.flexcli.scanner.Token keyToken = this.tokenStack.remove(this.tokenStack.size() - 1);
				this.buffer.add(new Token(TokenType.SUB_COMMAND_OPTION_KEY, keyToken.key, null, keyToken.pos, keyToken.raw));
				this.buffer.add(new Token(TokenType.EOF, null, null, token.pos, token.raw));
			}
			break;
			default:
				throw new IllegalArgumentException("Unexpected input " + token + " in state " + this.state + " .");
		}
	}

	private void globalOptionKeySeen(com.github.cbuschka.flexcli.scanner.Token token)
	{
		switch (token.type)
		{
			case ARGUMENT:
			{
				com.github.cbuschka.flexcli.scanner.Token keyToken = this.tokenStack.remove(this.tokenStack.size() - 1);
				this.buffer.add(new Token(TokenType.GLOBAL_OPTION_KEY, keyToken.key, null, keyToken.pos, keyToken.raw));
				this.buffer.add(new Token(TokenType.ARGUMENT, null, token.value, token.pos, token.raw));
				this.state = ParserState.INITIAL;
			}
			break;
			case EOF:
			{
				com.github.cbuschka.flexcli.scanner.Token keyToken = this.tokenStack.remove(this.tokenStack.size() - 1);
				this.buffer.add(new Token(TokenType.GLOBAL_OPTION_KEY, keyToken.key, null, keyToken.pos, keyToken.raw));
				this.buffer.add(new Token(TokenType.EOF, null, null, token.pos, token.raw));
				this.state = ParserState.END;
			}
			break;
			default:
				throw new IllegalArgumentException("Unexpected input " + token + " in state " + this.state + " .");
		}
	}

	private void subCommandSeen(com.github.cbuschka.flexcli.scanner.Token token)
	{
		switch (token.type)
		{
			case ARGUMENT:
				this.buffer.add(new Token(TokenType.ARGUMENT, null, token.value, token.pos, token.raw));
				break;
			case OPTION_KEY:
				this.tokenStack.add(token);
				this.state = ParserState.SUB_COMMAND_OPTION_KEY_SEEN;
				break;
			case DOUBLE_DASH:
				this.state = ParserState.DOUBLE_DASH_SEEN;
				break;
			case OPTION_PAIR:
				this.buffer.add(new Token(TokenType.SUB_COMMAND_OPTION_PAIR, token.key, token.value, token.pos, token.raw));
				this.state = ParserState.SUB_COMMAND_SEEN;
				break;
			case EOF:
				this.state = ParserState.END;
				break;
			default:
				throw new IllegalArgumentException("Unexpected input " + token + " in state " + this.state + " .");
		}
	}

	private void initial(com.github.cbuschka.flexcli.scanner.Token token)
	{
		switch (token.type)
		{
			case ARGUMENT:
				this.buffer.add(new Token(TokenType.SUB_COMMAND, null, token.value, token.pos, token.raw));
				this.state = ParserState.SUB_COMMAND_SEEN;
				break;
			case OPTION_KEY:
				this.tokenStack.add(token);
				this.state = ParserState.GLOBAL_OPTION_KEY_SEEN;
				break;
			case OPTION_PAIR:
				this.buffer.add(new Token(TokenType.GLOBAL_OPTION_PAIR, token.key, token.value, token.pos, token.raw));
				this.state = ParserState.INITIAL;
				break;
			case EOF:
				this.state = ParserState.END;
				break;
			case DOUBLE_DASH:
				this.state = ParserState.DOUBLE_DASH_SEEN;
				break;
			default:
				throw new IllegalArgumentException("Unexpected input " + token + " in state " + this.state + " .");
		}
	}
}
