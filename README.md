# CompressedLiterature2
This is a joint project between Bruce Taiga Baker and Jake McKenzie for our data structures course.

Our instructor a large collection of raw text files of famous literature, including Leo Tolstoy’s War and Peace consisting of over 3 million characters and about 22000 different words (counting differences in capitalization), and he’d like to store these works more efficiently. David Huffman developed a very efficient method for compressing data based on character frequency in a message. We applied this method in a straightforward way in Assignment 3 to compress the literature but our instructor has a better idea on how to apply Huffman’s encoding.

Since all the literature in my collection is in the English language it is reasonable to suppose
they will not just have commonly used characters but commonly used words as well. Our instructor's idea is
that if we can treat every distinct word as a symbol and every non-word character (white space,
punctuation, etc.) also as a symbol then we can apply Huffman’s encoding based on the
frequency of words in the text instead of characters. Our instructor proposes this will help us compress the
text much smaller.

To carry this out we need an efficient way to store each word as we encounter it and the
associated count it accumulates as we scan the entire document. While a binary search tree
may work, the constant access time of a hash table is more attractive. We will implement a
hash table to help store the word counts and eventual codes for each word. We will then
modify a solution to Assignment 4 to count words instead of characters.
