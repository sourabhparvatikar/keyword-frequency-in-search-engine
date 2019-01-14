1. The main function in keywordcounter.java reads the input file name, opens it and creates a write file called “output_file.txt”.
2. If a line starts with $, it reads the string as an input, else as a query.
3. If the keyword in input already exists, it calls increaseKey for that keyword, else it
creates a new Node object and calls insert.
4. If it is a query, it tries to parse it as an integer. If an exception occurs, it closes the files
and exits else it calls removeMax for the number of times mentioned in the query. It inserts the obtained max again into the
Fibonacci Heap after writing them to the output file.
