# log2prov

> Converts any log file to PROV-N.


```
java -jar log2prov-v1.0.jar -h

Log2Prov v1.0

Options:
-d <configuration_file>
-i <input_log_file>
-o <output_provn_file>
-p namespace prefix [optional]
-n namespace [optional]
```

Exemple...

```
java -jar log2prov-v1.0.jar -d etc\sample.conf -i mylog.log -o myprovlog.provn
```

## CONF File

### Structure
```
[line]
\\ specification of the boolean expression to test if line should be processed of ignored.
\\ See etc\sample.conf

[tokens]
\\ specifications of the tokens to be matched in the log file.
\\ See etc\sample.conf

[agents]
\\ specifications of the agents to be matched in the log file.
\\ See etc\sample.conf

[entities]
\\ specifications of the entities to be matched in the log file.
\\ See etc\sample.conf

[activities]
\\ specifications of the activities to be matched in the log file.
\\ See etc\sample.conf

[statements]
\\ specifications of the statements to be outputed in output prov-n file.
\\ See etc\sample.conf



```

### AST
```
expression     := IfThenExpr
                | ConcatExpr 
                | SubstringExpr 
                | ContainsExpr 
                | ReplaceExpr 
                | TestRegexpExpr 
                | MatchExpr 
                | BooleanExpr 
                | AssignExpr 
                | StatementExpr 
                | StringLiteral
                | NumberLiteral
                | $identifier   
               
IfThenExpr     := BooleanExpr ? expression : expression
                | BooleanExpr ? expression 
                 
BooleanExpr    := TestRegexpExpr 
                | ContainsExpr 
                | true 
                | false
                
ConcatExpr     := stringLiteral + stringLiteral | stringLiteral + ConcatExpr            
               
TestRegexpExpr := testRegexp(stringLiteral, stringLiteral)
               
ContainsExpr   := stringLiteral.contains(stringLiteral)
               
AssignExpr     := identifier = expression
               
ReplaceExpr    := stringLiteral.replace(stringLiteral, stringLiteral) 
                | ReplaceExpr.replace(stringLiteral, stringLiteral)
               
MatchExpr      := stringLiteral.match(stringLiteral) 
                | MatchExpr.match(stringLiteral)
			    
SubstringExpr  := stringLiteral.substring(numberLiteral, numberLiteral) 
                | SubstringExpr.substring(numberLiteral, numberLiteral)
               
StatementExpr  := actedOnBehalfOf($identifier, $identifier) 
                | wasAttributedTo($identifier, $identifier) 
                | wasDerivedFrom($identifier, $identifier) 
                | wasGeneratedBy($identifier, $identifier) 
                | used($identifier, $identifier) 
                | wasAssociatedWith($identifier, $identifier)
               
identifier     := [A-Za-z][A-Za-z0-9]*
               
numberLiteral  := [0-9]+
               
stringLiteral  := ".*"
               
literal        := $identifier | stringLiteral | numberLiteral
```

*$identifier points to the content defined inside the identifier.*

*$line points to the content of the current line being processed.*

**IC/UFF**

**2019.2**