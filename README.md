# log2prov

> Converts a log file to PROV-N through a definitions file.


# Usage

```
java -jar log2prov-v1.0.jar -h

Log2Prov v1.0

Options:
-d <definitions_file>
-i <input_log_file>
-o <output_provn_file>
-p namespace prefix [optional, default = default]
-n namespace [optional, default = #]
```

# Example

> java -jar log2prov-v1.0.jar -d [etc\sample.conf](http://www.github.com/gems-uff/log2prov/blob/master/etc/sample.conf) -i [etc\sample.log](http://www.github.com/gems-uff/log2prov/blob/master/etc/sample.log) -o etc\sample.provn


# Definitions File

## Structure
```
[line]
\\ specification of the boolean expression to test if the line should be processed or ignored.
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
\\ specifications of the statements to be outputed in the prov-n file.
\\ See etc\sample.conf



```

## BNF
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
                 
BooleanExpr    := ParenthesisExpr 
                | AndExpr
                | OrExpr
                | NotExpr
                | TestRegexpExpr 
                | ContainsExpr 
                | true 
                | false
                
ParentesisExpr := (BooleanExpr)

AndExpr        := BooleanExpr && BooleanExpr 

OrExpr         := BooleanExpr || BooleanExpr 
                
NotExpr        := !BooleanExpr
                            
TestRegexpExpr := testRegexp(stringLiteral, stringLiteral)

ContainsExpr   := stringLiteral.contains(stringLiteral)
               
ConcatExpr     := stringLiteral + stringLiteral 
                | stringLiteral + ConcatExpr
               
AssignExpr     := identifier = expression
               
ReplaceExpr    := stringLiteral.replace(stringLiteral, stringLiteral) 
                | ReplaceExpr.replace(stringLiteral, stringLiteral)
               
MatchExpr      := stringLiteral.match(stringLiteral) 
                | MatchExpr.match(stringLiteral)
			    
SubstringExpr  := stringLiteral.substring(numberLiteral, numberLiteral) 
                | SubstringExpr.substring(numberLiteral, numberLiteral)
               
StatementExpr  := actedOnBehalfOf($literal, $literal, -) 
                | used($literal, $literal, -)
                | wasDerivedFrom($literal, $literal, -)
                | wasGeneratedBy($literal, $literal, -)
                | wasAssociatedWith($literal, $literal, -)
                | wasAttributedTo($literal, $literal)
                | wasInformedBy($literal, $literal)
               
identifier     := [A-Za-z][A-Za-z0-9]*
               
numberLiteral  := [0-9]+
               
stringLiteral  := \"(?:[^\"\\\\]|\\\\.)*\"
               
literal        := $identifier | stringLiteral | numberLiteral
```

*$identifier points to the content defined inside the identifier.*

*$line points to the content of the current line being processed.*

# License

Copyright (c) 2018-2022 Universidade Federal Fluminense (UFF)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
