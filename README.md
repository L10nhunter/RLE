# Terrible RLE Code Implementation (Java)
## Description
This is a terrible implementation of the RLE algorithm in Java. It is not efficient, and it should not be used in any project. It is just a simple implementation to show how the RLE algorithm works in Pokémon Red and Blue, and even then, it is wrong because I had to cut the XOR section.

## How to use
you can download the code for use in any Java IDE, I used IntelliJ. Download the code, open it in your IDE, and run the code. If you want to change the input, the new Pokémon file should be named {Pokémon}Sprite.png, and it needs to go in the /Main/Images/{Pokémon} directory. It also must be a sprite in 4 color mode. The outputs will also be in the /Main/Images/{Pokémon}. This code does implement the high-low switching, as well as the encoding modes, so each output will go into the subdirectories of the {Pokémon} directory, {Pokémon}/{high/low}/{mode}. The final compressed Pokémon sprite, as it would be rendered if you looked at the ROM with a visualizer, will be in {Pokémon}ZipperedRLEEncoded.png. 

## Tests
I did push my tests to this repo, but they are pretty spaghetti. The easiest way to check if it's working or not is to check the {Pokémon}Sprite.png against the {Pokémon}Merged.png. This file is the result of the given sprite being compressed and decompressed. If the two files are the same, then the code is probably working as intended. Feel free to make issues if something breaks, and I may or may not try to fix it when I have the time. 