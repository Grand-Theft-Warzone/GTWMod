package me.phoenixra.libs.crunch.functional;

import me.phoenixra.libs.crunch.token.Token;
import me.phoenixra.libs.crunch.token.TokenType;
import me.phoenixra.libs.crunch.token.Value;

/**
 * Represents a list of arguments being passed to a Function
 * @author Redempt
 */
public class ArgumentList implements Token {
	
	private Value[] arguments;
	
	public ArgumentList(Value[] arguments) {
		this.arguments = arguments;
	}
	
	public Value[] getArguments() {
		return arguments;
	}
	
	@Override
	public TokenType getType() {
		return TokenType.ARGUMENT_LIST;
	}
	
}
