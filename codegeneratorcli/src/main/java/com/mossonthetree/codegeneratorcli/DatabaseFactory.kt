package com.mossonthetree.codegeneratorcli

import com.mossonthetree.codegeneratorlib.Database
import com.mossonthetree.codegeneratorlib.DatabaseType
import com.mossonthetree.codegeneratorlib.DatabaseVisitor
import com.mossonthetree.codegeneratorlib.Table
import com.mossonthetree.codegeneratorlib.parser.DatabaseLexer
import com.mossonthetree.codegeneratorlib.parser.DatabaseParser
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import java.lang.Exception

class DatabaseFactory {
    fun create(definition: String, root: String, projectName: String, databaseType: DatabaseType): Database? {
        try {
            val antlrCharStream = CharStreams.fromString(definition)
            val databaseLexer = DatabaseLexer(antlrCharStream)
            val tokens = CommonTokenStream(databaseLexer)
            val parser = DatabaseParser(tokens)
            val tree = parser.database()
            val visitor = DatabaseVisitor()

            return Database(root, projectName, databaseType, visitor.visit(tree) as MutableList<Table>?)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return null
    }
}