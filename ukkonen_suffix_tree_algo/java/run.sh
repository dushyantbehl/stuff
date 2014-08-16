#!/bin/sh

HELLO="Breathe, Breathing the Air. Don't be afraid to care."

echo $HELLO

##Compile the files
javac Index.java Main.java Node.java SuffixTree.java

if [ -z "$1" ]; then
    # Inputfile not specified please specify the input file.
    echo "Please specify input and output file as the first and second arguments."
    echo "usage ./run.sh <input-file> <output-file>"
    exit 1
fi
if [ -z "$2" ]; then
    # Inputfile not specified please specify the input file.
    echo "Please specify input and output file as the first and second arguments"
    echo "usage ./run.sh <input-file> <output-file>"
else
    # Run the program with input file as first argument.
    java Main $1 $2
fi
