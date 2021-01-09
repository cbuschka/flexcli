package com.github.cbuschka.flexcli.parser;

enum ParserState
{
	INITIAL,
	GLOBAL_OPTION_KEY_SEEN,
	SUB_COMMAND_SEEN,
	SUB_COMMAND_OPTION_KEY_SEEN,
	DOUBLE_DASH_SEEN,
	END;
}
