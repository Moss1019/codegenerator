// Generated from Database.g4 by ANTLR 4.9.2

package com.mossonthetree.codegeneratorlib.parser;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link DatabaseParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface DatabaseVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#database}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatabase(DatabaseParser.DatabaseContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#table}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTable(DatabaseParser.TableContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#joined}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJoined(DatabaseParser.JoinedContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#looped}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLooped(DatabaseParser.LoopedContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#column}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitColumn(DatabaseParser.ColumnContext ctx);
	/**
	 * Visit a parse tree produced by {@link DatabaseParser#option}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOption(DatabaseParser.OptionContext ctx);
}